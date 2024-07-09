DROP TABLE IF EXISTS orders cascade;
DROP TABLE IF EXISTS order_item cascade;

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    biling_address VARCHAR(225) NOT NULL,
    customer_id BIGINT NOT NULL,
    order_date  DATE NOT NULL,
    order_status VARCHAR(225) NOT NULL,
    payment_method VARCHAR(225) NOT NULL,
    shipping_address VARCHAR(225) NOT NULL,
    total_amount DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS order_item (
    id BIGSERIAL PRIMARY KEY,
    price DOUBLE PRECISION NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    order_id BIGINT NOT NULL,
    CONSTRAINT fkOrder
        FOREIGN KEY (order_id)
            REFERENCES orders(id)
);