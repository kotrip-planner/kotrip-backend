package com.example.kotrip.controller.hotel;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.hotel.HotelSearchDto;
import com.example.kotrip.service.hotel.HotelSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HotelController {
    private final HotelSearchService hotelService;

    @GetMapping("/hotelSearch")
    public ApiResponse<List<HotelSearchDto>> hotelSearch(@RequestParam BigDecimal mapAX, @RequestParam BigDecimal mapAY, @RequestParam BigDecimal mapBX, @RequestParam BigDecimal mapBY) {
        Double mapaX = mapAX.doubleValue();
        Double mapaY = mapAY.doubleValue();
        Double mapbX = mapBX.doubleValue();
        Double mapbY = mapBY.doubleValue();

        List<HotelSearchDto> hotelSearchDtoList = hotelService.dtoTrans(mapaX, mapaY, mapbX, mapbY);
        return new ApiResponse<>(HttpStatus.OK, "Success", hotelSearchDtoList);
    }
}
