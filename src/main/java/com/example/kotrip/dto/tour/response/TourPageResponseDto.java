package com.example.kotrip.dto.tour.response;

import java.util.List;
import lombok.Getter;

@Getter
public class TourPageResponseDto {
    private List<TourInfoDto> list;

    public TourPageResponseDto(List<TourInfoDto> list){
        this.list = list;
    }
}
