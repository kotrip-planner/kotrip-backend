package com.example.kotrip.dto.daytrip;

public class NaverDurationResponseDto {
    private final long duration;

    public NaverDurationResponseDto(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }
}
