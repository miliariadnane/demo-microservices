-- Sequences
CREATE SEQUENCE IF NOT EXISTS api_key_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS application_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS api_keys
(
    id              BIGINT                       DEFAULT nextval('api_key_sequence') PRIMARY KEY,
    key VARCHAR (255) UNIQUE NOT NULL,
    client          VARCHAR(255) UNIQUE NOT NULL,
    description     TEXT,
    created_date    TIMESTAMP,
    expiration_date TIMESTAMP,
    enabled         BOOLEAN             NOT NULL DEFAULT FALSE,
    never_expires   BOOLEAN             NOT NULL DEFAULT FALSE,
    approved        BOOLEAN             NOT NULL DEFAULT FALSE,
    revoked         BOOLEAN             NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS applications
(
    id               INT                   DEFAULT nextval('application_sequence') PRIMARY KEY,
    application_name VARCHAR(255) NOT NULL,
    enabled          BOOLEAN      NOT NULL DEFAULT FALSE,
    approved         BOOLEAN      NOT NULL DEFAULT FALSE,
    revoked          BOOLEAN      NOT NULL DEFAULT FALSE,
    api_key_id       BIGINT       NOT NULL,
    CONSTRAINT fk_api_key
        FOREIGN KEY (api_key_id)
            REFERENCES api_keys (id)
            ON DELETE CASCADE
);
