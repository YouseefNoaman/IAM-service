package com.example.iam.dto.rest;

import java.time.Instant;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Response DTO for authentication operations.
 * Contains user information and authentication tokens.
 */
@Setter
@Getter
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationRestResponse {

  /**
   * Unique identifier of the authenticated user.
   */
  private String userId;

  /**
   * Email address of the authenticated user.
   */
  private String email;

  /**
   * First name of the authenticated user.
   */
  private String firstName;

  /**
   * Last name of the authenticated user.
   */
  private String lastName;

  /**
   * Set of roles assigned to the authenticated user.
   */
  private Set<String> roles;

  /**
   * Authentication tokens for the user session.
   */
  private TokenInfo tokens;

  /**
   * Timestamp of the user's last successful login.
   */
  private Instant lastLoginAt;

  /**
   * Timestamp when the user account was created.
   */
  private Instant createdAt;

  /**
   * Inner class containing token-related information.
   */
  @Setter
  @Getter
  @EqualsAndHashCode
  @Builder
  public static class TokenInfo {

    /**
     * JWT access token for API authentication.
     */
    private String accessToken;

    /**
     * JWT refresh token for obtaining new access tokens.
     */
    private String refreshToken;

    /**
     * Expiration timestamp for the access token.
     */
    private Instant accessTokenExpiresAt;

    /**
     * Expiration timestamp for the refresh token.
     */
    private Instant refreshTokenExpiresAt;
  }
}
