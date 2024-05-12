package com.example.kotrip.dto.daytrip;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Node {
    private Long id;
    private String name;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private int time;
}