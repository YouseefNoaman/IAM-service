package com.example.iam.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Record representing an API error response.
 *
 * @param path      the path of the request that caused the error
 * @param status    the HTTP status code of the error
 * @param message   the main error message
 * @param errors    list of detailed error messages
 * @param timestamp the time when the error occurred
 */
public record ApiError(
    String path,
    HttpStatus status,
    String message,
    List<String> errors,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp
) {

  /**
   * Constructor with current timestamp.
   *
   * @param path    the path of the request that caused the error
   * @param status  the HTTP status code of the error
   * @param message the main error message
   * @param errors  list of detailed error messages
   */
  public ApiError(final String path, final HttpStatus status, final String message,
      final List<String> errors) {
    this(path, status, message, errors, LocalDateTime.now());
  }

  /**
   * Constructor with current timestamp and empty errors list.
   *
   * @param path    the path of the request that caused the error
   * @param status  the HTTP status code of the error
   * @param message the main error message
   */
  public ApiError(final String path, final HttpStatus status, final String message) {
    this(path, status, message, List.of(), LocalDateTime.now());
  }
}
