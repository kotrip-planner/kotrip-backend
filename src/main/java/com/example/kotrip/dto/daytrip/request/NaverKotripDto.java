package com.example.kotrip.dto.daytrip.request;

import com.example.kotrip.dto.daytrip.Node;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class NaverKotripDto {
    private LocalDate date;
    private List<Node> nodes;

    // 생성자, getter 등 필요한 메소드 추가
    public NaverKotripDto(LocalDate date, List<Node> nodes) {
        this.date = date;
        this.nodes = nodes;
    }
}