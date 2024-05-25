package com.example.kotrip.dto.tour.response;

import java.util.List;
import lombok.Getter;

@Getter
public class TourPageResponseDto {
    private int page;
    private List<TourInfoDto> list;

    public TourPageResponseDto(int page, List<TourInfoDto> list){
        this.page = page;
        this.list = list;
    }
}
