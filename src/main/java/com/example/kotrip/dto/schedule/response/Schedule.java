package com.example.kotrip.dto.schedule.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Schedule {
    private LocalDateTime time;
    List<Tour> tours;
}
