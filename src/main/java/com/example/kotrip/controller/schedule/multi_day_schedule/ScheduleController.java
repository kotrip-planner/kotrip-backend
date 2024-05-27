package com.example.kotrip.controller.schedule.multi_day_schedule;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;
import com.example.kotrip.dto.schedule.response.SchedulesResponseDto;
import com.example.kotrip.naver.NaverRequestDto;
import com.example.kotrip.service.schedule.multi_day_schedule.AllDayTspScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Tag(name = "MultiDaySchedule", description = "n박 m일 API")
public class ScheduleController {

    private final AllDayTspScheduleService allDayTspScheduleService;

   @PostMapping("")
   @Operation(summary = "n박 m일 스케줄 저장(tsp)", description = "1일차 Tsp, 나머지 일차 Greedy 적용한 스케줄 조회하는 API")
   public ApiResponse<ScheduleResponseDto> addSchedule(
           @RequestBody @Valid NaverRequestDto naverRequestDto
           ) {
       return ApiResponse.ok(allDayTspScheduleService.setSchedule(naverRequestDto));
   }

   @GetMapping("")
   @Operation(summary = "n박 m일 스케줄 조회(tsp)", description = "모든 일차 Tsp 적용한 스케줄 조회하는 API")
   public ApiResponse<SchedulesResponseDto> getSchedules() {
       return ApiResponse.ok(allDayTspScheduleService.getSchedule());
   }
}