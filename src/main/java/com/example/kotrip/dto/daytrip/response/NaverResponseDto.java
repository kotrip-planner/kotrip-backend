package com.example.kotrip.dto.daytrip.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NaverResponseDto {
    private int code;
    private String message;
    private NaverRouteResponseDto route;
}