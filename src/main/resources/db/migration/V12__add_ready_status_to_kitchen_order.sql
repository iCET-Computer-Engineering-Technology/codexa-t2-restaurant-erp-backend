-- Allow kitchen orders to transition to 'ready' before final completion.
ALTER TABLE kitchen_order
    MODIFY COLUMN status ENUM('pending','in_progress','ready','done','cancelled') NOT NULL DEFAULT 'pending';

