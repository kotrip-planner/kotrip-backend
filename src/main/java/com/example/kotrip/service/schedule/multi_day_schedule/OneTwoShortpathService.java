package com.example.kotrip.service.schedule.multi_day_schedule;

import com.example.kotrip.dto.daytrip.NewNode;
import com.example.kotrip.dto.daytrip.Node;
import com.example.kotrip.dto.daytrip.request.NaverRequestDto;
import com.example.kotrip.dto.daytrip.response.NaverResponseDto;
import io.netty.channel.ChannelOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
public class OneTwoShortpathService {

    // 네이버 지도 API, 발급받은 id와 password
    private static final String uriPath = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";
    @Value("${API-KEY.clientId}")
    private String CLIENT_ID;

    @Value("${API-KEY.clientSecret}")
    private String CLIENT_SECRET;


    public String getShortpath(List<Node> oneNodes, List<Node> twoNodes) {
        // 연결 및 응답 타임아웃 설정
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofMillis(10000));

        // 네이버 지도 Api 응답 타입을 JSON으로 받기를 원함
        WebClient webClient = WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(uriPath)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        List<NewNode> newNodes = nodeSetGoal(oneNodes, twoNodes);

        Flux<NewNode> nodesFlux = Flux.fromIterable(newNodes);
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

        String minOneNodeToTwoNode = "init";
        Long minDistance = Long.MAX_VALUE;
        for (Map<String, Long> distanceMatrix : nodeDistanceMatrix) {
            for (Map.Entry<String, Long> entry : distanceMatrix.entrySet()) {
                Long distance = entry.getValue();

                if (minDistance > distance) {
                    minDistance = distance;
                    minOneNodeToTwoNode = entry.getKey();
                }
            }
        }

        return minOneNodeToTwoNode;

    }


    // 네이버 API를 호출할 때, 출발지-목적지가 같게 호출이 되면 응답을 받지 못한다.
    // 따라서 출발지-목적지를 설정한 NewNode 리스트를 만든다.
    private List<NewNode> nodeSetGoal(List<Node> oneNodes, List<Node> twoNodes) {

        List<NewNode> newAllNodes = new ArrayList<>();

        for (int i = 0; i < oneNodes.size(); ++i) {
            Node start = oneNodes.get(i);
            for (int j = 0; j < twoNodes.size(); ++j) {
                if (i != j) {
                    Node end = twoNodes.get(j);

                    NewNode newNode = new NewNode(start.getId(), start.getName(), start.getLongitude(), start.getLatitude(),
                            end.getId(), end.getName(), end.getLongitude(), end.getLatitude());

                    newAllNodes.add(newNode);
                }
            }
        }

        return newAllNodes;
    }

}
