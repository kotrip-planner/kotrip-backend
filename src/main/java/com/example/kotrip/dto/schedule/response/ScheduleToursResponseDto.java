package com.example.kotrip.dto.schedule.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.beans.Transient;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ScheduleToursResponseDto {
    private LocalDate date;
    private List<ScheduleTourResponseDto> tours;

    @JsonIgnore
    private String title;

    @JsonIgnore
    private Integer areaId;
}
