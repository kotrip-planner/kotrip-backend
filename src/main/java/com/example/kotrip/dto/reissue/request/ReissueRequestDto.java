package com.example.kotrip.dto.reissue.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReissueRequestDto {
    @NotBlank(message = "refreshToken 값은 필수 입니다.")
    private String refreshToken;
}
