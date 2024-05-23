package com.example.kotrip.dto.daytrip.response;

import lombok.Getter;
import java.util.List;

@Getter
public class NaverRouteResponseDto {
    private List<NaverSummaryResponseDto> trafast;

    protected NaverRouteResponseDto() {
    }

    public NaverRouteResponseDto(List<NaverSummaryResponseDto> trafast) {
        this.trafast = trafast;
    }
}
