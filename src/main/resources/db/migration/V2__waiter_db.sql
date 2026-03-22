
CREATE TABLE IF NOT EXISTS order_status_updates (
                                                    id         INT      NOT NULL AUTO_INCREMENT,
                                                    order_id   INT      NOT NULL,
                                                    waiter_id  INT      NOT NULL,
                                                    status     ENUM('served','unserved') NOT NULL,
                                                    updated_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                                                    PRIMARY KEY (id),
                                                    UNIQUE KEY uq_order_waiter (order_id, waiter_id),
                                                    CONSTRAINT fk_osu_order
                                                        FOREIGN KEY (order_id) REFERENCES orders (id)
                                                            ON DELETE CASCADE ON UPDATE CASCADE,
                                                    CONSTRAINT fk_osu_waiter
                                                        FOREIGN KEY (waiter_id) REFERENCES waiter (id)
                                                            ON DELETE CASCADE ON UPDATE CASCADE
);