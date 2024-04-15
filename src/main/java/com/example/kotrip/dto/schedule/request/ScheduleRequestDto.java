package com.example.kotrip.dto.schedule.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {

    @NotNull(message = "스케줄을 입력해야합니다.")
    private List<ScheduleDetailRequestDto> kotrip;
}
