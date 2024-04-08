package com.example.kotrip.dto.schedule.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SchedulesTourResponseDto {
    private LocalDate date;
    private List<ScheduleTourResponseDto> tours;
}
