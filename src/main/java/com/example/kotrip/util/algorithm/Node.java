package com.example.kotrip.util.algorithm;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Node {
    int curIdx;
    String name;
    double latitude;
    double longitude;

    public Node(int curIdx, String name, BigDecimal latitude, BigDecimal longitude) {
        this.curIdx = curIdx;
        this.name = name;
        this.latitude = latitude.doubleValue();
        this.longitude = longitude.doubleValue();
    }
}
