package com.cloud.backend.global.response;

import com.cloud.backend.global.constants.StatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        HttpStatus status,
        String code,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(StatusCode.SUCCESS.getStatus())
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static ApiResponse<Void> success() {
        return ApiResponse.<Void>builder()
                .status(StatusCode.SUCCESS.getStatus())
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .build();
    }

    public static <T> ApiResponse<T> error(StatusCode statusCode) {
        return ApiResponse.<T>builder()
                .status(statusCode.getStatus())
                .code(statusCode.getCode())
                .message(statusCode.getMessage())
                .build();
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String code, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .code(code)
                .message(message)
                .build();
    }

    public static <T> ResponseEntity<ApiResponse<T>> toResponseEntity(ApiResponse<T> response) {
        return ResponseEntity
                .status(response.status)
                .body(response);
    }
}
