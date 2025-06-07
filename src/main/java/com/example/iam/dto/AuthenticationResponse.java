package com.example.iam.dto;

public record AuthenticationResponse(
    String accessToken,
    String refreshToken
) {} 