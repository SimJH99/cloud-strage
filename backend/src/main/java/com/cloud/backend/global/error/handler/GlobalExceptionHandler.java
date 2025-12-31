package com.cloud.backend.global.error.handler;

import com.cloud.backend.global.constants.StatusCode;
import com.cloud.backend.global.error.exception.CustomException;
import com.cloud.backend.global.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        StatusCode statusCode = ex.getStatusCode();

        log.error("CustomServiceException: {} - Status: {} - Code: {} - Message: {}",
                ex.getClass().getName(),
                statusCode.getStatus(),
                statusCode.getCode(),
                ex.getMessage()
        );

        return ApiResponse.toResponseEntity(
                ApiResponse.error(statusCode.getStatus(), statusCode.getCode(), ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleOtherExceptions(Exception ex) {
        String errorMessage;
        HttpStatus httpStatus;

        if (ex instanceof MethodArgumentNotValidException) {
            StringBuilder errorMessages = new StringBuilder();
            ((MethodArgumentNotValidException) ex).getBindingResult()
                    .getAllErrors()
                    .forEach(error -> errorMessages.append(error.getDefaultMessage()).append(" "));
            errorMessage = errorMessages.toString().trim();
            httpStatus = HttpStatus.BAD_REQUEST;

        } else if (ex instanceof EntityNotFoundException) {
            errorMessage = ex.getMessage();
            httpStatus = HttpStatus.NOT_FOUND;

        } else if (ex instanceof IllegalArgumentException) {
            errorMessage = ex.getMessage();
            httpStatus = HttpStatus.BAD_REQUEST;

        } else if (ex instanceof DataIntegrityViolationException) {
            errorMessage = ex.getMessage();
            httpStatus = HttpStatus.CONFLICT;

        } else if (ex instanceof AccessDeniedException) {
            errorMessage = ex.getMessage();
            httpStatus = HttpStatus.FORBIDDEN;

        } else {
            errorMessage = ex.getMessage();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        log.error("Exception: {} - Message: {}", ex.getClass().getName(), ex.getMessage());
        log.error("StackTrace: ", ex);

        return ApiResponse.toResponseEntity(
                ApiResponse.error(
                        httpStatus,
                        httpStatus.name(),
                        errorMessage
                )
        );
    }
}
