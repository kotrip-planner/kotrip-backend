package com.example.kotrip.controller.daySchedule;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;
import com.example.kotrip.naver.NaverRequestDto;
import com.example.kotrip.service.daySchedule.DayScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/day")
public class DayScheduleController {

    private final DayScheduleService dayScheduleService;

    @PostMapping("")
    public ApiResponse<ScheduleResponseDto> addSchedule(@RequestBody @Valid NaverRequestDto naverRequestDto) {
        return ApiResponse.ok(dayScheduleService.setSchedule(naverRequestDto));
    }
}
