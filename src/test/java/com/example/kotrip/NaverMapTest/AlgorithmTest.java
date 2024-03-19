package com.example.kotrip.NaverMapTest;

import com.example.kotrip.service.road.RoadService;
import com.example.kotrip.util.algorithm.Node;
import java.util.List;

public class AlgorithmTest {
    public static void main(String[] args) {

        List<Node> list = List.of(
                new Node(0,126.97691713713438,37.577536707517076),
                new Node(1,126.8980711,36.5763215),
                new Node(2,127.8980711,36.5763215),
                 new Node(3,128.8980711,37.5763215)
        );

        RoadService roadService = new RoadService();

        long time = System.currentTimeMillis();
        roadService.calculate(list);
        long finish = System.currentTimeMillis();

        System.out.println(finish - time);
    }
}
