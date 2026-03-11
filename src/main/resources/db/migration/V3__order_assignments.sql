CREATE TABLE order_assignments
(

    id          BIGINT AUTO_INCREMENT PRIMARY KEY,

    order_id    BIGINT NOT NULL,
    waiter_id   BIGINT NOT NULL,

    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (waiter_id) REFERENCES waiters (id)

);