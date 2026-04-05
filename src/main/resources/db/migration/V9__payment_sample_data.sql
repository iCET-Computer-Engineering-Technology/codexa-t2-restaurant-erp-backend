-- Only insert if order and user exist (safe inserts)
INSERT INTO payments (order_id, payment_method, amount, tip_amount, reference_number, processed_by)
SELECT 1, 'card', 1674.75, 100.00, 'TXN-CC-A3F8', 2
    WHERE EXISTS (SELECT 1 FROM orders WHERE id = 1)
  AND NOT EXISTS (SELECT 1 FROM payments WHERE order_id = 1);

INSERT INTO payments (order_id, payment_method, amount, tip_amount, reference_number, processed_by)
SELECT 2, 'cash', 2511.85, 200.00, 'TXN-CA-B2C1', 2
    WHERE EXISTS (SELECT 1 FROM orders WHERE id = 2)
  AND NOT EXISTS (SELECT 1 FROM payments WHERE order_id = 2);

INSERT INTO payments (order_id, payment_method, amount, tip_amount, reference_number, processed_by)
SELECT 3, 'card', 727.65, 50.00, 'TXN-CC-D4E9', 1
    WHERE EXISTS (SELECT 1 FROM orders WHERE id = 3)
  AND NOT EXISTS (SELECT 1 FROM payments WHERE order_id = 3);

INSERT INTO payments (order_id, payment_method, amount, tip_amount, reference_number, processed_by)
SELECT 4, 'cash', 1045.41, 0.00, 'TXN-CA-F5G7', 2
    WHERE EXISTS (SELECT 1 FROM orders WHERE id = 4)
  AND NOT EXISTS (SELECT 1 FROM payments WHERE order_id = 4);

INSERT INTO payments (order_id, payment_method, amount, tip_amount, reference_number, processed_by)
SELECT 5, 'card', 890.00, 0.00, 'TXN-CC-H6I2', 1
    WHERE EXISTS (SELECT 1 FROM orders WHERE id = 5)
  AND NOT EXISTS (SELECT 1 FROM payments WHERE order_id = 5);