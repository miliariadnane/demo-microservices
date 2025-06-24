-- First insert API Keys and store their IDs
INSERT INTO api_keys (id, key, client, description, created_date, expiration_date, enabled, never_expires, approved,
                      revoked)
SELECT nextval('api_key_sequence'), t.key, t.client, t.description, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '365 days', t.enabled, t.never_expires, t.approved, t.revoked
FROM (
         VALUES
             ('ecom-frontend-key-2024', 'E-commerce Frontend', 'API Key for frontend application', true, false, true, false),
             ('mobile-app-key-2024', 'Mobile Application', 'API Key for mobile app', true, false, true, false),
             ('admin-dashboard-key-2024', 'Admin Dashboard', 'API Key for admin dashboard', true, true, true, false),
             -- Internal traffic key (never exposed publicly)
             ('internal-service-key', 'Internal Service', 'API Key for inter-service calls', true, true, true, false)
     ) AS t(key, client, description, enabled, never_expires, approved, revoked);

-- Then insert Applications using the actual API key IDs by looking up the key value
INSERT INTO applications (id, application_name, enabled, approved, revoked, api_key_id)
SELECT nextval('application_sequence'),
       t.app_name,
       true,
       true,
       false,
       (SELECT ak.id FROM api_keys ak WHERE ak.key = t.api_key_name)
FROM (VALUES ('CUSTOMER', 'ecom-frontend-key-2024'),
             ('CUSTOMER', 'mobile-app-key-2024'),
             ('CUSTOMER', 'admin-dashboard-key-2024'),
             ('PRODUCT', 'ecom-frontend-key-2024'),
             ('PRODUCT', 'mobile-app-key-2024'),
             ('PRODUCT', 'admin-dashboard-key-2024'),
             ('ORDER', 'ecom-frontend-key-2024'),
             ('ORDER', 'mobile-app-key-2024'),
             ('ORDER', 'admin-dashboard-key-2024'),
             ('PAYMENT', 'ecom-frontend-key-2024'),
             ('PAYMENT', 'admin-dashboard-key-2024'),
             ('NOTIFICATION', 'admin-dashboard-key-2024'),
             -- Grant the internal-service-key to every microservice that consumes internal APIs
             ('CUSTOMER', 'internal-service-key'),
             ('PRODUCT', 'internal-service-key'),
             ('ORDER', 'internal-service-key'), -- This line specifically authorizes 'internal-service-key' for the 'ORDER' application
             ('PAYMENT', 'internal-service-key'),
             ('NOTIFICATION', 'internal-service-key'),
             ('APIKEY_MANAGER', 'internal-service-key')
     ) AS t(app_name, api_key_name);
