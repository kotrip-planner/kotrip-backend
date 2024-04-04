package com.example.kotrip.controller;

import com.example.kotrip.dto.hotel.HotelSearchDto;
import com.example.kotrip.service.hotel.HotelSearchService;
import lombok.RequiredArgsConstructor;
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
    public List<HotelSearchDto> hotelSearch(@RequestParam BigDecimal mapX, @RequestParam BigDecimal mapY) {
        return hotelService.dtoTrans(mapX, mapY);
    }
}
