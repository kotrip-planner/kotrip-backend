package com.example.kotrip.dto.login.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @NotBlank(message = "로그인시, 카카오 인증 코드는 필수로 입력해주어야 합니다.")
    String code;
}
