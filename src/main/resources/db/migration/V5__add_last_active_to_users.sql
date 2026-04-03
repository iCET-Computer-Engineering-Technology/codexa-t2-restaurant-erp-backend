-- =============================================================
-- Smart Restaurant ERP €” V5 Migration
-- Add last_active_at to users table for heartbeat mechanism
-- =============================================================

ALTER TABLE users
ADD COLUMN last_active_at DATETIME NULL DEFAULT NULL AFTER is_online;

