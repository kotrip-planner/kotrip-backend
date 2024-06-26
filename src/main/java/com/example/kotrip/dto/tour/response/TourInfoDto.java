package com.example.kotrip.dto.tour.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class TourInfoDto {

    private int id;
    private String title;
    private String imageUrl;
    private String addr1;
    private Double mapX;
    private Double mapY;

    public TourInfoDto(int id, String title, String imageUrl, String addr1, Double mapX, Double mapY) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.addr1 = addr1;
        this.mapX = mapX;
        this.mapY = mapY;
    }
}
