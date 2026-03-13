CREATE TABLE order_items (
                             id           BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id     BIGINT NOT NULL,
                             menu_item_id BIGINT NOT NULL,
                             quantity     INT NOT NULL,
                             unit_price   DECIMAL(10,2) NOT NULL,
                             total_price  DECIMAL(10,2) NOT NULL,
                             created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);
