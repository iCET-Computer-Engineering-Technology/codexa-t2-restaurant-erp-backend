-- =============================================================
-- Smart Restaurant ERP €” V4 Migration
-- Add is_online status to users table
-- =============================================================

ALTER TABLE users
ADD COLUMN is_online TINYINT NULL DEFAULT 0 AFTER enabled;

