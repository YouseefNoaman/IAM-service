CREATE TABLE IF NOT EXISTS users
(
    id
    UUID
    PRIMARY
    KEY,
    email
    VARCHAR
(
    255
) NOT NULL UNIQUE,
    password VARCHAR
(
    255
) NOT NULL,
    first_name VARCHAR
(
    255
) NOT NULL,
    last_name VARCHAR
(
    255
) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    date_created TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
                               created_by VARCHAR (255),
    last_modified_by VARCHAR
(
    255
),
    version BIGINT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
    );

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id
    UUID
    NOT
    NULL
    REFERENCES
    users
(
    id
),
    role VARCHAR
(
    50
) NOT NULL,
    PRIMARY KEY
(
    user_id,
    role
)
    );

-- Index for email column (though UNIQUE constraint already creates an index)
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- Index for soft delete flag as it's commonly used in queries
CREATE INDEX IF NOT EXISTS idx_users_deleted ON users(deleted);

-- Index for user roles for faster role lookups
CREATE INDEX IF NOT EXISTS idx_user_roles_role ON user_roles(role);

-- Composite index for common queries that might filter by multiple columns
CREATE INDEX IF NOT EXISTS idx_users_email_deleted ON users(email, deleted);

-- Index for audit fields that might be used in queries
CREATE INDEX IF NOT EXISTS idx_users_date_created ON users(date_created);
CREATE INDEX IF NOT EXISTS idx_users_last_updated ON users(last_updated);
CREATE INDEX IF NOT EXISTS idx_users_created_by ON users(created_by);
CREATE INDEX IF NOT EXISTS idx_users_last_modified_by ON users(last_modified_by); 