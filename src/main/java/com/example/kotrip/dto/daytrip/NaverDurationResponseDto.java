package com.example.kotrip.dto.daytrip;

import lombok.Getter;

@Getter
public class NaverDurationResponseDto {
    private long duration;

    protected NaverDurationResponseDto() {
    }

    public NaverDurationResponseDto(long duration) {
        this.duration = duration;
    }
}
