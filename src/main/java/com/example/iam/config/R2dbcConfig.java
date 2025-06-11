package com.example.iam.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.transaction.ReactiveTransactionManager;

import io.r2dbc.spi.ConnectionFactory;

/**
 * Configuration class for R2DBC database connectivity. Provides beans for transaction management
 * and auditing.
 */
@Configuration
@EnableR2dbcRepositories(basePackages = "com.example.iam.repository")
@EnableR2dbcAuditing
public final class R2dbcConfig {

  /**
   * Creates a transaction manager for R2DBC operations.
   *
   * @param connectionFactory the R2DBC connection factory
   * @return the reactive transaction manager
   */
  @Bean
  public ReactiveTransactionManager transactionManager(final ConnectionFactory connectionFactory) {
    return new R2dbcTransactionManager(connectionFactory);
  }

  /**
   * Creates an auditor aware implementation for tracking entity changes.
   *
   * @return the reactive auditor aware implementation
   */
  @Bean
  public ReactiveAuditorAware<UUID> auditorAware() {
    return () -> ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .filter(auth -> auth != null && auth.isAuthenticated())
        .map(auth -> UUID.fromString(auth.getName()));
  }
}
