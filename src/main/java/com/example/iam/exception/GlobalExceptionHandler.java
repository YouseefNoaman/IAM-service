package com.example.iam.exception;

import java.util.List;

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

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Global exception handler for the IAM service. This class can be extended to add custom exception handling.
 * When extending this class, you can:
 * 
 * 1. Override existing handlers to customize error responses
 * 2. Add new handlers for different exception types
 * 3. Modify the error response format
 * 
 * Guidelines for extension:
 * - Maintain consistent error response format
 * - Ensure proper logging of exceptions
 * - Handle exceptions at appropriate levels
 * - Consider security implications when exposing error details
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles UserAlreadyExistsException.
   * Can be overridden to customize the error response or add additional processing.
   *
   * @param ex the UserAlreadyExistsException
   * @param exchange the current server exchange
   * @return a Mono containing the error response
   */
  @ExceptionHandler(UserAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public Mono<ApiError> handleUserAlreadyExists(final UserAlreadyExistsException ex,
      final ServerWebExchange exchange) {
    log.error("User already exists error: {}", ex.getMessage());
    return createErrorResponse(exchange, HttpStatus.CONFLICT, ex.getMessage());
  }

  /**
   * Handles invalid credentials exceptions.
   * Can be overridden to customize the error response or add additional processing.
   *
   * @param ex the RuntimeException (InvalidCredentialsException or BadCredentialsException)
   * @param exchange the current server exchange
   * @return a Mono containing the error response
   */
  @ExceptionHandler({InvalidCredentialsException.class, BadCredentialsException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Mono<ApiError> handleInvalidCredentials(final RuntimeException ex,
      final ServerWebExchange exchange) {
    log.error("Invalid credentials error: {}", ex.getMessage());
    return createErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Invalid email or password");
  }

  /**
   * Handles access denied exceptions.
   * Can be overridden to customize the error response or add additional processing.
   *
   * @param ex the AccessDeniedException
   * @param exchange the current server exchange
   * @return a Mono containing the error response
   */
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public Mono<ApiError> handleAccessDenied(final AccessDeniedException ex,
      final ServerWebExchange exchange) {
    log.error("Access denied error: {}", ex.getMessage());
    return createErrorResponse(exchange, HttpStatus.FORBIDDEN, "Access denied");
  }

  /**
   * Handles authentication exceptions.
   * Can be overridden to customize the error response or add additional processing.
   *
   * @param ex the AuthenticationException
   * @param exchange the current server exchange
   * @return a Mono containing the error response
   */
  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Mono<ApiError> handleAuthentication(final AuthenticationException ex,
      final ServerWebExchange exchange) {
    log.error("Authentication error: {}", ex.getMessage());
    return createErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Authentication failed");
  }

  /**
   * Handles validation exceptions.
   * Can be overridden to customize the error response or add additional processing.
   *
   * @param ex the WebExchangeBindException
   * @param exchange the current server exchange
   * @return a Mono containing the error response
   */
  @ExceptionHandler(WebExchangeBindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ApiError> handleValidation(final WebExchangeBindException ex,
      final ServerWebExchange exchange) {
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(FieldError::getDefaultMessage)
        .toList();

    log.error("Validation error: {}", errors);
    return createErrorResponse(exchange, HttpStatus.BAD_REQUEST, "Validation failed", errors);
  }

  /**
   * Handles constraint violation exceptions.
   * Can be overridden to customize the error response or add additional processing.
   *
   * @param ex the ConstraintViolationException
   * @param exchange the current server exchange
   * @return a Mono containing the error response
   */
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ApiError> handleConstraintViolation(final ConstraintViolationException ex,
      final ServerWebExchange exchange) {
    List<String> errors = ex.getConstraintViolations()
        .stream()
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .toList();

    log.error("Constraint violation: {}", errors);
    return createErrorResponse(exchange, HttpStatus.BAD_REQUEST, "Validation failed", errors);
  }

  /**
   * Handles all unhandled exceptions.
   * Can be overridden to customize the error response or add additional processing.
   *
   * @param ex the Exception
   * @param exchange the current server exchange
   * @return a Mono containing the error response
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Mono<ApiError> handleAll(final Exception ex, final ServerWebExchange exchange) {
    log.error("Unexpected error occurred", ex);
    return createErrorResponse(
        exchange,
        HttpStatus.INTERNAL_SERVER_ERROR,
        "An unexpected error occurred"
    );
  }

  /**
   * Creates an error response without additional errors.
   * Can be overridden to customize the error response format.
   *
   * @param exchange the current server exchange
   * @param status the HTTP status
   * @param message the error message
   * @return a Mono containing the error response
   */
  private Mono<ApiError> createErrorResponse(
      final ServerWebExchange exchange,
      final HttpStatus status,
      final String message
  ) {
    return createErrorResponse(exchange, status, message, List.of());
  }

  /**
   * Creates an error response with additional errors.
   * Can be overridden to customize the error response format.
   *
   * @param exchange the current server exchange
   * @param status the HTTP status
   * @param message the error message
   * @param errors the list of additional errors
   * @return a Mono containing the error response
   */
  private Mono<ApiError> createErrorResponse(
      final ServerWebExchange exchange,
      final HttpStatus status,
      final String message,
      final List<String> errors
  ) {
    String path = exchange.getRequest().getPath().value();
    return Mono.just(new ApiError(path, status, message, errors));
  }
}
