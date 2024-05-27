package com.example.kotrip.controller.history;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.history.response.HistoryResponseDto;
import com.example.kotrip.dto.schedule.request.ScheduleUuidDto;
import com.example.kotrip.dto.schedule.response.ScheduleEachResponseDto;
import com.example.kotrip.service.history.HistoryService;
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
@RequiredArgsConstructor
@RequestMapping("/api/history")
@Tag(name = "History", description = "히스토리 API")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    @Operation(summary = "스케줄 조회", description = "유저가 저장한 스케줄 리스트를 조회하는 API")
    public ApiResponse<HistoryResponseDto> getHistory(){
        return ApiResponse.ok(historyService.getHistory());
    }

    @PostMapping
    @Operation(summary = "스케줄 세부내용 조회", description = "스케줄 내의 관광지, 추천 경로, 숙소와 같은 세부 내용을 조회하는 API")
    public ApiResponse<ScheduleEachResponseDto> getDetailSchedule(@Valid @RequestBody ScheduleUuidDto scheduleUuidDto){
        return ApiResponse.ok(historyService.getSchedule(scheduleUuidDto));
    }
}
