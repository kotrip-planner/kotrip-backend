package com.example.kotrip.dto.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ApiResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;

    public ApiResponse(final HttpStatus status, final String message, final T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(final HttpStatus status, final String message, final T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> ok(final T data) {
        return of(HttpStatus.OK, HttpStatus.OK.name(), data);
    }

    public static <T> ApiResponse<T> ok(final String message){
        return of(HttpStatus.OK, message, null);
    }
}
