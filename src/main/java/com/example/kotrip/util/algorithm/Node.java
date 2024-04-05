package com.example.kotrip.util.algorithm;

import lombok.Getter;

@Getter
public class Node {
    int curIdx;
    String name;
    double latitude;
    double longitude;

    public Node(int curIdx, String name, double latitude, double longitude) {
        this.curIdx = curIdx;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
