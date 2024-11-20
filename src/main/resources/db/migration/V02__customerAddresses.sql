CREATE TABLE IF NOT EXISTS customer_addresses (
    customer_addresses_id BIGINT PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255),
    zip_code VARCHAR(255) NOT NULL,
    customer_id BIGINT NOT NULL
);

CREATE SEQUENCE customer_addresses_seq;
