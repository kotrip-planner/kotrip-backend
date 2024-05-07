package com.example.kotrip.dto.schedule.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ScheduleEachResponseDto {
    private String title;
    private String uuid;
    private String city;
    private List<ScheduleToursResponseDto> schedule;
}
