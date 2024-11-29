-- First insert API Keys and store their IDs
INSERT INTO api_keys (id, key, client, description, created_date, expiration_date, enabled, never_expires, approved,
                      revoked)
SELECT nextval('api_key_sequence'), key, client, description, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '365 days', enabled, never_expires, approved, revoked
FROM (
    VALUES
    ('ecom-frontend-key-2024', 'E-commerce Frontend', 'API Key for frontend application', true, false, true, false),
    ('mobile-app-key-2024', 'Mobile Application', 'API Key for mobile app', true, false, true, false),
    ('admin-dashboard-key-2024', 'Admin Dashboard', 'API Key for admin dashboard', true, true, true, false)
    ) AS t(key, client, description, enabled, never_expires, approved, revoked);

-- Then insert Applications using the actual API key IDs
INSERT INTO applications (id, application_name, enabled, approved, revoked, api_key_id)
SELECT nextval('application_sequence'),
       app_name,
       true,
       true,
       false,
       ak.id
FROM (VALUES ('CUSTOMER', 1), ('CUSTOMER', 2), ('CUSTOMER', 3),
             ('PRODUCT', 1), ('PRODUCT', 2), ('PRODUCT', 3),
             ('ORDER', 1), ('ORDER', 2), ('ORDER', 3),
             ('PAYMENT', 1), ('PAYMENT', 3),
             ('NOTIFICATION', 3)
      ) AS t(app_name, key_order)
      JOIN api_keys ak ON ak.id = (SELECT id FROM api_keys ORDER BY id LIMIT 1 OFFSET (t.key_order - 1));
