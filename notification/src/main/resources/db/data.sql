INSERT INTO notification (id, customer_id, customer_name, customer_email, sender, message, sent_at)
VALUES (nextval('notification_sequence'), 1, 'John Smith', 'john.smith@gmail.com', 'NanoShop',
        'Your order #1 has been confirmed', CURRENT_TIMESTAMP),
       (nextval('notification_sequence'), 1, 'John Smith', 'john.smith@gmail.com', 'NanoShop',
        'Your payment for order #1 has been processed', CURRENT_TIMESTAMP),
       (nextval('notification_sequence'), 2, 'Emma Wilson', 'emma.wilson@outlook.com', 'NanoShop',
        'Your order #3 has been shipped', CURRENT_TIMESTAMP),
       (nextval('notification_sequence'), 3, 'Mohammed Ali', 'mohammed.ali@yahoo.com', 'NanoShop',
        'Payment reminder for order #4', CURRENT_TIMESTAMP),
       (nextval('notification_sequence'), 4, 'Sarah Chen', 'sarah.chen@gmail.com', 'NanoShop',
        'Thank you for your purchase! Order #5 confirmed', CURRENT_TIMESTAMP);
