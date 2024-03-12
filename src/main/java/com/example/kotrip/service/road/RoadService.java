package com.example.kotrip.service.road;

import com.example.kotrip.util.algorithm.Node;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLException;
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

    //"126.844856,37.5407361" 이런 형식으로 입력해야함
    public static Long getDuration(String start, String goal) { // 두 지점의 최소 시간 구하는 로여
        HttpClient httpClient = HttpClient.create().secure(t -> {
            try {
                t.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build());
            }catch (SSLException e){
                System.out.println(e);
            }
        });

        WebClient client = WebClient.builder()
                .baseUrl(uriPath)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        String response = client.get().uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("start", start)
                        .queryParam("goal", goal)
                        .queryParam("option",option)
                        .build())
                .header("X-NCP-APIGW-API-KEY-ID",CLIENT_ID)
                .header("X-NCP-APIGW-API-KEY",CLIENT_SECRET)
                .retrieve() 			  // 응답을 받게 하되,
                .bodyToMono(String.class) // 응답 값을 하나만,
                .block(); 				  // 동기로 받으려 한다.

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(response);
        long duration = jsonObject.getAsJsonObject("route").getAsJsonArray("trafast").get(0).getAsJsonObject().getAsJsonObject("summary").get("duration").getAsLong();
        return duration;
    }

    public void calculate(List<Node> nodes){ // 위치 지점을 받아온다.

        // 1.노드별 시간을 구하고, 그 시간을 2차원 배열에 담아준다.
        // 2.노드별 curIdx로 구분을 해준다.

        int N = nodes.size();
        long[][] w = new long[N][N];
        int[][] dp = new int[N][N];

        //   0 1 2 3 4
        // 0
        // 1
        // 2
        // 3
        // 4

        long beforeTime = System.currentTimeMillis();
        for (int i = 0; i < nodes.size(); i++) {
            Node startNode = nodes.get(i);
            double startLatitude = startNode.getLatitude();
            double startLongitude = startNode.getLongitude();

            String startCoordinates = startLatitude +","+ startLongitude;

            for (int j = 0; j < nodes.size(); j++) {
                if(i == j) { // 자기 자신의 노드인 경우 제외
                    continue;
                }
                Node goalNode = nodes.get(j);
                double goalLatitude = goalNode.getLatitude();
                double goalLongitude = goalNode.getLongitude();
                String goalCoordinates = goalLatitude +","+ goalLongitude;
                long duration = getDuration(startCoordinates, goalCoordinates);

                // 두 노드 사이의 거리 구하기
                w[startNode.getCurIdx()][goalNode.getCurIdx()] = duration;
            }
        }
        long afterTime = System.currentTimeMillis();

        System.out.println(afterTime - beforeTime);

        System.out.println(Arrays.deepToString(w));
    }
}
