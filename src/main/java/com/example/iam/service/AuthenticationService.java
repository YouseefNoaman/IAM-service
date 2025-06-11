package com.example.iam.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.iam.domain.user.User;
import com.example.iam.domain.user.UserRole;
import com.example.iam.dto.AuthenticationRequest;
import com.example.iam.dto.AuthenticationResponse;
import com.example.iam.dto.RegisterRequest;
import com.example.iam.exception.InvalidCredentialsException;
import com.example.iam.exception.UserAlreadyExistsException;
import com.example.iam.repository.UserRepository;
import com.example.iam.repository.UserRoleRepository;
import com.example.iam.security.JwtService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Service class handling user authentication operations.
 */
@Service
@RequiredArgsConstructor
public final class AuthenticationService {

  /**
   * Repository for user operations.
   */
  private final UserRepository userRepository;

  /**
   * Repository for user role operations.
   */
  private final UserRoleRepository userRoleRepository;

  /**
   * Service for JWT operations.
   */
  private final JwtService jwtService;

  /**
   * Encoder for password hashing.
   */
  private final PasswordEncoder passwordEncoder;

  /**
   * Registers a new user.
   *
   * @param request the registration request
   * @return a mono containing the authentication response
   */
  public Mono<AuthenticationResponse> register(final RegisterRequest request) {
    return userRepository.findByEmail(request.email())
        .hasElement()
        .flatMap(exists -> exists
            ? Mono.<AuthenticationResponse>error(
            new UserAlreadyExistsException(request.email()))
            : createUser(request));
  }

  /**
   * Authenticates a user.
   *
   * @param request the authentication request
   * @return a mono containing the authentication response
   */
  public Mono<AuthenticationResponse> authenticate(final AuthenticationRequest request) {
    return userRepository.findByEmail(request.email())
        .filter(user -> passwordEncoder.matches(
            request.password(), user.getPassword()))
        .map(user -> {
          String accessToken = jwtService.generateAccessToken(user);
          String refreshToken = jwtService.generateRefreshToken(user);
          return new AuthenticationResponse(accessToken, refreshToken);
        })
        .switchIfEmpty(Mono.error(new InvalidCredentialsException()));
  }

  private Mono<AuthenticationResponse> createUser(final RegisterRequest request) {
    User user = User.builder()
        .email(request.email())
        .password(passwordEncoder.encode(request.password()))
        .firstName(request.firstName())
        .lastName(request.lastName())
        .role(UserRole.USER)
        .enabled(true)
        .accountNonExpired(true)
        .accountNonLocked(true)
        .credentialsNonExpired(true)
        .build();

    return userRepository.save(user)
        .map(savedUser -> {
          String accessToken = jwtService.generateAccessToken(savedUser);
          String refreshToken = jwtService.generateRefreshToken(savedUser);
          return new AuthenticationResponse(accessToken, refreshToken);
        });
  }
}
