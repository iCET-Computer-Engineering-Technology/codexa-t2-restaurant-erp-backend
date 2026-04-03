-- =============================================================
-- Smart Restaurant ERP €” V3 Migration
-- Add Chef Assignment feature
-- =============================================================

ALTER TABLE kitchen_order
ADD COLUMN chef_id INT NULL DEFAULT NULL AFTER order_id;

ALTER TABLE kitchen_order
ADD CONSTRAINT fk_kitchen_order_chef
FOREIGN KEY (chef_id) REFERENCES users (id)
ON DELETE SET NULL ON UPDATE CASCADE;

