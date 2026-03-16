

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


CREATE TABLE menu_item_variants (
                                    variant_id INT AUTO_INCREMENT PRIMARY KEY,
                                    menu_item_id INT NOT NULL,
                                    portion_size_id INT NOT NULL,
                                    price DECIMAL(10, 2) NOT NULL,
                                    prep_time_minutes INT NOT NULL,
                                    is_available BOOLEAN DEFAULT TRUE,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                                    UNIQUE KEY unique_menu_size (menu_item_id, portion_size_id),

                                    FOREIGN KEY (menu_item_id) REFERENCES menu_items(menu_item_id) ON DELETE CASCADE,
                                    FOREIGN KEY (portion_size_id) REFERENCES portion_sizes(portion_size_id),

                                    CONSTRAINT chk_price_positive CHECK (price > 0),
                                    CONSTRAINT chk_prep_time_positive CHECK (prep_time_minutes > 0),

                                    INDEX idx_menu_item_variants_menu_item_id (menu_item_id),
                                    INDEX idx_menu_item_variants_is_available (is_available)
);