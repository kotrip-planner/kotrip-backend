package com.example.kotrip.service.schedule.multi_day_schedule;

import com.example.kotrip.dto.daytrip.NewNode;
import com.example.kotrip.dto.daytrip.request.NaverRequestDto;
import com.example.kotrip.dto.daytrip.response.NaverResponseDto;
import com.example.kotrip.dto.daytrip.Node;
import com.example.kotrip.service.road.TspService;
import io.netty.channel.ChannelOption;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OneDayTspScheduleService {
    private static final String uriPath = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";
    private static final String CLIENT_ID = "5x4xophd9r";
    private static final String CLIENT_SECRET = "iYRQwGRIZEmc8uAkxwkCTdrbUsOAZpTo5nNVik3g";

    // visited[True]인 곳은 api 거리 탐색 X
    public ArrayList<Long> getDriving(NaverRequestDto naverRequestDto) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofMillis(10000));

        WebClient webClient = WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(uriPath)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();


        // 1일차 노드들
        List<Node> allNodes = naverRequestDto.getKotrip().get(0).getNodes();


        // 관광지 id 리스트, DP 테이블 구하기 위함
        List<Long> allNodeIndex = new ArrayList<>();
        for (Node node : allNodes) {
            allNodeIndex.add(node.getId());
        }

        List<NewNode> newAllNodes = nodeSetGoal(allNodes);


        // Mono: 0~1개의 아이템을 비동기적으로 처리할 수 있는 Reactor 데이터 스트림
        // Flux: 0~N개의 아이템을 비동기적으로 처리할 수 있는 Reactor 데이터 스트림
        Flux<NewNode> nodesFlux = Flux.fromIterable(newAllNodes);

        List<Map<String, Long>> nodeDistanceMatrix = nodesFlux.flatMap(node ->
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .queryParam("start", node.getStartLatitude() + "," + node.getStartLongitude())
                                .queryParam("goal", node.getDestLatitude() + "," + node.getDestLongitude())
                                .queryParam("option", "trafast") // trafast API 요청
                                .build())
                        .header("X-NCP-APIGW-API-KEY-ID", CLIENT_ID)
                        .header("X-NCP-APIGW-API-KEY", CLIENT_SECRET)
                        .retrieve()
                        .bodyToMono(NaverResponseDto.class)
                        .map(response -> Map.of(node.getStartId() + "-" + node.getDestId(), response.getRoute().getTrafast().get(0).getSummary().getDuration()))
        ).collectList().block();



        Long[][] dpMatrix = new Long[allNodes.size()][allNodes.size()];
        for (Map<String, Long> nodeDistance : nodeDistanceMatrix) {
            for (Map.Entry<String, Long> entry : nodeDistance.entrySet()) {
                String key = entry.getKey();
                Long duration = entry.getValue();

                String[] parts = key.split("-");
                Long from = Long.parseLong(parts[0]); // 관광지 시작 id
                int fromIdx = allNodeIndex.indexOf(from);// 관광지 시작 id에 대한 index

                Long to = Long.parseLong(parts[1]); // 관광지 도착 id
                int toIdx = allNodeIndex.indexOf(to);// 관광지 도착 id에 대한 index

                dpMatrix[fromIdx][toIdx] = duration;
            }
        }

        Long[] nodes = allNodes.stream() // TSP 로직을 위한 node ID 배열 반환
                .map(node -> node.getId())
                .toArray(Long[]::new);

        TspService tspService = new TspService(dpMatrix.length, dpMatrix, nodes);
        ArrayList<Long> oneDayPaths = tspService.printPath(0, 1);

        if (!oneDayPaths.isEmpty()) {
            oneDayPaths.remove(oneDayPaths.size() - 1);
        }


        return oneDayPaths;
    }


    // 네이버 API를 호출할 때, 출발지-목적지가 같게 호출하지 않게끔 하기 위함.
    private List<NewNode> nodeSetGoal(List<Node> allNodes) {

        List<NewNode> newAllNodes = new ArrayList<>();

        for (int i = 0; i < allNodes.size(); ++i) {
            Node start = allNodes.get(i);
            for (int j = 0; j < allNodes.size(); ++j) {
                if (i != j) {
                    Node end = allNodes.get(j);

                    NewNode newNode = new NewNode(start.getId(), start.getName(), start.getLongitude(), start.getLatitude(),
                            end.getId(), end.getName(), end.getLongitude(), end.getLatitude());

                    newAllNodes.add(newNode);
                }
            }
        }

        return newAllNodes;
    }

}
