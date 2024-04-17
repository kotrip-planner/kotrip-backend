package com.example.kotrip.dto.history.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HistoryResponseDto {
    List<HistoryEachResponseDto> history = new ArrayList<>();
}
