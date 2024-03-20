package com.example.kotrip.dto.login.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private String message;
    private String accessToken;
    private String refreshToken;

    public static LoginResponseDto of(final String message, final String accessToken, final String refreshToken){
        return LoginResponseDto.builder()
                .message(message)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
