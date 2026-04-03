-- Inventory auto-deduction support when KDS order item moves to fired/in_progress

CREATE TABLE IF NOT EXISTS inventory_deduction_audit (
    order_item_id INT NOT NULL,
    deducted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (order_item_id),
    CONSTRAINT fk_inv_deduction_audit_order_item
        FOREIGN KEY (order_item_id) REFERENCES order_items (id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS inventory_deduction_errors (
    id INT NOT NULL AUTO_INCREMENT,
    order_item_id INT NOT NULL,
    order_id INT NULL,
    ingredient_id INT NULL,
    required_quantity DECIMAL(10,4) NULL,
    available_quantity DECIMAL(10,4) NULL,
    reason VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_inv_deduction_errors_order_item (order_item_id),
    INDEX idx_inv_deduction_errors_order (order_id),
    CONSTRAINT fk_inv_deduction_errors_order_item
        FOREIGN KEY (order_item_id) REFERENCES order_items (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_inv_deduction_errors_order
        FOREIGN KEY (order_id) REFERENCES orders (id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_inv_deduction_errors_ingredient
        FOREIGN KEY (ingredient_id) REFERENCES ingredients (id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

