package com.example.kotrip.service.login;

import com.example.kotrip.dto.login.response.LoginResponseDto;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {

    private static final String REQUEST_URL = "https://kauth.kakao.com/oauth/token";
    private static final String POST_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String REST_API_KEY = "fa498bcb6460766a009c3d798e9ac960";
    private static final String CLIENT_SECRET = "lEB0s7oBeim5d2Y4UnEBH2Owa9n32VZs";

    public LoginResponseDto login(String code) {

        log.info("login을 시작합니다.");

        String accessToken = getKakaoAccessToken(code);
        HashMap<String,Object> userInfo = getUserInfo(accessToken);

        String nickname = String.valueOf(userInfo.get("nickname"));
        String id = String.valueOf(userInfo.get("id"));

        log.info("accessToken : {}" ,accessToken);
        log.info("nickname : {}", nickname);
        log.info("id : {}", id);

        // 유저 정보가 존재하는가?

        // 유저 정보가 존재하는 경우, 시크릿키로 토큰 발급

        // 유저 정보가 존재하지 않는 경우, DB에 회원가입 후 토큰 발급

        return LoginResponseDto.of("로그인 성공");
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

    private String getKakaoAccessToken(String code){ // 카카오 엑세스 토큰 가져오기
        String accessToken = "";

        try {
            URL url = new URL(REQUEST_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            // setDoOutput()은 OutputStream으로 POST 데이터를 넘겨 주겠다는 옵션이다.
            // POST 요청을 수행하려면 setDoOutput()을 true로 설정한다.
            conn.setDoOutput(true);

            // POST 요청에서 필요한 파라미터를 OutputStream을 통해 전송
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + REST_API_KEY + // REST_API_KEY
                    "&client_secret=" + CLIENT_SECRET +
                    "&code=" + code;
            bufferedWriter.write(sb);
            bufferedWriter.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }

            JsonElement element = JsonParser.parseString(result.toString());

            accessToken = element.getAsJsonObject().get("access_token").getAsString();

            bufferedReader.close();
            bufferedWriter.close();

        } catch (IOException e) {
            // 토큰 발급이 안되는 경우 유효하지 않은 값이다.
            throw new RuntimeException(e);
        }

        return accessToken;
    }
}
