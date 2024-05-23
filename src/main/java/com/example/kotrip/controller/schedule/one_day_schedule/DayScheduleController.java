package com.example.kotrip.controller.schedule.one_day_schedule;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.daytrip.request.NaverRequestDto;
import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;
import com.example.kotrip.service.schedule.one_day_schedule.OneDayScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/day")
public class DayScheduleController {
    private final OneDayScheduleService dayTripService;

    @PostMapping("")
    public ApiResponse<ScheduleResponseDto> addSchedule(@RequestBody @Valid NaverRequestDto naverRequestDto) {
        return ApiResponse.ok(dayTripService.setSchedule(naverRequestDto));
    }
}
