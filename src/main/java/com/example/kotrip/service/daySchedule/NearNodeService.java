package com.example.kotrip.service.daySchedule;

import com.example.kotrip.dto.daytrip.Node;

import java.util.Map;

public class NearNodeService {

    private static final String uriPath = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";
    private static final String CLIENT_ID = "ncxf9y01px";
    private static final String CLIENT_SECRET = "aWz84CcrXTVDMSZULVD9HH4Z3J2sWdge5zBYaMQw";

    // visited[True]인 곳은 api 거리 탐색 X
    public Node getNearNode(Node start, Map<Long, Boolean> visited) {
        return start;
    }
}
