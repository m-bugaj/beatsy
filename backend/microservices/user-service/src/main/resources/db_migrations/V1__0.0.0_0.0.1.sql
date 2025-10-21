-- Tworzenie funkcji aktualizującej modified_at
CREATE OR REPLACE FUNCTION set_modified_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.modified_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE SEQUENCE users_profile_id_seq;

ALTER SEQUENCE users_profile_id_seq
    OWNER TO admin;

CREATE TABLE IF NOT EXISTS users_profile
(
    id            BIGINT    DEFAULT NEXTVAL('users_profile_id_seq'::regclass) PRIMARY KEY,
    user_hash     VARCHAR(255) UNIQUE NOT NULL,
    display_name      VARCHAR(255) UNIQUE,
    bio TEXT,
    location VARCHAR(255),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER trigger_users_profile_modified_at
    BEFORE UPDATE
    ON users_profile
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();