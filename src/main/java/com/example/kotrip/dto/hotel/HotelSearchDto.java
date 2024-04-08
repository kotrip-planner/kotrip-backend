package com.example.kotrip.dto.hotel;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HotelSearchDto {
    public String addr1;
    public String addr2;
    public String imageUrl1;
    public String imageUrl2;

    public BigDecimal mapX;

    public BigDecimal mapY;
    public String tel;

    public String title;

    public Long distance;

    public HotelSearchDto(HotelSearchInterface hotelInfoInterface) {
        this.addr1 = hotelInfoInterface.getAddr1();
        this.addr2 = hotelInfoInterface.getAddr2();
        this.imageUrl1 = hotelInfoInterface.getImage_url1();
        this.imageUrl2 = hotelInfoInterface.getImage_url2();
        this.mapX = hotelInfoInterface.getMap_x();
        this.mapY = hotelInfoInterface.getMap_y();
        this.tel = hotelInfoInterface.getTel();
        this.title = hotelInfoInterface.getTitle();
        this.distance = hotelInfoInterface.getDistance();
    }
}
