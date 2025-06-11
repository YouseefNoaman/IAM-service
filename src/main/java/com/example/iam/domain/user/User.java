package com.example.iam.domain.user;

import java.util.Collection;
import java.util.Collections;

import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.iam.domain.common.BaseEntity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User entity representing a system user with authentication details. Implements Spring Security's
 * UserDetails for authentication integration.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Table("users")
public final class User extends BaseEntity implements UserDetails {

  /**
   * User's first name.
   */
  private String firstName;

  /**
   * User's last name.
   */
  private String lastName;

  /**
   * User's email address (used as username).
   */
  private String email;

  /**
   * User's encrypted password.
   */
  private String password;

  /**
   * User's role in the system.
   */
  private UserRole role;

  /**
   * Flag indicating if the account is enabled.
   */
  private boolean enabled;

  /**
   * Flag indicating if the account is locked.
   */
  private boolean accountNonLocked;

  /**
   * Flag indicating if the account is expired.
   */
  private boolean accountNonExpired;

  /**
   * Flag indicating if the credentials are expired.
   */
  private boolean credentialsNonExpired;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(
        new SimpleGrantedAuthority("ROLE_" + role.toString())
    );
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
