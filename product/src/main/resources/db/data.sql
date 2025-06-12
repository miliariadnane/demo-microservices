INSERT INTO product (id, name, image, price, available_quantity)
VALUES
    (nextval('product_sequence'), 'MacBook Pro M3', 'https://picsum.photos/id/1/200/200', 1999, 50),
    (nextval('product_sequence'), 'iPhone 15 Pro', 'https://picsum.photos/id/2/200/200', 1299, 100),
    (nextval('product_sequence'), 'AirPods Pro', 'https://picsum.photos/id/3/200/200', 249, 200),
    (nextval('product_sequence'), 'iPad Air', 'https://picsum.photos/id/4/200/200', 599, 75),
    (nextval('product_sequence'), 'Apple Watch Series 9', 'https://picsum.photos/id/5/200/200', 499, 120);
