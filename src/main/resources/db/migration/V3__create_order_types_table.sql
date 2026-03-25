-- =============================================================
-- Smart Restaurant ERP — V4 Migration
-- Create Order Types table and update Orders table
-- =============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- Create order_types table
CREATE TABLE IF NOT EXISTS order_types (
    id INT NOT NULL AUTO_INCREMENT,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NULL DEFAULT NULL,
    is_active TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Insert standard order types
INSERT INTO order_types (type_name, description, is_active)
VALUES
    ('dine_in', 'Dine-in orders at restaurant tables', 1),
    ('takeout', 'Takeout orders for customer pickup', 1),
    ('delivery', 'Delivery orders to customer addresses', 1),
    ('online', 'Online orders through web/app platform', 1);

-- Modify orders table to use order_type_id foreign key
-- First, add the new column
ALTER TABLE orders ADD COLUMN order_type_id INT NULL DEFAULT NULL AFTER id;

-- Update existing orders based on order_type enum values
UPDATE orders o
SET o.order_type_id = (
    SELECT id FROM order_types ot WHERE ot.type_name = o.order_type
)
WHERE o.order_type IS NOT NULL;

-- Add foreign key constraint
ALTER TABLE orders
ADD CONSTRAINT fk_orders_order_type
FOREIGN KEY (order_type_id) REFERENCES order_types (id)
ON DELETE RESTRICT ON UPDATE CASCADE;

-- Note: The old order_type ENUM column can be kept for backward compatibility
-- or dropped later after confirming all code changes are in place.

SET FOREIGN_KEY_CHECKS = 1;

