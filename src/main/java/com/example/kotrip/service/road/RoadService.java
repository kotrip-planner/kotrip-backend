package com.example.kotrip.service.road;

import com.example.kotrip.util.algorithm.Node;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Service
public class RoadService {

    private static final String option = "trafast";
    private static final String uriPath = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";
    private static final String CLIENT_ID = "ncxf9y01px";
    private static final String CLIENT_SECRET = "aWz84CcrXTVDMSZULVD9HH4Z3J2sWdge5zBYaMQw";
    private ConcurrentHashMap<String, Long> durationMap = new ConcurrentHashMap<>();

    public void getDuration(String start, String goal, int i, int j, CountDownLatch latch) {

        String key = i + "-" + j;

        WebClient client = WebClient.builder()
                .baseUrl(uriPath)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .build();

        client.mutate()
                .baseUrl(uriPath)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("start", start)
                        .queryParam("goal", goal)
                        .queryParam("option", option)
                        .build())
                .header("X-NCP-APIGW-API-KEY-ID", CLIENT_ID)
                .header("X-NCP-APIGW-API-KEY", CLIENT_SECRET)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(response);
                    long duration = jsonObject.getAsJsonObject("route").getAsJsonArray("trafast").get(0).getAsJsonObject().getAsJsonObject("summary").get("duration").getAsLong();
                    durationMap.put(key, duration);
                    latch.countDown();
                }, error -> {
                    System.err.println("Error in API call: " + error.getMessage());
                    latch.countDown();
                });
    }

    public void calculate(List<Node> nodes) {
        int N = nodes.size();
        CountDownLatch latch = new CountDownLatch(N * (N - 1));

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j) continue;
                Node startNode = nodes.get(i);
                Node goalNode = nodes.get(j);
                String start = startNode.getLatitude() + "," + startNode.getLongitude();
                String goal = goalNode.getLatitude() + "," + goalNode.getLongitude();
                getDuration(start, goal, i, j, latch);
            }
        }

        try {
            latch.await(); // 전부 요청이 완료될 때 까지 기다린다,
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        long[][] distances = new long[N][N];
        durationMap.forEach((key, value) -> {
            String[] indices = key.split("-");
            int startIdx = Integer.parseInt(indices[0]);
            int goalIdx = Integer.parseInt(indices[1]);
            distances[startIdx][goalIdx] = value;
        });

        System.out.println(Arrays.deepToString(distances));
    }
}

