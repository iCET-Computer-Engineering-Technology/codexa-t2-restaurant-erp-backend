CREATE TABLE category (
                          category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          category_name VARCHAR(45) NOT NULL UNIQUE
);

CREATE TABLE modifiers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT,
    name VARCHAR(200),
    price_adjustment DECIMAL(10, 2) DEFAULT 0,
    is_active TINYINT DEFAULT 1
);


CREATE TABLE menu_item_modifier_groups (
    id INT AUTO_INCREMENT PRIMARY KEY,
    menu_item_id INT,
    modifier_group_id INT,
    sort_order INT DEFAULT 0
);
