package com.example.kotrip.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String secretKey = "8af7RixU2UNn1BUVIg2AvJEQaf0D29XYA23Cmdkf9bcelSdIE";
    private static final String NICKNAME = "nickname";
    private static final String KAKAOID = "kakaoId";
    private static final String AUTHORIZATION = "Authorization";

    private static long ACCESS_TOKEN_EXPIRED_TIME = 86400000L; // 1일
    private static long REFRESH_TOKEN_EXPIRED_TIME = 2592000000L; // 30일

    private SecretKey key;

    public String createAccessToken(String nickname, String kakaoId){
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

    public String createRefreshToken(String nickname, String kakaoId) {
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
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get(NICKNAME,String.class);
    }

    public String getKakaoId(String token) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get(KAKAOID,String.class);
    }

    public String resolveAccessToken(HttpServletRequest request){
        return request.getHeader(AUTHORIZATION);
    }

    public void validateToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getBody();
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
