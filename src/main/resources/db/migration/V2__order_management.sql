-- -------------------------------------------------------------
-- 0. tables
--    No dependencies
-- -------------------------------------------------------------
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

-- -------------------------------------------------------------
-- 1. menu_categories
--    No dependencies
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS menu_categories (
                                               id        INT          NOT NULL AUTO_INCREMENT,
                                               name      VARCHAR(200) NULL DEFAULT NULL,
                                               is_active TINYINT      NOT NULL DEFAULT 1,
                                               PRIMARY KEY (id)
);


-- -------------------------------------------------------------
-- 2. portions
--    No dependencies
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS portions (
                                        id           INT          NOT NULL AUTO_INCREMENT,
                                        portion_name VARCHAR(100) NOT NULL,
                                        PRIMARY KEY (id)
);


-- -------------------------------------------------------------
-- 3. menu_items
--    Depends on : menu_categories
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS menu_items (
                                          id           INT          NOT NULL AUTO_INCREMENT,
                                          category_id  INT          NULL DEFAULT NULL,
                                          name         VARCHAR(200) NULL DEFAULT NULL,
                                          description  TEXT         NULL DEFAULT NULL,
                                          is_available TINYINT      NOT NULL DEFAULT 1,
                                          image_url    VARCHAR(500) NULL DEFAULT NULL,
                                          created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                          PRIMARY KEY (id),
                                          CONSTRAINT fk_menu_items_category
                                              FOREIGN KEY (category_id) REFERENCES menu_categories (id)
                                                  ON DELETE SET NULL ON UPDATE CASCADE
);


-- -------------------------------------------------------------
-- 4. menu_item_price
--    Depends on : menu_items, portions
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS menu_item_price (
                                               id         INT           NOT NULL AUTO_INCREMENT,
                                               item_id    INT           NOT NULL,
                                               portion_id INT           NOT NULL,
                                               price      DECIMAL(10,2) NOT NULL,
                                               is_active  TINYINT       NOT NULL DEFAULT 1,
                                               PRIMARY KEY (id),
                                               UNIQUE KEY uq_item_portion (item_id, portion_id),
                                               CONSTRAINT fk_menu_item_price_item
                                                   FOREIGN KEY (item_id) REFERENCES menu_items (id)
                                                       ON DELETE RESTRICT ON UPDATE CASCADE,
                                               CONSTRAINT fk_menu_item_price_portion
                                                   FOREIGN KEY (portion_id) REFERENCES portions (id)
                                                       ON DELETE RESTRICT ON UPDATE CASCADE
);


-- -------------------------------------------------------------
-- 5. orders
--    Depends on : tables, customers, users
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
                                      id              INT           NOT NULL AUTO_INCREMENT,
                                      order_number    VARCHAR(50)   NULL DEFAULT NULL,
                                      order_type      ENUM('dine_in','takeout','delivery','online') NULL DEFAULT NULL,
                                      table_id        INT           NULL DEFAULT NULL,
                                      customer_id     INT           NULL DEFAULT NULL,
                                      server_id       INT           NULL DEFAULT NULL,
                                      status          ENUM('open','sent_to_kitchen','partially_ready','ready','paid','voided') NOT NULL DEFAULT 'open',
                                      subtotal        DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                                      discount_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                                      tax_amount      DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                                      service_charge  DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                                      total_amount    DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                                      notes           TEXT          NULL DEFAULT NULL,
                                      created_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      updated_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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


-- -------------------------------------------------------------
-- 6. order_items
--    Depends on : orders, menu_items, portions
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS order_items (
                                           id           INT           NOT NULL AUTO_INCREMENT,
                                           order_id     INT           NULL DEFAULT NULL,
                                           menu_item_id INT           NULL DEFAULT NULL,
                                           portion_id   INT           NOT NULL,
                                           quantity     INT           NOT NULL DEFAULT 1,
                                           price        DECIMAL(10,2) NULL DEFAULT NULL,
                                           status       ENUM('pending','fired','ready','served','voided') NOT NULL DEFAULT 'pending',
                                           notes        TEXT          NULL DEFAULT NULL,
                                           created_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           PRIMARY KEY (id),
                                           CONSTRAINT fk_order_items_order
                                               FOREIGN KEY (order_id) REFERENCES orders (id)
                                                   ON DELETE CASCADE ON UPDATE CASCADE,
                                           CONSTRAINT fk_order_items_menu_item
                                               FOREIGN KEY (menu_item_id) REFERENCES menu_items (id)
                                                   ON DELETE RESTRICT ON UPDATE CASCADE,
                                           CONSTRAINT fk_order_items_portion
                                               FOREIGN KEY (portion_id) REFERENCES portions (id)
                                                   ON DELETE RESTRICT ON UPDATE CASCADE
);


-- -------------------------------------------------------------
-- 7. kds_orders
--    Depends on : orders, users
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS kds_orders (
                                          id           INT      NOT NULL AUTO_INCREMENT,
                                          order_id     INT      NULL DEFAULT NULL,
                                          displayed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          bumped_at    DATETIME NULL DEFAULT NULL,
                                          bumped_by    INT      NULL DEFAULT NULL,
                                          is_rush      TINYINT  NOT NULL DEFAULT 0,
                                          is_vip       TINYINT  NOT NULL DEFAULT 0,
                                          color_status ENUM('green','yellow','red') NOT NULL DEFAULT 'green',
                                          PRIMARY KEY (id),
                                          CONSTRAINT fk_kds_orders_order
                                              FOREIGN KEY (order_id) REFERENCES orders (id)
                                                  ON DELETE CASCADE ON UPDATE CASCADE,
                                          CONSTRAINT fk_kds_orders_bumped_by
                                              FOREIGN KEY (bumped_by) REFERENCES users (id)
                                                  ON DELETE SET NULL ON UPDATE CASCADE
);


-- -------------------------------------------------------------
-- 8. kds_order_items
--    Depends on : kds_orders, order_items
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS kds_order_items (
                                               id            INT      NOT NULL AUTO_INCREMENT,
                                               kds_order_id  INT      NULL DEFAULT NULL,
                                               order_item_id INT      NULL DEFAULT NULL,
                                               status        ENUM('pending','in_progress','done') NOT NULL DEFAULT 'pending',
                                               fired_at      DATETIME NULL DEFAULT NULL,
                                               completed_at  DATETIME NULL DEFAULT NULL,
                                               PRIMARY KEY (id),
                                               CONSTRAINT fk_kds_order_items_kds_order
                                                   FOREIGN KEY (kds_order_id) REFERENCES kds_orders (id)
                                                       ON DELETE CASCADE ON UPDATE CASCADE,
                                               CONSTRAINT fk_kds_order_items_order_item
                                                   FOREIGN KEY (order_item_id) REFERENCES order_items (id)
                                                       ON DELETE CASCADE ON UPDATE CASCADE
);


-- -------------------------------------------------------------
-- 9. kitchen_order
--    Depends on : orders
-- -------------------------------------------------------------
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

-- -------------------------------------------------------------
-- 10. order_sequence
--   No dependencies
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS order_sequence (
                                sequence_date DATE PRIMARY KEY,
                                last_sequence INT NOT NULL DEFAULT 0
);