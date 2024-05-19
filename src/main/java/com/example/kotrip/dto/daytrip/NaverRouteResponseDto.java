package com.example.kotrip.dto.daytrip;

import java.util.List;

public class NaverRouteResponseDto {
    private final List<NaverSummaryResponseDto> trafast;

    public NaverRouteResponseDto(List<NaverSummaryResponseDto> trafast) {
        this.trafast = trafast;
    }

    public List<NaverSummaryResponseDto> getTrafast() {
        return trafast;
    }
}



