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