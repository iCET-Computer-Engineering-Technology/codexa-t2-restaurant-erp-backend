ALTER TABLE order_assignments MODIFY COLUMN status VARCHAR(255) NOT NULL DEFAULT 'UNSERVED';

UPDATE order_assignments SET status = 'UNSERVED' WHERE status = 'PENDING';
