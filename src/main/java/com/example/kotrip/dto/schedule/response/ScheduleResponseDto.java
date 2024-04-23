package com.example.kotrip.dto.schedule.response;

import lombok.Getter;

@Getter
public class ScheduleResponseDto {

    String uuid;

    public ScheduleResponseDto(final String uuid) {
        this.uuid = uuid;
    }
}
