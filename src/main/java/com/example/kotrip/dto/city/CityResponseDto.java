package com.example.kotrip.dto.city;

import com.example.kotrip.entity.city.City;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CityResponseDto {
    public int areaId;
    public String title;
    public String imageUrl;
    public double mapX;
    public double mapY;

    public CityResponseDto(City city) {
        areaId = city.cityId;
        title = city.title;
        imageUrl = city.imageUrl;
        mapX = city.mapX;
        mapY = city.mapY;
    }
}
