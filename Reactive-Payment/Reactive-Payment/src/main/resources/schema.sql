DROP TABLE transaction_details;
DROP TABLE balance;

CREATE TABLE IF NOT EXISTS transaction_details (
    id BIGSERIAL PRIMARY KEY,
    amount DOUBLE PRECISION NOT NULL,
    order_id BIGINT NOT NULL,
    payment_date TIMESTAMP(6) NOT NULL,
    mode VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    reference_number VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS balance (
    id BIGSERIAL PRIMARY KEY,
    amount DOUBLE PRECISION NOT NULL,
    customer_id BIGINT NOT NULL
);