package com.example.kotrip.service.login;

import com.example.kotrip.dto.login.response.LoginResponseDto;
import com.example.kotrip.dto.reissue.request.ReissueRequestDto;
import com.example.kotrip.dto.reissue.response.ReissueResponseDto;
import com.example.kotrip.entity.schedule.Schedule;
import com.example.kotrip.entity.user.User;
import com.example.kotrip.jwt.JwtTokenProvider;
import com.example.kotrip.repository.schedule.ScheduleRepository;
import com.example.kotrip.repository.scheduleTour.ScheduleTourRepository;
import com.example.kotrip.repository.user.UserRepository;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private static final String KAKAO_TOKEN_VALID_URL = "https://kapi.kakao.com/v1/user/access_token_info";
    private static final String POST_URL = "https://kapi.kakao.com/v2/user/me";

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleTourRepository scheduleTourRepository;

    public ReissueResponseDto reissue(ReissueRequestDto reissueRequestDto) {

        String refreshToken = reissueRequestDto.getRefreshToken();
        jwtTokenProvider.validateToken(refreshToken);

        String nickname = jwtTokenProvider.getUsername(refreshToken);
        String kakaoId = jwtTokenProvider.getKakaoId(refreshToken);

        String accessToken = jwtTokenProvider.createAccessToken(nickname,kakaoId);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(nickname,kakaoId);

        return ReissueResponseDto.of(accessToken, newRefreshToken);
    }

    @Transactional
    public String withdrawal() {
        Authentication authentication = getAuthentication();
        User user = userRepository.findUserByNickname(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
        List<Schedule> schedules = scheduleRepository.findSchedulesByUserOrderByTime(user).orElseThrow(() -> new NoSuchElementException("스케줄이 존재하지 않습니다."));

        for(Schedule schedule: schedules) {
            scheduleTourRepository.deleteScheduleToursByScheduleId(schedule.getClassificationId());
        }
        scheduleRepository.deleteSchedulesByUser(user);
        userRepository.delete(user); // 계정 삭제

        return "회원 탈퇴가 완료되었습니다.";
    }

    public LoginResponseDto login(String token) {

        log.info("login을 시작합니다.");

        validateToken(token); // 토큰 유효성 검사
        HashMap<String,Object> userInfo = getUserInfo(token);

        String nickname = String.valueOf(userInfo.get("nickname"));
        String kakaoId = String.valueOf(userInfo.get("id"));

        // 유저 정보가 존재하는가?
        Optional<User> user = userRepository.findUserByKakaoUserId(kakaoId);

        if(user.isEmpty()) { // 유저가 존재하지 않는 경우 회원가입
            log.info("계정이 존재하지 않으므로 회원가입을 진행합니다.");
            userRepository.save(User.create(kakaoId,nickname));
        }

        // 토큰 생성 (닉네임, 카카오 아이디 기반)
        String accessToken = jwtTokenProvider.createAccessToken(nickname,kakaoId);
        String refreshToken = jwtTokenProvider.createRefreshToken(nickname,kakaoId);

        // 토큰 발행
        return LoginResponseDto.of("로그인 성공", accessToken,refreshToken);
    }

    private HashMap<String, Object> getUserInfo(String accessToken) { // 토큰 기반으로 유저 정보 추출하기
        HashMap<String, Object> userInfo = new HashMap<>();
        try {
            URL url = new URL(POST_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonElement element = JsonParser.parseString(result.toString());
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String id = element.getAsJsonObject().get("id").getAsString();

            userInfo.put("nickname", nickname);
            userInfo.put("id", id);

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return userInfo;
    }

    private void validateToken(String accessToken) {
        try {

            log.info("카카오 토큰 유효성 검사 시작");
            URL url = new URL(KAKAO_TOKEN_VALID_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);

            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonElement element = JsonParser.parseString(result.toString());

            String id = element.getAsJsonObject().get("id").getAsString();

            log.info("카카오 토큰 유효성 검증 id : {}",id);

            if(id == null) {
                throw new RuntimeException("유효하지 않은 토큰");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
