package com.example.kotrip.controller.tour;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.tour.TourInfoDto;
import com.example.kotrip.repository.tour.TourRepository;
<<<<<<< HEAD:src/main/java/com/example/kotrip/controller/TourController.java
import java.util.List;
=======
>>>>>>> feature/schedule:src/main/java/com/example/kotrip/controller/tour/TourController.java
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TourController {
    private final TourRepository tourRepository;

    @GetMapping("/tour")
    public ApiResponse<List<TourInfoDto>> tours(@RequestParam int cityId) {
        List<TourInfoDto> tourInfoDtoList = tourRepository.findTourInfoByCityId(cityId);
        return ApiResponse.ok(tourInfoDtoList);
    }
}
