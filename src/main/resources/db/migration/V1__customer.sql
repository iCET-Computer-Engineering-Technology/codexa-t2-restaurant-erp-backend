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

CREATE TABLE customer_segments (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   segment_name VARCHAR(100),
                                   criteria_json JSON,
                                   auto_segment TINYINT DEFAULT 1,
                                   created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customer_segment_members (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          segment_id INT,
                                          customer_id INT,
                                          assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE marketing_campaigns (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     campaign_name VARCHAR(255),
                                     segment_id INT,
                                     channel ENUM('email', 'sms'),
                                     subject VARCHAR(255),
                                     body_template TEXT,
                                     ab_test_enabled TINYINT DEFAULT 0,
                                     variant_b_body TEXT,
                                     scheduled_at DATETIME,
                                     sent_at DATETIME,
                                     status ENUM('draft', 'scheduled', 'sent', 'cancelled') DEFAULT 'draft',
                                     created_by INT,
                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE automated_messages (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    trigger_type ENUM('birthday', 'anniversary', 'lapsed', 'tier_change'),
                                    channel ENUM('email', 'sms'),
                                    template_body TEXT,
                                    offer_type ENUM('discount', 'free_item', 'none'),
                                    offer_value DECIMAL(10, 2),
                                    send_days_before INT DEFAULT 1,
                                    is_active TINYINT DEFAULT 1,
                                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE campaign_analytics (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    campaign_id INT,
                                    customer_id INT,
                                    sent_at DATETIME,
                                    opened_at DATETIME,
                                    clicked_at DATETIME,
                                    converted_at DATETIME,
                                    unsubscribed_at DATETIME,
                                    variant CHAR(1)
);
