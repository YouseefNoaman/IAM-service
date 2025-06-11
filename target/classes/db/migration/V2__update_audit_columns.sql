-- Rename existing audit columns
ALTER TABLE users RENAME COLUMN created_at TO date_created;
ALTER TABLE users RENAME COLUMN updated_at TO last_updated;
ALTER TABLE users RENAME COLUMN updated_by TO last_modified_by;

-- Update column types to include timezone
ALTER TABLE users
ALTER
COLUMN date_created TYPE TIMESTAMP WITH TIME ZONE,
    ALTER
COLUMN last_updated TYPE TIMESTAMP WITH TIME ZONE;

-- Create new indices for audit columns
CREATE INDEX IF NOT EXISTS idx_users_date_created ON users(date_created);
CREATE INDEX IF NOT EXISTS idx_users_last_updated ON users(last_updated);
CREATE INDEX IF NOT EXISTS idx_users_created_by ON users(created_by);
CREATE INDEX IF NOT EXISTS idx_users_last_modified_by ON users(last_modified_by); 