CREATE TABLE IF NOT EXISTS waiters
(
    id
    BIGINT
    NOT
    NULL
    AUTO_INCREMENT,
    name
    VARCHAR
(
    100
) NOT NULL,
    status ENUM
(
    'ACTIVE',
    'INACTIVE'
) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY
(
    id
)
    );