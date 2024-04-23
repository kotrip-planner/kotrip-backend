package com.example.kotrip.controller.daySchedule;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.naver.NaverRequestDto;
import com.example.kotrip.service.daySchedule.DayScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class DayScheduleController {

    private final DayScheduleService dayScheduleService;

    public ApiResponse<String> addSchedule(@RequestBody @Valid NaverRequestDto naverRequestDto) {
        return ApiResponse.ok(dayScheduleService.setSchedule(naverRequestDto));
    }
}
