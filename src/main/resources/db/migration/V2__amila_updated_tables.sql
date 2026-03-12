CREATE TABLE orders (
                        id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                        table_id       BIGINT NOT NULL,
                        customer_id    BIGINT DEFAULT NULL,
                        order_number   VARCHAR(50) NOT NULL UNIQUE,
                        status         ENUM('RECEIVED','PREPARING','READY','COMPLETED','CANCELLED') NOT NULL DEFAULT 'RECEIVED',
                        total_amount   DECIMAL(10,2) NOT NULL,
                        tax            DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                        payment_status ENUM('PENDING','PAID','REFUNDED') DEFAULT 'PENDING',
                        created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

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