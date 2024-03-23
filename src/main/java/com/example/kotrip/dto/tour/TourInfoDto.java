package com.example.kotrip.dto.tour;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class TourInfoDto {
    private String title;
    private String imageUrl;
    private String addr1;
    private BigDecimal mapX;
    private BigDecimal mapY;

    public TourInfoDto(String title, String imageUrl, String addr1, BigDecimal mapX, BigDecimal mapY) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.addr1 = addr1;
        this.mapX = mapX;
        this.mapY = mapY;
    }
}
