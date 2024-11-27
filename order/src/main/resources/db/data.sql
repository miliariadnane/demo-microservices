INSERT INTO "order" (id, customer_id, product_id, amount, create_at)
VALUES (nextval('order_sequence'), 1, 1, 2, CURRENT_TIMESTAMP),
       (nextval('order_sequence'), 1, 3, 1, CURRENT_TIMESTAMP),
       (nextval('order_sequence'), 2, 2, 3, CURRENT_TIMESTAMP),
       (nextval('order_sequence'), 3, 4, 1, CURRENT_TIMESTAMP),
       (nextval('order_sequence'), 4, 1, 2, CURRENT_TIMESTAMP);
