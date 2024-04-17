package com.example.kotrip.dto.history.response;

import com.example.kotrip.entity.schedule.ScheduleTour;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@Builder
public class HistoryScheduleResponseDto {
    private List<ScheduleTour> schedule = new ArrayList<>();
}
