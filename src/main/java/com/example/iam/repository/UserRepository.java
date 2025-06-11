package com.example.iam.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.iam.domain.user.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, UUID> {

  Mono<User> findByEmail(String email);

  Mono<Boolean> existsByEmail(String email);
}
