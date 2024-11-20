CREATE TABLE IF NOT EXISTS product (
    product_id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE', 'OUT_OF_STOCK')),
    quantity_on_hand INT NOT NULL
);

CREATE SEQUENCE product_seq;
