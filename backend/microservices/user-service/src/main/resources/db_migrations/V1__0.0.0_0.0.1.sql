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

CREATE TABLE IF NOT EXISTS user_account
(
    id            BIGSERIAL PRIMARY KEY,
    user_hash     VARCHAR(255) UNIQUE NOT NULL,
    username      VARCHAR(255) UNIQUE,
    email         VARCHAR(255)        NOT NULL UNIQUE,
    password_hash VARCHAR(255),
    first_name    VARCHAR(255)        NOT NULL,
    last_name     VARCHAR(255)        NOT NULL,
    role_id       BIGINT              NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role (id)
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