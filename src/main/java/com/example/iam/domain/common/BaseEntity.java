package com.example.iam.domain.common;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Getter;
import lombok.Setter;

/**
 * Base entity class providing common fields and functionality for all domain entities.
 * This class can be extended to create domain-specific entities with built-in auditing
 * and versioning support.
 * 
 * When extending this class:
 * 1. All audit fields (created/updated timestamps and users) are automatically managed
 * 2. Optimistic locking is handled through the version field
 * 3. Soft delete functionality is available through the deleted flag
 * 4. UUID-based identification is provided
 */
@Getter
@Setter
public abstract class BaseEntity implements Persistable<UUID> {

  /**
   * Unique identifier for the entity.
   */
  @Id
  private UUID id;

  /**
   * Timestamp when the entity was created.
   * Automatically managed by Spring Data's auditing support.
   */
  @CreatedDate
  @Column("created_at")
  private Instant createdAt;

  /**
   * Timestamp when the entity was last modified.
   * Automatically managed by Spring Data's auditing support.
   */
  @LastModifiedDate
  @Column("updated_at")
  private Instant updatedAt;

  /**
   * Username or ID of the user who created the entity.
   * Automatically managed by Spring Data's auditing support.
   */
  @CreatedBy
  @Column("created_by")
  private String createdBy;

  /**
   * Username or ID of the user who last modified the entity.
   * Automatically managed by Spring Data's auditing support.
   */
  @LastModifiedBy
  @Column("updated_by")
  private String updatedBy;

  /**
   * Version number for optimistic locking.
   * Automatically incremented by Spring Data on each update.
   */
  @Version
  private Long version;

  /**
   * Flag indicating whether the entity has been soft deleted.
   * True if the entity is deleted, false otherwise.
   */
  @Column("deleted")
  private boolean deleted = false;

  /**
   * Determines if the entity is new by checking if it has an ID.
   * This method can be overridden to provide custom new/existing logic.
   *
   * @return true if the entity is new (has no ID), false otherwise
   */
  @Override
  public boolean isNew() {
    return id == null;
  }
}
