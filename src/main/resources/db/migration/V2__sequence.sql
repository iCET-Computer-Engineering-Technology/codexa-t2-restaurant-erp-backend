CREATE TABLE order_sequence (
                                sequence_date DATE    PRIMARY KEY,
                                last_sequence INT     NOT NULL DEFAULT 0
);