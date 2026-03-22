CREATE TABLE IF NOT EXISTS waiter (
                                      id          INT          NOT NULL AUTO_INCREMENT,
                                      waiter_name VARCHAR(200) NOT NULL,
                                      status      ENUM('active','inactive','on_break') NULL DEFAULT 'active',
                                      created_at  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
                                      updated_at  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS kitchen_order (
                                             id         INT      NOT NULL AUTO_INCREMENT,
                                             order_id   INT      NOT NULL,
                                             status     ENUM('pending','in_progress','done','cancelled') NOT NULL DEFAULT 'pending',
                                             get_time   DATETIME NULL DEFAULT NULL,
                                             end_time   DATETIME NULL DEFAULT NULL,
                                             created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                             PRIMARY KEY (id),
                                             CONSTRAINT fk_kitchen_order_order
                                                 FOREIGN KEY (order_id) REFERENCES orders (id)
                                                     ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS order_assignment (
                                                id               INT      NOT NULL AUTO_INCREMENT,
                                                kitchen_order_id INT      NOT NULL,
                                                waiter_id        INT      NOT NULL,
                                                assigned_at      DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                                                PRIMARY KEY (id),
                                                INDEX idx_kitchen_order_id (kitchen_order_id ASC),
                                                INDEX idx_waiter_id (waiter_id ASC),
                                                CONSTRAINT order_assignment_ibfk_1
                                                    FOREIGN KEY (kitchen_order_id) REFERENCES kitchen_order (id),
                                                CONSTRAINT order_assignment_ibfk_2
                                                    FOREIGN KEY (waiter_id) REFERENCES waiter (id)
);

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