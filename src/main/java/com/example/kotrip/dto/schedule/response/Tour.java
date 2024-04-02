package com.example.kotrip.dto.schedule.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class Tour {
    private int tourId;
    private String title;
    private String imageUrl;
    private double mapX;
    private double mapY;
}
