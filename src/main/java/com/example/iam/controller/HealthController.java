package com.example.iam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("api/v1/health")
@Tag(name = "Health Check", description = "Health check endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class HealthController {

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