-- Sequences
CREATE SEQUENCE IF NOT EXISTS payment_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS payment
(
    id          BIGINT DEFAULT nextval('payment_sequence') PRIMARY KEY,
    customer_id BIGINT      NOT NULL,
    order_id    BIGINT      NOT NULL,
    create_at   TIMESTAMP   NOT NULL,
    status      VARCHAR(50) NOT NULL
);
