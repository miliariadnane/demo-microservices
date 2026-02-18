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

-- CQRS read model
CREATE TABLE IF NOT EXISTS order_view
(
    order_id       BIGINT           PRIMARY KEY,
    customer_id    BIGINT           NOT NULL,
    customer_name  TEXT,
    customer_email TEXT,
    product_id     BIGINT           NOT NULL,
    product_name   TEXT,
    product_price  DOUBLE PRECISION NOT NULL DEFAULT 0,
    amount         DOUBLE PRECISION NOT NULL,
    created_at     TIMESTAMP
);
