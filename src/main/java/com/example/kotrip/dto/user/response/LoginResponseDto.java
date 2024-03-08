package com.example.kotrip.dto.user.response;

import lombok.Builder;

@Builder
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;

    public LoginResponseDto of(final String accessToken) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
