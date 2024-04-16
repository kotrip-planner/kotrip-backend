package com.example.kotrip.controller.history;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.history.response.HistoryResponseDto;
import com.example.kotrip.service.history.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ApiResponse<HistoryResponseDto> getHistory(){
        return ApiResponse.ok(historyService.getHistory());
    }
}
