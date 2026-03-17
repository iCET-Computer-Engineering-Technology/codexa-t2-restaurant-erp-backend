CREATE TABLE marketing_campaigns (
    id INT AUTO_INCREMENT PRIMARY KEY,
    campaign_name VARCHAR(255),
    segment_id INT,
    channel
        SET
        ('email', 'sms'),
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