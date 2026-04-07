UPDATE payments
SET payment_method = 'cash'
WHERE payment_method NOT IN ('cash', 'card')
OR payment_method IS NULL;

DELETE p1 FROM payments p1
INNER JOIN payments p2
WHERE p1.order_id = p2.order_id
AND p1.id > p2.id;

ALTER TABLE payments
MODIFY COLUMN payment_method ENUM('cash', 'card') NOT NULL;

ALTER TABLE payments
ADD CONSTRAINT unique_order_payment
UNIQUE (order_id);




