package com.example.kotrip.controller.schedule;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;
import com.example.kotrip.dto.schedule.response.SchedulesResponseDto;
import com.example.kotrip.naver.NaverRequestDto;
import com.example.kotrip.service.schedule.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

   @PostMapping("")
   public ApiResponse<ScheduleResponseDto> addSchedule(
           @RequestBody @Valid NaverRequestDto naverRequestDto
           ) throws InterruptedException {
       return ApiResponse.ok(scheduleService.setSchedule(naverRequestDto));
   }

   @GetMapping("")
   public ApiResponse<SchedulesResponseDto> getSchedules() {
       return ApiResponse.ok(scheduleService.getSchedule());
   }
}