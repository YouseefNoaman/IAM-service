package com.example.iam.exception.rest;

public class RestAuthenticationException extends RuntimeException {
    public RestAuthenticationException(String message) {
        super(message);
    }

    public RestAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
} 