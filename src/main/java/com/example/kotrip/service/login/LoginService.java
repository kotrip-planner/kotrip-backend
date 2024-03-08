package com.example.kotrip.service.login;

import com.example.kotrip.dto.user.response.LoginResponseDto;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private static final String REQ_URL = "https://kauth.kakao.com/oauth/token";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String APP_KEY;

    public LoginResponseDto getAccessToken(String code) throws MalformedURLException {

        String accessToken = null;
        String refreshToken = null;

        try {
            URL url = new URL(REQ_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // POST 요청
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + APP_KEY);
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            System.out.println("responseCode : " + con.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "";
            String result = "";

            while((line = br.readLine()) != null) {
                result += line;
            }

            System.out.println("response body:" + result);
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get(ACCESS_TOKEN).getAsString();
            refreshToken = element.getAsJsonObject().get(REFRESH_TOKEN).getAsString();

            System.out.println("access token : " + accessToken);
            System.out.println("refresh token : " + refreshToken);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return LoginResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }
}
