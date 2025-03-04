-- Tworzenie funkcji aktualizującej modified_at
CREATE OR REPLACE FUNCTION set_modified_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.modified_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TABLE IF NOT EXISTS users
(
    id            BIGINT PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles
(
    id          BIGINT PRIMARY KEY,
    name        VARCHAR(50) UNIQUE NOT NULL,
    is_default  BOOLEAN            NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles
(
    id          BIGINT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    role_id     BIGINT NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);


-- Dodanie triggera do każdej tabeli
CREATE TRIGGER trigger_users_modified_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();

CREATE TRIGGER trigger_roles_modified_at
    BEFORE UPDATE
    ON roles
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();

CREATE TRIGGER trigger_user_roles_modified_at
    BEFORE UPDATE
    ON user_roles
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();