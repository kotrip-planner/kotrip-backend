package com.example.kotrip.controller.schedule.multi_day_schedule;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.daytrip.request.NaverRequestDto;
import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;
import com.example.kotrip.dto.schedule.response.SchedulesResponseDto;

import com.example.kotrip.service.schedule.multi_day_schedule.OtherDayGreedyScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class MultiDayScheduleController {

    private final OtherDayGreedyScheduleService otherDayGreedyScheduleService;

    @PostMapping("")
    public ApiResponse<ScheduleResponseDto> addSchedule(
           @RequestBody @Valid NaverRequestDto naverRequestDto
           ) {
       return ApiResponse.ok(otherDayGreedyScheduleService.setSchedule(naverRequestDto));
    }

    @GetMapping("")
    public ApiResponse<SchedulesResponseDto> getSchedules() {
       return ApiResponse.ok(otherDayGreedyScheduleService.getSchedule());
    }
}