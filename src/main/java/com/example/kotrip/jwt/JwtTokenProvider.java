package com.example.kotrip.jwt;

import com.example.kotrip.repository.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${API-KEY.secretKey}")
    private String secretKey;
    private static final String NICKNAME = "nickname";
    private static final String KAKAOID = "kakaoId";
    private static final String AUTHORIZATION = "Authorization";

    private static long ACCESS_TOKEN_EXPIRED_TIME = 86400000L; // 1일
    private static long REFRESH_TOKEN_EXPIRED_TIME = 2592000000L; // 30일

    private final UserRepository userRepository;

    private SecretKey key;

    public String createAccessToken(String nickname, String kakaoId){

        key = getKey(Decoders.BASE64);

        Claims claims = Jwts.claims()
                .add(NICKNAME, nickname)
                .add(KAKAOID, kakaoId)
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ ACCESS_TOKEN_EXPIRED_TIME))
                .signWith(key)
                .compact();
    }

    private SecretKey getKey(Decoder<CharSequence, byte[]> base64) {
        log.info("secretKey : {}", secretKey);
        return Keys.hmacShaKeyFor(base64.decode(secretKey));
    }

    public String createRefreshToken(String nickname, String kakaoId) {

        key = getKey(Decoders.BASE64);

        Claims claims = Jwts.claims()
                .add(NICKNAME, nickname)
                .add(KAKAOID, kakaoId)
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ REFRESH_TOKEN_EXPIRED_TIME))
                .signWith(key)
                .compact();
    }

    public String getUsername(String token) {
        log.info("secretKey : {}", secretKey);
        key = getKey(Decoders.BASE64);
        System.out.println(token);
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get(NICKNAME,String.class);
    }

    public String getKakaoId(String token) {
        key = getKey(Decoders.BASE64URL);
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get(KAKAOID,String.class);
    }

    public String resolveAccessToken(HttpServletRequest request){
        return request.getHeader(AUTHORIZATION);
    }

    public void validateToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getBody();
            String nickname = getUsername(token);
            userRepository.findUserByNickname(nickname).orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지 않습니다."));

        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
    }
}
