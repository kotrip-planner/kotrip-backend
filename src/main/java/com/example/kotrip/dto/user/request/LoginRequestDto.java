package com.example.kotrip.dto.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @NotNull(message = "kakao에서 전달받은 코드를 입력해주세요.")
    private String code;
}
