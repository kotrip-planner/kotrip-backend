package com.example.kotrip.controller.hotel;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.hotel.HotelSearch;
import com.example.kotrip.dto.hotel.HotelSearchResult;
import com.example.kotrip.service.hotel.HotelSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Hotel", description = "숙소 API")
public class HotelController {
    private final HotelSearchService hotelService;

    @GetMapping("/hotelSearch")
    @Operation(summary = "숙소 조회", description = "출발지와 도착지를 지름으로 하는 원 반경 내의 숙소를 조회하는 API")
    public ApiResponse<HotelSearchResult> hotelSearch(@RequestParam BigDecimal mapAX, @RequestParam BigDecimal mapAY, @RequestParam BigDecimal mapBX, @RequestParam BigDecimal mapBY) {
        Double mapaX = mapAX.doubleValue();
        Double mapaY = mapAY.doubleValue();
        Double mapbX = mapBX.doubleValue();
        Double mapbY = mapBY.doubleValue();

        HotelSearchResult hotelSearchResult = hotelService.dtoTrans(mapaX, mapaY, mapbX, mapbY);
        return new ApiResponse<>(HttpStatus.OK, "Success", hotelSearchResult);
    }
}
