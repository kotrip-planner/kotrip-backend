package com.example.kotrip.dto.daytrip.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NaverRequestDto {
    private String title;
    private List<NaverKotripDto> kotrip;
    private int areaId;
}