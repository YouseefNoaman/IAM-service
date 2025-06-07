package com.example.iam.service;

import com.example.iam.domain.user.User;
import com.example.iam.domain.user.UserRole;
import com.example.iam.dto.AuthenticationRequest;
import com.example.iam.dto.AuthenticationResponse;
import com.example.iam.dto.RegisterRequest;
import com.example.iam.repository.UserRepository;
import com.example.iam.repository.UserRoleRepository;
import com.example.iam.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public Mono<AuthenticationResponse> register(RegisterRequest request) {
        return userRepository.existsByEmail(request.email())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new IllegalArgumentException("Email already exists"));
                }

                User user = User.builder()
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .build();
                user.setId(UUID.randomUUID());

                return userRepository.save(user)
                    .flatMap(savedUser -> {
                        UserRole userRole = UserRole.builder()
                            .userId(savedUser.getId())
                            .role("USER")
                            .build();
                        
                        return userRoleRepository.save(userRole)
                            .then(userRoleRepository.findByUserId(savedUser.getId())
                                .map(UserRole::getRole)
                                .collectList()
                                .doOnNext(roles -> savedUser.setRoles(Set.copyOf(roles)))
                                .thenReturn(savedUser));
                    })
                    .map(savedUser -> {
                        String accessToken = jwtService.generateAccessToken(savedUser);
                        String refreshToken = jwtService.generateRefreshToken(savedUser);
                        return new AuthenticationResponse(accessToken, refreshToken);
                    });
            });
    }

    public Mono<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        return userRepository.findByEmail(request.email())
            .flatMap(user -> {
                if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                    return Mono.error(new IllegalArgumentException("Invalid credentials"));
                }

                return userRoleRepository.findByUserId(user.getId())
                    .map(UserRole::getRole)
                    .collectList()
                    .doOnNext(roles -> user.setRoles(Set.copyOf(roles)))
                    .map(roles -> {
                        String accessToken = jwtService.generateAccessToken(user);
                        String refreshToken = jwtService.generateRefreshToken(user);
                        return new AuthenticationResponse(accessToken, refreshToken);
                    });
            })
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid credentials")));
    }
} 