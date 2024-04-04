package com.example.kotrip.util.token;

public class TokenUtils {

    public static String resolveToken(String token) {
        return token.split(" ")[1];
    }
}
