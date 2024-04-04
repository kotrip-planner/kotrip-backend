package com.example.kotrip.service.road;

import com.example.kotrip.dto.schecule.ScheduleRequestDto;
import com.example.kotrip.repository.tour.TourInfoRepository;
import com.example.kotrip.util.algorithm.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final RoadService roadService;
    private final TourInfoRepository tourInfoRepository;

    public void schedule(ScheduleRequestDto requestDto) {
        // 0.
        // data.areaId 지정

        // 1.
        // schedules.time 지정

        // 2.
        // requestDto 각 관광지 id로, TourInfoRepository에서 관광지를 노드화 한다.
        List<Node> nodeList = new ArrayList<>();

        List<Integer> tourIdList = requestDto.getPlan().getTourIdList();
        for (Integer tourId : tourIdList) {
            Node node = tourInfoRepository.findNodeByInfoId(tourId);
            nodeList.add(node);
        }

        // 3.
        // TSP 알고리즘을 실행하기 위한 w[i][j] = NaverMap(i, j) 간선 그리기
        String[] places = nodeList.stream().map(Node::getName).toArray(String[]::new);
        long cost[][] = roadService.calculate(nodeList); // 각 관광지까지의 w[][] 간선

        // 4.
        // TSP 알고리즘 실행
        TspService tspService = new TspService(nodeList.size(), cost, places);

        int start = 0;
        int bit = 1;
        long minCost = tspService.printCost(start, bit); // 외판원 최소 비용
        ArrayList<String> paths = tspService.printPath(start, bit); //외판원 최적 경로

        // 5.
        // Tour 데이터 만들기
        // (1 → 3 → 4 → 2) 경로가 그려질 것
        // 차례대로 tours[tour 기본 정보, duration] 배열에 저장




        // 6. homeResponseDto 반환
        // return homeResponseDto;
    }
}
