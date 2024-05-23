package com.example.kotrip.service.schedule.one_day_schedule;

import com.example.kotrip.dto.daytrip.response.NaverResponseDto;
import com.example.kotrip.dto.daytrip.Node;
import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
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
                .baseUrl(uriPath)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();



        // Mono: 0~1개의 아이템을 비동기적으로 처리할 수 있는 Reactor 데이터 스트림
        // Flux: 0~N개의 아이템을 비동기적으로 처리할 수 있는 Reactor 데이터 스트림
        Flux<Node> nodesFlux = Flux.fromIterable(allNodes).filter(node -> !visited.get(node.getId()));

        List<Map<Long, Long>> nearDuration = nodesFlux.flatMap(node ->
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
                            return Map.of(node.getId(), response.getRoute().getTrafast().get(0).getSummary().getDuration());
                        })
        ).collectList().block();


        Long nearId = 0L;
        Double shortTime = Double.MAX_VALUE;
        for (Map<Long, Long> near : nearDuration) {
            for (Long key : near.keySet()) {
                double time = near.get(key);

                if (shortTime > time) {
                    shortTime = time;
                    nearId = key;
                }
            }
        }

        for (Node oneNode : allNodes) {
            if (oneNode.getId() == nearId) {
                return oneNode;
            }
        }

        return null;
    }
}
