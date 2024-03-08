package com.example.kotrip.util.algorithm;

import lombok.Getter;

@Getter
public class Node {

    int curIdx;
    Long latitude;
    Long longitude;

    public Node(int curIdx, Long latitude, Long longitude) {
        this.curIdx = curIdx;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
