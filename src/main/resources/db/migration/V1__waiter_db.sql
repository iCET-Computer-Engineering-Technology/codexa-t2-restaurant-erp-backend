CREATE TABLE IF NOT EXISTS waiter (
                        id          INT          NOT NULL AUTO_INCREMENT,
                        waiter_name VARCHAR(200) NOT NULL,
                        status      ENUM('active','inactive','on_break') NULL DEFAULT 'active',
                        created_at  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (id)
);
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(100),
                       enabled BOOLEAN DEFAULT 1,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customers (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        first_name VARCHAR(100),
                        last_name VARCHAR(100),
                        email VARCHAR(255),
                        phone VARCHAR(30),
                        preferred_language VARCHAR(10) DEFAULT 'en',
                        dietary_notes TEXT,
                        communication_email TINYINT DEFAULT 1,
                        communication_sms TINYINT DEFAULT 1,
                        gdpr_deleted TINYINT DEFAULT 0,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        birthday DATE,
                        loyalty_points INT DEFAULT 0 NOT NULL
);
CREATE TABLE IF NOT EXISTS tables (
                        id           INT         NOT NULL AUTO_INCREMENT,
                        section_id   INT         NULL DEFAULT NULL,
                        table_number VARCHAR(20) NULL DEFAULT NULL,
                        capacity     INT         NULL DEFAULT NULL,
                        pos_x        INT         NULL DEFAULT NULL,
                        pos_y        INT         NULL DEFAULT NULL,
                        status       ENUM('available','occupied','reserved','cleaning') NULL DEFAULT 'available',
                        updated_at   DATETIME    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS  orders (
                        id             INT           NOT NULL AUTO_INCREMENT,
                        order_number    VARCHAR(50)   NULL DEFAULT NULL,
                        order_type      ENUM('dine_in','takeout','delivery','online') NULL DEFAULT NULL,
                        table_id        INT           NULL DEFAULT NULL,
                        customer_id     INT           NULL DEFAULT NULL,
                        server_id       INT           NULL DEFAULT NULL,
                        status          ENUM('open','sent_to_kitchen','partially_ready','ready','paid','voided') NULL DEFAULT 'open',
                        subtotal        DECIMAL(10,2) NULL DEFAULT '0.00',
                        discount_amount DECIMAL(10,2) NULL DEFAULT '0.00',
                        tax_amount      DECIMAL(10,2) NULL DEFAULT '0.00',
                        service_charge  DECIMAL(10,2) NULL DEFAULT '0.00',
                        total_amount    DECIMAL(10,2) NULL DEFAULT '0.00',
                        notes           TEXT          NULL DEFAULT NULL,
                        created_at      DATETIME      NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at      DATETIME      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (id),
                        CONSTRAINT fk_orders_table
                        FOREIGN KEY (table_id) REFERENCES tables (id)
                        ON DELETE SET NULL ON UPDATE CASCADE,
                        CONSTRAINT fk_orders_customer
                        FOREIGN KEY (customer_id) REFERENCES customers (id)
                        ON DELETE SET NULL ON UPDATE CASCADE,
                        CONSTRAINT fk_orders_server
                        FOREIGN KEY (server_id) REFERENCES users (id)
                        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS order_status_updates (
                        id         INT      NOT NULL AUTO_INCREMENT,
                        order_id   INT      NOT NULL,
                        waiter_id  INT      NOT NULL,
                        status     ENUM('served','unserved') NOT NULL,
                        updated_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (id),
                        CONSTRAINT fk_osu_order
                        FOREIGN KEY (order_id) REFERENCES orders (id)
                        ON DELETE CASCADE ON UPDATE CASCADE,
                        CONSTRAINT fk_osu_waiter
                        FOREIGN KEY (waiter_id) REFERENCES waiter (id)
                        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS order_assignment (
                        id               INT      PRIMARY KEY AUTO_INCREMENT,
                        kitchen_order_id INT      NOT NULL,
                        waiter_id        INT      NOT NULL,
                        assigned_at      DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (id),
                        INDEX kitchen_order_id (kitchen_order_id ASC),
                        INDEX waiter_id (waiter_id ASC),
                        CONSTRAINT order_assignment_ibfk_1
                        FOREIGN KEY (kitchen_order_id) REFERENCES kitchen_order (id),
                        CONSTRAINT order_assignment_ibfk_2
                        FOREIGN KEY (waiter_id) REFERENCES waiter (id)
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



