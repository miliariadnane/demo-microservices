INSERT INTO payment (id, customer_id, order_id, create_at, status)
VALUES (nextval('payment_sequence'), 1, 1, CURRENT_TIMESTAMP, 'COMPLETED'),
       (nextval('payment_sequence'), 1, 2, CURRENT_TIMESTAMP, 'COMPLETED'),
       (nextval('payment_sequence'), 2, 3, CURRENT_TIMESTAMP, 'COMPLETED'),
       (nextval('payment_sequence'), 3, 4, CURRENT_TIMESTAMP, 'PENDING'),
       (nextval('payment_sequence'), 4, 5, CURRENT_TIMESTAMP, 'PENDING');
