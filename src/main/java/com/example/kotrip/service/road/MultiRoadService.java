package com.example.kotrip.service.road;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiRoadService {
    // 지점 쌍의 거리를 저장할 ConcurrentHashMap
    private static ConcurrentHashMap<String, String> distanceMap = new ConcurrentHashMap<>();

    private static final String option = "trafast";
    private static final String uriPath = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";
    private static final String CLIENT_ID = "ncxf9y01px";
    private static final String CLIENT_SECRET = "aWz84CcrXTVDMSZULVD9HH4Z3J2sWdge5zBYaMQw";

    private static void calculateDistance(String startPoint, String endPoint) {
        // API URL 구성 예시. 실제 URL과 파라미터는 API 문서에 따라 다를 수 있습니다.
        String url = String.format(uriPath);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Naver-Client-Id", "your_client_id")
                .header("X-Naver-Client-Secret", "your_client_secret")
                .build();

        try {

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            // 결과를 ConcurrentHashMap에 저장
            distanceMap.put(startPoint + " to " + endPoint, response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10); // 스레드 풀 설정

        String[] locations = {"지점1", "지점2", "지점3", "지점4", "지점5", "지점6", "지점7", "지점8", "지점9", "지점10"};

        for (int i = 0; i < locations.length; i++) {
            for (int j = i + 1; j < locations.length; j++) {
                String startPoint = locations[i];
                String endPoint = locations[j];
                executor.submit(() -> calculateDistance(startPoint, endPoint));
            }
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // 모든 작업이 완료될 때까지 대기
        }

        // 결과 출력
        distanceMap.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
