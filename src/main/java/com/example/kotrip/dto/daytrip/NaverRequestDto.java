package com.example.kotrip.dto.daytrip;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class NaverRequestDto {
    @JsonProperty("title")
    private String title;

    @JsonProperty("kotrip")
    private List<NaverKotripDto> kotrip;

    @JsonProperty("areaId")
    private int areaId;

    // 생성자, getter 등 필요한 메소드 추가
    public NaverRequestDto(String title, List<NaverKotripDto> kotrip, int areaId) {
        this.title = title;
        this.kotrip = kotrip;
        this.areaId = areaId;
    }
}