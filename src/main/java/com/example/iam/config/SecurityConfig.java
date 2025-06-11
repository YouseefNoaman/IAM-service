package com.example.iam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

import com.example.iam.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * Security configuration class for the IAM service.
 */
@Configuration
@RequiredArgsConstructor
public final class SecurityConfig {

  /**
   * The JWT authentication filter.
   */
  private final JwtAuthenticationFilter jwtAuthFilter;

  /**
   * Configures the security filter chain.
   *
   * @param http the server HTTP security configuration
   * @return the configured security filter chain
   */
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(
      final ServerHttpSecurity http) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(auth -> auth
            .pathMatchers("/api/v1/auth/**")
            .permitAll()
            .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**",
                "/api-docs/**").permitAll()
            .anyExchange()
            .authenticated()
        )
        .securityContextRepository(
            new WebSessionServerSecurityContextRepository())
        .addFilterAt(jwtAuthFilter,
            SecurityWebFiltersOrder.AUTHENTICATION)
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .build();
  }
}
