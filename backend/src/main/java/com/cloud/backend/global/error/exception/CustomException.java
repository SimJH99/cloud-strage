package com.cloud.backend.global.error.exception;

import com.cloud.backend.global.constants.StatusCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final StatusCode statusCode;

    public CustomException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
    }

    public CustomException(StatusCode statusCode, String message) {
        super(message != null ? message : statusCode.getMessage());
        this.statusCode = statusCode;
    }
}
