package com.example.kotrip.controller.tour;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.tour.response.TourInfoDto;
import com.example.kotrip.dto.tour.response.TourPageResponseDto;
import com.example.kotrip.repository.tour.TourRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TourController {
    private final TourRepository tourRepository;

    @GetMapping("/tour")
    public ApiResponse<TourPageResponseDto> tours(@RequestParam int cityId) {
        List<TourInfoDto> tourInfoDtoList = tourRepository.findTourInfoByCityId(cityId);
        TourPageResponseDto tourPageResponseDto = new TourPageResponseDto(tourInfoDtoList);
        return ApiResponse.ok(tourPageResponseDto);
    }
}
