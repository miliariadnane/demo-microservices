-- Sequences
CREATE SEQUENCE IF NOT EXISTS product_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS product
(
    id                 BIGINT           DEFAULT nextval('product_sequence') PRIMARY KEY,
    name               TEXT    NOT NULL,
    image              TEXT,
    price              INTEGER NOT NULL,
    available_quantity INTEGER NOT NULL DEFAULT 0
);
