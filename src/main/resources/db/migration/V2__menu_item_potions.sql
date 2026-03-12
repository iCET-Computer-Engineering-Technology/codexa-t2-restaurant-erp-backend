CREATE TABLE menu_items (
                            menu_item_id INT AUTO_INCREMENT PRIMARY KEY,
                            item_name    VARCHAR(100) NOT NULL,
                            description  TEXT,
                            category     VARCHAR(50)  NOT NULL,
                            is_active    BOOLEAN   DEFAULT TRUE,
                            created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                            INDEX        idx_menu_items_is_active (is_active),
                            INDEX        idx_menu_items_category (category),
                            INDEX        idx_menu_items_name (item_name)
);

CREATE TABLE portion_sizes (
                               portion_size_id INT AUTO_INCREMENT PRIMARY KEY,
                               size_name VARCHAR(20) NOT NULL UNIQUE,
                               description VARCHAR(100),
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                               INDEX idx_portion_sizes_name (size_name)
);