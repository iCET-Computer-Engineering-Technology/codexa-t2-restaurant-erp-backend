UPDATE payments
SET payment_method = 'cash'
WHERE payment_method NOT IN ('cash', 'card')
OR payment_method IS NULL;

DELETE FROM payments;

ALTER TABLE payments
MODIFY COLUMN payment_method ENUM('cash', 'card') NOT NULL;

ALTER TABLE payments
ADD CONSTRAINT unique_order_payment
UNIQUE (order_id);

INSERT INTO payments (order_id, payment_method, amount, tip_amount, reference_number, processed_by)
VALUES
    (1, 'card', 1674.75, 100.00, 'TXN-CC-A3F8', 2),
    (2, 'cash', 2511.85, 200.00, 'TXN-CS-B2C1', 2),
    (3, 'card', 727.65,  50.00,  'TXN-CC-D4E9', 1),
    (4, 'cash', 1045.41, 0.00,   'TXN-CS-F5G7', 2),
    (5, 'card', 890.00,  0.00,   'TXN-CC-H6I2', 1);



