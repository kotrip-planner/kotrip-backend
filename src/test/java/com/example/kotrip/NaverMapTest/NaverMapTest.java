package com.example.kotrip.NaverMapTest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import javax.net.ssl.SSLException;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

public class NaverMapTest {
    public static void main(String[] args) {
        String uriPath = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";

        // 시작지
        String start = "126.97691713713438,37.577536707517076";

        // 목적지
        String goal = "126.8980711,36.5763215";

        // 실시간 빠른길
        String option = "trafast";

        long beforeTime = System.currentTimeMillis();

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

        String clientId = "ncxf9y01px";
        String clientSecret = "aWz84CcrXTVDMSZULVD9HH4Z3J2sWdge5zBYaMQw";

        String response = client.get().uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("start", start)
                        .queryParam("goal", goal)
                        .queryParam("option",option)
                        .build())
                .header("X-NCP-APIGW-API-KEY-ID",clientId)
                .header("X-NCP-APIGW-API-KEY",clientSecret)
                .retrieve() 			  // 응답을 받게 하되,
                .bodyToMono(String.class) // 응답 값을 하나만,
                .block(); 				  // 동기로 받으려 한다.

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(response);
        long duration = jsonObject.getAsJsonObject("route").getAsJsonArray("trafast").get(0).getAsJsonObject().getAsJsonObject("summary").get("duration").getAsLong();
        System.out.println("duration : " + duration);

        long afterTime = System.currentTimeMillis();

        System.out.println(afterTime - beforeTime);
    }
}
