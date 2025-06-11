package com.example.iam.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.iam.domain.user.UserRole;

import reactor.core.publisher.Flux;

@Repository
public interface UserRoleRepository extends R2dbcRepository<UserRole, UUID> {

  Flux<UserRole> findByUserId(UUID userId);
}
