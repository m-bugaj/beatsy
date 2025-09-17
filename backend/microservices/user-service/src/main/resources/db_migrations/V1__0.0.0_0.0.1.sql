-- Tworzenie funkcji aktualizującej modified_at
CREATE OR REPLACE FUNCTION set_modified_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.modified_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE IF NOT EXISTS role
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) UNIQUE NOT NULL,
    is_default  BOOLEAN            NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE user_account_id_seq;

ALTER SEQUENCE user_account_id_seq
    OWNER TO admin;

CREATE TABLE IF NOT EXISTS user_account
(
    id            BIGINT    DEFAULT NEXTVAL('user_account_id_seq'::regclass) PRIMARY KEY,
    user_hash     VARCHAR(255) UNIQUE NOT NULL,
    username      VARCHAR(255) UNIQUE,
    email         VARCHAR(255)        NOT NULL UNIQUE,
    password_hash VARCHAR(255),
    first_name    VARCHAR(255)        NOT NULL,
    last_name     VARCHAR(255)        NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE user_role_id_seq;

ALTER SEQUENCE user_role_id_seq
    OWNER TO admin;

CREATE TABLE IF NOT EXISTS user_role
(
    id            BIGINT    DEFAULT NEXTVAL('user_role_id_seq'::regclass) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at    TIMESTAMP DEFAULT now(),
    modified_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES user_account (id),
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE IF NOT EXISTS refresh_token
(
    id            BIGSERIAL PRIMARY KEY,
    token           VARCHAR(255) UNIQUE,
    expires         TIMESTAMP NOT NULL UNIQUE,
    user_account_id    BIGSERIAL NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_account_id) REFERENCES user_account(id) ON DELETE CASCADE
);

CREATE SEQUENCE user_session_id_seq;

ALTER SEQUENCE user_session_id_seq
    OWNER TO admin;

CREATE TABLE user_session
(
    id                BIGINT    DEFAULT NEXTVAL('user_session_id_seq'::regclass) PRIMARY KEY,
    user_id           BIGINT                              NOT NULL,
    user_hash         VARCHAR(255)                        NOT NULL,
    subscription_hash VARCHAR(255),
    ip_address        VARCHAR(255),
    user_agent        VARCHAR(512),
    last_activity     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    expires_at        TIMESTAMP                           NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES user_account (id)
        ON DELETE CASCADE,
    CONSTRAINT unique_user UNIQUE (user_id)
);

-- Dodanie triggera do każdej tabeli
CREATE TRIGGER trigger_user_account_modified_at
    BEFORE UPDATE
    ON user_account
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();

CREATE TRIGGER trigger_refresh_token_modified_at
    BEFORE UPDATE
    ON refresh_token
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();

CREATE TRIGGER trigger_role_modified_at
    BEFORE UPDATE
    ON role
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();

CREATE TRIGGER trigger_user_session_modified_at
    BEFORE UPDATE
    ON user_session
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();