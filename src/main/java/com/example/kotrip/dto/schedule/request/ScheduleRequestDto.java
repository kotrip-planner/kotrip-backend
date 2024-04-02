package com.example.kotrip.dto.schedule.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {

    @NotNull(message = "areaId는 필수로 입력 해야 합니다.")
    private int areaId;

    @NotNull(message = "일정을 필수로 입력 해야 합니다.")
    private List<Day> days;
}
