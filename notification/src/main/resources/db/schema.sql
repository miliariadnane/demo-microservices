-- Sequences
CREATE SEQUENCE IF NOT EXISTS notification_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS notification
(
    id             BIGINT DEFAULT nextval('notification_sequence') PRIMARY KEY,
    customer_id    BIGINT    NOT NULL,
    customer_name  TEXT,
    customer_email TEXT,
    sender         TEXT      NOT NULL,
    message        TEXT      NOT NULL,
    sent_at        TIMESTAMP NOT NULL
);
