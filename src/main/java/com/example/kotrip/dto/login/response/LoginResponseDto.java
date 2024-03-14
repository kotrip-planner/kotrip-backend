package com.example.kotrip.dto.login.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    String message;

    public static LoginResponseDto of(final String message){
        return LoginResponseDto.builder()
                .message(message)
                .build();
    }
}
