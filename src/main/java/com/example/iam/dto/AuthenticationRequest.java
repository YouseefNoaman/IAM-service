package com.example.iam.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Record representing an authentication request containing user credentials.
 *
 * @param email    the user's email address
 * @param password the user's password
 */
public record AuthenticationRequest(
    @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
    @NotBlank(message = "Password is required") String password
) { }
