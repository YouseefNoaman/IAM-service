package com.example.iam.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.iam.dto.AuthenticationRequest;
import com.example.iam.dto.AuthenticationResponse;
import com.example.iam.dto.RegisterRequest;
import com.example.iam.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Controller handling authentication-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
@SecurityRequirements // Explicitly mark this controller's endpoints as not requiring authentication
public final class AuthenticationController {

  /**
   * Service handling authentication operations.
   */
  private final AuthenticationService authenticationService;

  /**
   * Handles user registration requests.
   *
   * @param request Registration details
   * @return Authentication response with tokens
   */
  @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Register a new user",
      description = "Register a new user with email, password, and personal information"
  )
  public Mono<ResponseEntity<AuthenticationResponse>> register(
      @Valid @RequestBody final RegisterRequest request
  ) {
    return authenticationService.register(request)
        .map(ResponseEntity::ok);
  }

  /**
   * Handles user authentication requests.
   *
   * @param request Authentication credentials
   * @return Authentication response with tokens
   */
  @PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Authenticate user",
      description = "Authenticate a user with email and password to receive JWT tokens"
  )
  public Mono<ResponseEntity<AuthenticationResponse>> authenticate(
      @Valid @RequestBody final AuthenticationRequest request
  ) {
    return authenticationService.authenticate(request)
        .map(ResponseEntity::ok);
  }
}
