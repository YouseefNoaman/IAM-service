package com.example.iam.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Filter for handling JWT-based authentication in the request pipeline.
 */
@Component
@RequiredArgsConstructor
public final class JwtAuthenticationFilter implements WebFilter {

  /**
   * Number of characters in "Bearer " prefix.
   */
  private static final int BEARER_PREFIX_LENGTH = 7;

  /**
   * Service for handling JWT operations.
   */
  private final JwtService jwtService;

  /**
   * Service for loading user details.
   */
  private final ReactiveUserDetailsService userDetailsService;

  /**
   * Filters incoming requests to handle JWT authentication.
   *
   * @param exchange the current server exchange
   * @param chain    the filter chain
   * @return void mono
   */
  @Override
  public @NonNull Mono<Void> filter(
      final @NonNull ServerWebExchange exchange,
      final @NonNull WebFilterChain chain) {
    String authHeader = exchange.getRequest()
        .getHeaders()
        .getFirst(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return chain.filter(exchange);
    }

    String jwt = authHeader.substring(BEARER_PREFIX_LENGTH);
    String userEmail = jwtService.extractUsername(jwt);

    return userEmail != null
        ? userDetailsService.findByUsername(userEmail)
        .filter(userDetails -> jwtService.isTokenValid(jwt, userDetails))
        .map(userDetails -> new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        ))
        .flatMap(auth -> chain.filter(exchange)
            .contextWrite(ReactiveSecurityContextHolder
                .withAuthentication(auth)))
        .switchIfEmpty(chain.filter(exchange))
        : chain.filter(exchange);
  }
}
