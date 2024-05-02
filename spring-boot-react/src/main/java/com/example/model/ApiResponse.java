package com.example.model;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.http.HttpStatus;

import lombok.Data;

/*
 * ApiResponse class
 */
@Data
public class ApiResponse {
    private int status;
    private String message;
    private String error;

    private Timestamp timestamp = Timestamp.from(Instant.now());

    private Object data;

    public static ApiResponse success(HttpStatus status, String message, Object data) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(message);
        apiResponse.setData(data);
        return apiResponse;
    }

    public static ApiResponse error(HttpStatus status, String error) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(status.value());
        apiResponse.setError(error);
        return apiResponse;
    }
}