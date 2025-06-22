-- Sequences
CREATE SEQUENCE IF NOT EXISTS order_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS "order"
(
    id          BIGINT DEFAULT nextval('order_sequence') PRIMARY KEY,
    customer_id BIGINT           NOT NULL,
    product_id  BIGINT           NOT NULL,
    amount      DOUBLE PRECISION NOT NULL,
    create_at   TIMESTAMP        NOT NULL
);
