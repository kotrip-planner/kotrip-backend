package com.example.kotrip.dto.schecule;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class ScheduleResponseDto {
    private int cityId;
    private String time;

    private List<Recommend> recommend;

    @Getter
    static class Recommend {
        private int tourId;
        private String title;
        private Double duration;
        private String imageUrl;
        private BigDecimal mapX;
        private BigDecimal mapY;

        public Recommend(int tourId, String title, Double duration, String imageUrl, BigDecimal mapX, BigDecimal mapY) {
            this.tourId = tourId;
            this.title = title;
            this.duration = duration;
            this.imageUrl = imageUrl;
            this.mapX = mapX.setScale(10);
            this.mapY = mapY.setScale(10);
        }
    }

    public ScheduleResponseDto(int cityId, String time, List<Recommend> recommend) {
        this.cityId = cityId;
        this.time = time;
        this.recommend = recommend;
    }
}
