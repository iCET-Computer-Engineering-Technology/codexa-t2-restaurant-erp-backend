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
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(30) UNIQUE,
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

CREATE TABLE customer_visits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    visit_date DATETIME,
    order_id INT,
    spend_amount DECIMAL(10, 2),
    notes TEXT
);

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50),
    order_type ENUM('dine_in', 'takeout', 'delivery', 'online'),
    table_id INT,
    customer_id INT,
    server_id INT,
    status ENUM('open','sent_to_kitchen','partially_ready','ready','paid','voided') DEFAULT 'open',
    subtotal DECIMAL(10, 2) DEFAULT 0,
    discount_amount DECIMAL(10, 2) DEFAULT 0,
    tax_amount DECIMAL(10, 2) DEFAULT 0,
    total_amount DECIMAL(10, 2) DEFAULT 0,
    notes TEXT,
    source VARCHAR(50),
    external_order_id VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE loyalty_accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    points_balance INT DEFAULT 0,
    tier_id INT,
    lifetime_points INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);