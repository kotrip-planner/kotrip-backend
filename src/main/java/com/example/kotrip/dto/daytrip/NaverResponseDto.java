package com.example.kotrip.dto.daytrip;

import java.util.List;

public class NaverResponseDto {
    private final int code;
    private final String message;
    private final NaverRouteResponseDto route;

    public NaverResponseDto(int code, String message, NaverRouteResponseDto route) {
        this.code = code;
        this.message = message;
        this.route = route;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public NaverRouteResponseDto getRoute() {
        return route;
    }
}