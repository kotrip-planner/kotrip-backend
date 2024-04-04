package com.example.kotrip.dto.schecule;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ScheduleRequestDto {
    private int areaId;
    private Plan plan;

    @Getter @Setter
    public static class Plan {
        private String time;
        private List<Integer> tourIdList;
    }
}
