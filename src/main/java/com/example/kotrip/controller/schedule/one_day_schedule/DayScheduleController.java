package com.example.kotrip.controller.schedule.one_day_schedule;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.daytrip.request.NaverRequestDto;
import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;
import com.example.kotrip.service.schedule.one_day_schedule.OneDayScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/day")
@Tag(name = "DaySchedule", description = "당일치기 API")
public class DayScheduleController {
    private final OneDayScheduleService dayTripService;

    @PostMapping("")
    @Operation(summary = "당일치기 스케줄 저장", description = "Greedy 적용한 당일치기 스케줄 조회하는 API")
    public ApiResponse<ScheduleResponseDto> addSchedule(@RequestBody @Valid NaverRequestDto naverRequestDto) {
        return ApiResponse.ok(dayTripService.setSchedule(naverRequestDto));
    }
}
