package com.example.kotrip.dto.schedule.response;

import lombok.Getter;

@Getter
public class ScheduleResponseDto {

    String message;

    public ScheduleResponseDto(String message) {
        this.message = message;
    }
}
