package com.example.iam.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

/**
 * Controller for health check endpoints. This class can be extended to add custom health checks.
 * When extending this class, you can:
 * 
 * 1. Override existing health check methods to add custom health metrics
 * 2. Add new health check endpoints for specific components
 * 3. Customize the health check response format
 * 
 * Guidelines for extension:
 * - Keep health checks lightweight and fast
 * - Add meaningful health metrics
 * - Consider security implications of exposed information
 * - Maintain consistent response format
 */
@RestController
@RequestMapping("api/v1/health")
@Tag(name = "Health Check", description = "Health check endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class HealthController {

  /**
   * Basic health check endpoint that can be extended to include additional health metrics.
   * Override this method to customize the health check response.
   *
   * @return a map containing basic health information
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Basic health check",
      description = "Returns basic application health information"
  )
  public Map<String, Object> healthCheck() {
    return Map.of(
        "status", "UP",
        "timestamp", Instant.now(),
        "service", "IAM Service",
        "version", "1.0.0"
    );
  }

  /**
   * Reactive health check endpoint that can be extended to include additional health metrics.
   * Override this method to customize the reactive health check response.
   *
   * @return a Mono containing a map with basic health information
   */
  @GetMapping(value = "/reactive", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Reactive health check",
      description = "Returns basic application health information using reactive processing"
  )
  public Mono<Map<String, Object>> reactiveHealthCheck() {
    return Mono.just(Map.of(
        "status", "UP",
        "timestamp", Instant.now(),
        "service", "IAM Service Reactive",
        "version", "1.0.0"
    ));
  }
}
