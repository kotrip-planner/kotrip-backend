package com.example.kotrip.dto.tour;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TourInfoDto {

    private int id;
    private String title;
    private String imageUrl;
    private String addr1;
    private BigDecimal mapX;
    private BigDecimal mapY;

    public TourInfoDto(int id, String title, String imageUrl, String addr1, BigDecimal mapX, BigDecimal mapY) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.addr1 = addr1;
        this.mapX = mapX;
        this.mapY = mapY;
    }
}
