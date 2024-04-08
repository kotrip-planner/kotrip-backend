package com.example.kotrip.dto.schedule.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ScheduleDetailRequestDto {

    @NotNull(message = "관광지 id는 필수로 입려해야 합니다.")
    private int tourId;

    @NotNull(message = "위도는 필수로 입려해야 합니다.")
    private Double latitude;

    @NotNull(message = "경도는 필수로 입려해야 합니다.")
    private Double longitude;

    @NotNull(message = "일차는 필수로 입려해야 합니다.")
    private int day;

    @NotNull(message = "날짜는 필수로 입려해야 합니다.")
    private LocalDateTime date;
}
