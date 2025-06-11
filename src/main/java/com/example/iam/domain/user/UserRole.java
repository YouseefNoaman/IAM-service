package com.example.iam.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of user roles in the system.
 */
@Getter
@RequiredArgsConstructor
public enum UserRole {

  /**
   * Administrator role with full system access.
   */
  ADMIN("Administrator"),

  /**
   * Regular user role with limited access.
   */
  USER("User");

  /**
   * Human-readable name of the role.
   */
  private final String displayName;
}
