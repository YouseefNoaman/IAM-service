package com.example.iam.dto.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Setter;
import lombok.Getter;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.Set;

@Setter
@Getter
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationRestResponse {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roles;
    private TokenInfo tokens;
    private Instant lastLoginAt;
    private Instant createdAt;

    @Setter
    @Getter
    @EqualsAndHashCode
    @Builder
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;
        private Instant accessTokenExpiresAt;
        private Instant refreshTokenExpiresAt;
    }
} 