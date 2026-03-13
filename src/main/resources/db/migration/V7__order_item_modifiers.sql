CREATE TABLE order_item_modifiers (
                                      id               BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      order_item_id    BIGINT NOT NULL,
                                      modifier_id      BIGINT NOT NULL,
                                      modifier_name    VARCHAR(100) NOT NULL,
                                      price_adjustment DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                                      quantity         INT NOT NULL DEFAULT 1,
                                      created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      CONSTRAINT fk_order_item_modifiers_item FOREIGN KEY (order_item_id) REFERENCES order_items(id) ON DELETE CASCADE
);