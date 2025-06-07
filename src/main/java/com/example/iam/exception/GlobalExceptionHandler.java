package com.example.iam.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<ApiError> handleUserAlreadyExists(UserAlreadyExistsException ex, ServerWebExchange exchange) {
        log.error("User already exists error: {}", ex.getMessage());
        return createErrorResponse(exchange, HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({InvalidCredentialsException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<ApiError> handleInvalidCredentials(RuntimeException ex, ServerWebExchange exchange) {
        log.error("Invalid credentials error: {}", ex.getMessage());
        return createErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Mono<ApiError> handleAccessDenied(AccessDeniedException ex, ServerWebExchange exchange) {
        log.error("Access denied error: {}", ex.getMessage());
        return createErrorResponse(exchange, HttpStatus.FORBIDDEN, "Access denied");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<ApiError> handleAuthentication(AuthenticationException ex, ServerWebExchange exchange) {
        log.error("Authentication error: {}", ex.getMessage());
        return createErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Authentication failed");
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiError> handleValidation(WebExchangeBindException ex, ServerWebExchange exchange) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        log.error("Validation error: {}", errors);
        return createErrorResponse(exchange, HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiError> handleConstraintViolation(ConstraintViolationException ex, ServerWebExchange exchange) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList();

        log.error("Constraint violation: {}", errors);
        return createErrorResponse(exchange, HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ApiError> handleAll(Exception ex, ServerWebExchange exchange) {
        log.error("Unexpected error occurred", ex);
        return createErrorResponse(
            exchange,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred"
        );
    }

    private Mono<ApiError> createErrorResponse(
            ServerWebExchange exchange,
            HttpStatus status,
            String message
    ) {
        return createErrorResponse(exchange, status, message, List.of());
    }

    private Mono<ApiError> createErrorResponse(
            ServerWebExchange exchange,
            HttpStatus status,
            String message,
            List<String> errors
    ) {
        String path = exchange.getRequest().getPath().value();
        return Mono.just(new ApiError(path, status, message, errors));
    }
} 