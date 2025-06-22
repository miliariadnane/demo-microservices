-- Sequences
CREATE SEQUENCE IF NOT EXISTS customer_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS customer
(
    id      BIGINT DEFAULT nextval('customer_sequence') PRIMARY KEY,
    name    TEXT NOT NULL,
    email   TEXT NOT NULL UNIQUE,
    phone   TEXT,
    address TEXT
);
