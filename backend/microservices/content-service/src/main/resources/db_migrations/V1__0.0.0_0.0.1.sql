-- Tworzenie funkcji aktualizującej modified_at
CREATE OR REPLACE FUNCTION set_modified_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.modified_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE SEQUENCE content_id_seq;

ALTER SEQUENCE content_id_seq
    OWNER TO admin;

CREATE TABLE content
(
    id          BIGINT    DEFAULT NEXTVAL('content_id_seq'::regclass) NOT NULL PRIMARY KEY,
    hash        VARCHAR(255) UNIQUE                                   NOT NULL,
    user_hash   VARCHAR(255)                                          NOT NULL,
    type        VARCHAR(255)                                          NOT NULL,
    title       VARCHAR(255) UNIQUE                                   NOT NULL,
    description VARCHAR(255),
    visibility  VARCHAR(255)                                          NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE beat_details
(
    content_id BIGINT PRIMARY KEY,
    bpm        INTEGER,
    key        VARCHAR(10),
    mood       VARCHAR(255) NOT NULL,
    CONSTRAINT fk_beat_details_content
        FOREIGN KEY (content_id)
            REFERENCES content (id)
            ON DELETE CASCADE
);

CREATE SEQUENCE genres_id_seq;

ALTER SEQUENCE genres_id_seq
    OWNER TO admin;

CREATE TABLE genres
(
    id   BIGINT DEFAULT NEXTVAL('genres_id_seq'::regclass) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO genres (name)
VALUES ('HIP_HOP'),
       ('TRAP'),
       ('LOFI'),
       ('RNB'),
       ('POP'),
       ('EDM'),
       ('HOUSE'),
       ('DRILL'),
       ('REGGAETON'),
       ('DANCEHALL');

-- CREATE TABLE beat_details
-- (
--     id          BIGINT    DEFAULT NEXTVAL('beats_id_seq'::regclass) NOT NULL PRIMARY KEY,
--     hash        VARCHAR(255) UNIQUE                                 NOT NULL,
--     user_hash   VARCHAR(255)                                        NOT NULL,
--     title       VARCHAR(255) UNIQUE                                 NOT NULL,
--     description VARCHAR(255),
--     bpm         INT,
--     mood        VARCHAR(255)                                        NOT NULL,
--     visibility  VARCHAR(255)                                        NOT NULL,
--     created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );



-- Dodanie triggera do każdej tabeli
CREATE TRIGGER trigger_content_modified_at
    BEFORE UPDATE
    ON content
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();
