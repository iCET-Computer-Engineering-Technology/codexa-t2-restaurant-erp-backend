CREATE TABLE IF NOT EXIST `menu_categories` (
                                                             `id`        INT          NOT NULL AUTO_INCREMENT,
                                                             `name`      VARCHAR(200) NULL DEFAULT NULL,
    `is_active` TINYINT      NULL DEFAULT '1',
    PRIMARY KEY (`id`)
    ) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXIST `menu_items` (
                                                        `id`            INT           NOT NULL AUTO_INCREMENT,
                                                        `category_id`   INT           NULL DEFAULT NULL,
                                                        `name`          VARCHAR(200)  NULL DEFAULT NULL,
    `description`   TEXT          NULL DEFAULT NULL,
    `is_available`  TINYINT       NULL DEFAULT '1',
    `image_url`     VARCHAR(500)  NULL DEFAULT NULL,
    `created_at`    DATETIME      NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    DATETIME      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_menu_items_category`
    FOREIGN KEY (`category_id`) REFERENCES `menu_categories` (`id`)
                                                                 ON DELETE SET NULL ON UPDATE CASCADE
    ) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXIST `portions` (
                                                      `id`           INT          NOT NULL AUTO_INCREMENT,
                                                      `portion_name` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXIST `menu_item_price` (
                                                             `id`        INT           NOT NULL AUTO_INCREMENT,
                                                             `item_id`   INT           NOT NULL,
                                                             `portion_id` INT          NOT NULL,
                                                             `price`     DECIMAL(10,2) NOT NULL,
    `is_active` TINYINT       NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    INDEX `item_id` (`item_id` ASC),
    INDEX `portion_id` (`portion_id` ASC),
    CONSTRAINT `menu_item_price_ibfk_1`
    FOREIGN KEY (`item_id`) REFERENCES `menu_items` (`id`),
    CONSTRAINT `menu_item_price_ibfk_2`
    FOREIGN KEY (`portion_id`) REFERENCES `portions` (`id`)
    ) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
