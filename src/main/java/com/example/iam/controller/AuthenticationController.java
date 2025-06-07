package com.example.iam.controller;

import com.example.iam.dto.AuthenticationRequest;
import com.example.iam.dto.AuthenticationResponse;
import com.example.iam.dto.RegisterRequest;
import com.example.iam.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
@SecurityRequirements // Explicitly mark this controller's endpoints as not requiring authentication
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Register a new user",
        description = "Register a new user with email, password, and personal information"
    )
    public Mono<AuthenticationResponse> register(
            @RequestBody @Valid RegisterRequest request
    ) {
        return authenticationService.register(request);
    }

    @PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Authenticate user",
        description = "Authenticate a user with email and password to receive JWT tokens"
    )
    public Mono<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return authenticationService.authenticate(request);
    }
} 