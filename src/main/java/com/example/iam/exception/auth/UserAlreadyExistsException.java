package com.example.iam.exception.auth;

/**
 * Exception thrown when attempting to create a user with an email that already exists.
 */
public class UserAlreadyExistsException extends RuntimeException {

  /**
   * Constructs a new UserAlreadyExistsException with the specified email.
   *
   * @param email the email address that already exists
   */
  public UserAlreadyExistsException(final String email) {
    super("User already exists with email: " + email);
  }
}
