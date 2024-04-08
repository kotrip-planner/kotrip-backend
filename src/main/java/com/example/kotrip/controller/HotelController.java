package com.example.kotrip.controller;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.hotel.HotelSearchDto;
import com.example.kotrip.dto.tour.TourInfoDto;
import com.example.kotrip.service.hotel.HotelSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HotelController {
    private final HotelSearchService hotelService;

    @GetMapping("/hotelSearch")
    public ApiResponse<List<HotelSearchDto>> hotelSearch(@RequestParam BigDecimal mapX, @RequestParam BigDecimal mapY) {
        List<HotelSearchDto> hotelSearchDtoList = hotelService.dtoTrans(mapX, mapY);
        return new ApiResponse<>(HttpStatus.OK, "Success", hotelSearchDtoList);
    }
}
