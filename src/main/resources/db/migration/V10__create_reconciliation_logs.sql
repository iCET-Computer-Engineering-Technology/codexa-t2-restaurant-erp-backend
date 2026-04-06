CREATE TABLE IF NOT EXISTS reconciliation_logs (
    id INT NOT NULL AUTO_INCREMENT,
    recon_date DATE NOT NULL,
    total_order_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_payment_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    discrepancy DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_reconciliation_logs_recon_date (recon_date)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

