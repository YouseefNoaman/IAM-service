package com.example.iam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.iam.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Configuration class for application-wide beans and security settings.
 */
@Configuration
@RequiredArgsConstructor
public final class ApplicationConfig {

  /**
   * Repository for user-related database operations.
   */
  private final UserRepository userRepository;

  /**
   * Creates a ReactiveUserDetailsService bean for user authentication.
   *
   * @return A ReactiveUserDetailsService implementation
   */
  @Bean
  public ReactiveUserDetailsService userDetailsService() {
    return username -> userRepository.findByEmail(username)
        .cast(UserDetails.class)
        .switchIfEmpty(Mono.error(
            new UsernameNotFoundException("User not found")));
  }

  /**
   * Creates a ReactiveAuthenticationManager bean for authentication handling.
   *
   * @return A configured ReactiveAuthenticationManager
   */
  @Bean
  public ReactiveAuthenticationManager authenticationManager() {
    UserDetailsRepositoryReactiveAuthenticationManager authManager =
        new UserDetailsRepositoryReactiveAuthenticationManager(
            userDetailsService());
    authManager.setPasswordEncoder(passwordEncoder());
    return authManager;
  }

  /**
   * Creates a PasswordEncoder bean for password hashing.
   *
   * @return A BCryptPasswordEncoder instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
