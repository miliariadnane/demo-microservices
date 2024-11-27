INSERT INTO payment (id, customer_id, order_id, create_at)
VALUES (nextval('payment_sequence'), 1, 1, CURRENT_TIMESTAMP),
       (nextval('payment_sequence'), 1, 2, CURRENT_TIMESTAMP),
       (nextval('payment_sequence'), 2, 3, CURRENT_TIMESTAMP),
       (nextval('payment_sequence'), 3, 4, CURRENT_TIMESTAMP),
       (nextval('payment_sequence'), 4, 5, CURRENT_TIMESTAMP);
