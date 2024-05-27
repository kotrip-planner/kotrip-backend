package com.example.kotrip.controller.city;

import com.example.kotrip.dto.city.CityResponseDto;
import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.repository.city.CityRepository;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "City", description = "도시 API")
public class CityController {
    private final CityRepository cityRepository;

    @GetMapping("/city")
    @Operation(summary = "도시 조회", description = "전국 26개의 대표 도시 정보를 조회하는 API")
    public ApiResponse<List<CityResponseDto>> cityList() {
        List<CityResponseDto> cities = cityRepository.findDtoAll();
        return new ApiResponse<>(HttpStatus.OK, "Success", cities);
    }
}
