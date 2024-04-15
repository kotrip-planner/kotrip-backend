package com.example.kotrip.dto.schedule.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ScheduleTourResponseDto {
    private Long id;
    private String title;
    private String imageUrl;
    private BigDecimal mapX;
    private BigDecimal mapY;
}
