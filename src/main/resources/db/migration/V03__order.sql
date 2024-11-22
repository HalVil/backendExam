CREATE TABLE IF NOT EXISTS candy_order (
    candy_order_id BIGINT PRIMARY KEY,
    shipping_charge DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    shipped BOOLEAN NOT NULL,
    customer_id BIGINT NOT NULL,
    shipping_address_id BIGINT NOT NULL
);

CREATE SEQUENCE candy_order_seq;

CREATE TABLE IF NOT EXISTS order_product (
    candy_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (candy_order_id, product_id)
);

CREATE SEQUENCE order_product_seq;