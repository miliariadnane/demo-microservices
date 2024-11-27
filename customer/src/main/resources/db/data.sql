INSERT INTO customer (id, name, email, phone, address)
VALUES (nextval('customer_sequence'), 'John Smith', 'john.smith@gmail.com', '+1234567890', '123 Main St, New York, NY'),
       (nextval('customer_sequence'), 'Emma Wilson', 'emma.wilson@outlook.com', '+1987654321',
        '456 Park Ave, London, UK'),
       (nextval('customer_sequence'), 'Mohammed Ali', 'mohammed.ali@yahoo.com', '+212661234567',
        'Marina Street, Casablanca, Morocco'),
       (nextval('customer_sequence'), 'Sarah Chen', 'sarah.chen@gmail.com', '+8613912345678',
        '789 Nanjing Road, Shanghai, China');
