package com.example.kotrip.dto.schedule.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {

    List<com.example.kotrip.entity.schedule.Schedule> schedules;

    @Builder
    public static ScheduleResponseDto of(List<com.example.kotrip.entity.schedule.Schedule> schedules) {
        return ScheduleResponseDto.builder()
                .schedules(schedules)
                .build();
    }
}
