CREATE TABLE IF NOT EXISTS customer (
    customer_id BIGINT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone INT NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE SEQUENCE customer_seq;
