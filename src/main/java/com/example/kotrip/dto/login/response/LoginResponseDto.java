package com.example.kotrip.dto.login.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    String message;
    String accessToken;

    public static LoginResponseDto of(final String message, final String accessToken){
        return LoginResponseDto.builder()
                .message(message)
                .accessToken(accessToken)
                .build();
    }
}
