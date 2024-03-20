package com.example.kotrip.util.algorithm;

import lombok.Getter;

@Getter
public class Node {
    int curIdx;
    double latitude;
    double longitude;

    public Node(int curIdx, double latitude, double longitude) {
        this.curIdx = curIdx;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
