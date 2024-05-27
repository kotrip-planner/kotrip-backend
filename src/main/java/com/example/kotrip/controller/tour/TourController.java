package com.example.kotrip.controller.tour;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.tour.response.TourInfoDto;
import com.example.kotrip.dto.tour.response.TourPageResponseDto;
import com.example.kotrip.repository.tour.TourRepository;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Tour", description = "관광지 API")
public class TourController {
    private final TourRepository tourRepository;

    @GetMapping("/tour")
    @Operation(summary = "도시 내 관광지 조회", description = "임의로 선정한 도시 내의 관광지 정보 조회하는 API")
    public ApiResponse<TourPageResponseDto> tours(@RequestParam int cityId, @RequestParam int page) {
        List<TourInfoDto> tourInfoDtoList = tourRepository.findTourInfoByCityId(cityId, PageRequest.of(page, 20));
        TourPageResponseDto tourPageResponseDto = new TourPageResponseDto(page,tourInfoDtoList);
        return ApiResponse.ok(tourPageResponseDto);
    }
}
