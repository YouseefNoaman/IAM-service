package com.example.iam.config;

import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

/**
 * Implementation of ReactiveAuditorAware that provides auditing information based on the current security context.
 * This class can be extended to customize the auditing behavior in the following ways:
 * 
 * 1. Override getCurrentAuditor() to modify how the auditor is determined
 * 2. Add additional auditing metadata by extending the class
 * 3. Implement custom security context handling
 * 
 * When extending this class, ensure that:
 * - The security context is properly handled in a reactive way
 * - The default value is appropriate for your use case
 * - Any custom implementation maintains thread safety
 */
@Component
public class SecurityAuditorAware implements ReactiveAuditorAware<String> {

  /**
   * Gets the current auditor from the security context.
   * This method can be overridden to customize how the auditor is determined.
   * 
   * @return a Mono containing the username of the current authenticated user,
   *         or "system" if no authenticated user is found
   */
  @Override
  public Mono<String> getCurrentAuditor() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .map(Authentication::getName)
        .defaultIfEmpty("system");
  }
}
