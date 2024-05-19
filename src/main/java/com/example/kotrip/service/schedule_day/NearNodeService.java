package com.example.kotrip.service.schedule_day;

import com.example.kotrip.dto.daytrip.NaverResponseDto;
import com.example.kotrip.dto.daytrip.Node;
import io.netty.channel.ChannelOption;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class NearNodeService {

    private static final String uriPath = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";
    private static final String CLIENT_ID = "ncxf9y01px";
    private static final String CLIENT_SECRET = "aWz84CcrXTVDMSZULVD9HH4Z3J2sWdge5zBYaMQw";

    // visited[True]인 곳은 api 거리 탐색 X
    public Node getNearNode(Node start, List<Node> allNodes, Map<Long, Boolean> visited) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofMillis(10000));

        WebClient webClient = WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        // 멀티쓰레드 환경에서 안전하게 사용하기 위함
        AtomicReference<Node> nearestNode = new AtomicReference<>(start);
        AtomicReference<Double> shortestDistance = new AtomicReference<>(Double.MAX_VALUE);

        // Mono: 0~1개의 아이템을 비동기적으로 처리할 수 있는 Reactor 데이터 스트림
        // Flux: 0~N개의 아이템을 비동기적으로 처리할 수 있는 Reactor 데이터 스트림
        Flux<Node> nodesFlux = Flux.fromIterable(allNodes)
                .filter(node -> !visited.getOrDefault(node.getId(), false));

        return nodesFlux.flatMap(node ->
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .queryParam("start", start.getLongitude() + "," + start.getLatitude())
                                .queryParam("goal", node.getLongitude() + "," + node.getLatitude())
                                .queryParam("option", "trafast") // trafast API 요청
                                .build())
                        .header("X-NCP-APIGW-API-KEY-ID", CLIENT_ID)
                        .header("X-NCP-APIGW-API-KEY", CLIENT_SECRET)
                        .retrieve()
                        .bodyToMono(NaverResponseDto.class)
                        .map(response -> {
                            // 여기서 "start 노드와 주변 노드 간의 거리 구하는 로직"을 구현
                            double duration = parseDistance(response);
                            if (duration < shortestDistance.get()) {
                                shortestDistance.set(duration);
                                nearestNode.set(node);
                            }
                            return node;
                        })
        ).then(Mono.just(nearestNode.get())).block();
    }

    private double parseDistance(NaverResponseDto response) {
        if (response != null &&
                response.getRoute() != null &&
                response.getRoute().getTrafast() != null &&
                !response.getRoute().getTrafast().isEmpty() &&
                response.getRoute().getTrafast().get(0).getSummary() != null) {

            return response.getRoute().getTrafast().get(0).getSummary().getDuration(); // 거리 정보를 반환함
        }
        return Double.MAX_VALUE; // 거리 정보를 찾을 수 없는 경우, 최대값을 반환하여 이 노드를 선택하지 않도록 함
    }


}
