package com.example.kotrip.dto.history.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HistoryEachResponseDto {
    private String uuid;
    private String title;
    private String imageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
}
