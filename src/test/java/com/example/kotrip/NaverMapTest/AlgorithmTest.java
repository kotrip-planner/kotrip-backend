package com.example.kotrip.NaverMapTest;

import com.example.kotrip.service.road.RoadService;
import com.example.kotrip.service.road.TspService;
import com.example.kotrip.util.algorithm.Node;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;



public class AlgorithmTest {
    @Test
    void tspTest() {
        long time = System.currentTimeMillis();

        // given
        List<Node> list = List.of(
                new Node(0, "동대문", 126.87691713713438, 37.577536707517076),
                new Node(1, "서대문", 126.9780711, 36.909783215),
                new Node(2, "남대문", 127.8830711, 36.6163215),
                new Node(3, "북대문", 128.9180711, 37.1963215)
        );


        // when
        String[] places = list.stream().map(Node::getName).toArray(String[]::new);

        RoadService roadService = new RoadService();
        long[][] graph = roadService.calculate(list); // 각 관광지까지의 w[][] 간선
        TspService tspService = new TspService(list.size(), graph, places);

        int start = 0;
        int bit = 1;
        long minCost = tspService.printCost(start, bit); // 외판원 최소 비용
        ArrayList<String> paths = tspService.printPath(start, bit); //외판원 최적 경로


        //then
        System.out.println("외판원 최소 비용: " + minCost);
        System.out.println("외판원 최적 경로: " + paths);

        long finish = System.currentTimeMillis();
        System.out.println(finish - time);
    }
}
