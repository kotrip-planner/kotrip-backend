package com.example.kotrip.dto.daytrip;

public class NaverSummaryResponseDto {
    private final NaverDurationResponseDto summary;

    public NaverSummaryResponseDto(NaverDurationResponseDto summary) {
        this.summary = summary;
    }

    public NaverDurationResponseDto getSummary() {
        return summary;
    }
}