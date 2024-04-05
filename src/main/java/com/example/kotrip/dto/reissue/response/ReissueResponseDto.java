package com.example.kotrip.dto.reissue.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReissueResponseDto {
    private String accessToken;
    private String refreshToken;

    public static ReissueResponseDto of(final String accessToken, final String refreshToken) {
        return ReissueResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
