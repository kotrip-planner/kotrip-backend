package com.example.kotrip.dto.history.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HistoryEachResponseDto {
    private String uuid;
    private String title;
    private String city;
    private String imageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdTime;
}
