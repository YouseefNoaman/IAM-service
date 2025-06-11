package com.example.iam.exception.rest;

/**
 * Exception thrown when REST authentication fails.
 */
public class RestAuthenticationException extends RuntimeException {

  /**
   * Constructs a new RestAuthenticationException with the specified message.
   *
   * @param message the error message
   */
  public RestAuthenticationException(final String message) {
    super(message);
  }

  /**
   * Constructs a new RestAuthenticationException with the specified message and cause.
   *
   * @param message the error message
   * @param cause   the cause of the exception
   */
  public RestAuthenticationException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
