package com.example.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

/**
 * Main application class for the IAM Service. Configures Spring Boot application with R2DBC,
 * caching, security, and transaction management.
 */
@SpringBootApplication
@EnableR2dbcAuditing
@EnableWebFluxSecurity
@EnableTransactionManagement
@EnableCaching
@OpenAPIDefinition(
    info = @Info(
        title = "IAM Service API",
        version = "1.0.0",
        description = "Modern Identity and Access Management Service API Documentation",
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
    )
)
public final class IamServiceApplication {

  /**
   * Private constructor to prevent instantiation.
   */
  private IamServiceApplication() {
  }

  /**
   * Main method that starts the Spring Boot application.
   *
   * @param args command line arguments
   */
  public static void main(final String[] args) {
    SpringApplication.run(IamServiceApplication.class, args);
  }
}
