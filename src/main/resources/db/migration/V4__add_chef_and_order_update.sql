-- Create chef table
CREATE TABLE chef (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    availability ENUM('available','busy','offline') DEFAULT 'available',
    current_task_load INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Add chef_id to orders
ALTER TABLE orders ADD chef_id INT;

-- Foreign key
ALTER TABLE orders
    ADD CONSTRAINT fk_orders_chef
        FOREIGN KEY (chef_id) REFERENCES chef(id);