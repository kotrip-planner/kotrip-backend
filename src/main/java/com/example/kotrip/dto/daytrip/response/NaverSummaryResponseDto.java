package com.example.kotrip.dto.daytrip.response;

public class NaverSummaryResponseDto {
    private NaverDurationResponseDto summary;

    public NaverSummaryResponseDto(){
    }

    public NaverSummaryResponseDto(NaverDurationResponseDto summary) {
        this.summary = summary;
    }

    public NaverDurationResponseDto getSummary() {
        return summary;
    }
}