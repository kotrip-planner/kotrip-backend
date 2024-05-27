package com.example.kotrip.controller.schedule.multi_day_schedule;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.daytrip.request.NaverRequestDto;
import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;
import com.example.kotrip.dto.schedule.response.SchedulesResponseDto;

import com.example.kotrip.service.schedule.multi_day_schedule.OtherDayGreedyScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
@Tag(name = "MultiDaySchedule", description = "n박 m일 API")
public class MultiDayScheduleController {

    private final OtherDayGreedyScheduleService otherDayGreedyScheduleService;

    @PostMapping("")
    @Operation(summary = "n박 m일 스케줄 저장(tsp+greedy)", description = "1일차 Tsp, 나머지 일차 Greedy 적용한 스케줄 조회하는 API")
    public ApiResponse<ScheduleResponseDto> addSchedule(
           @RequestBody @Valid NaverRequestDto naverRequestDto
           ) {
       return ApiResponse.ok(otherDayGreedyScheduleService.setSchedule(naverRequestDto));
    }

    @GetMapping("")
    @Operation(summary = "n박 m일 스케줄 조회(tsp+greedy)", description = "1일차 Tsp, 나머지 일차 Greedy 적용한 스케줄 조회하는 API")
    public ApiResponse<SchedulesResponseDto> getSchedules() {
       return ApiResponse.ok(otherDayGreedyScheduleService.getSchedule());
    }
}