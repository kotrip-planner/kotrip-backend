package com.example.kotrip.dto.schedule.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SchedulesResponseDto {
    private List<SchedulesTourResponseDto> schedule;
}
