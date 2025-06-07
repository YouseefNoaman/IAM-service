package com.example.iam.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
    String path,
    HttpStatus status,
    String message,
    List<String> errors,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp
) {
    public ApiError(String path, HttpStatus status, String message, List<String> errors) {
        this(path, status, message, errors, LocalDateTime.now());
    }

    public ApiError(String path, HttpStatus status, String message) {
        this(path, status, message, List.of(), LocalDateTime.now());
    }
} 