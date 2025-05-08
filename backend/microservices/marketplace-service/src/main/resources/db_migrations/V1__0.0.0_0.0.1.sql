-- Tworzenie funkcji aktualizującej modified_at
CREATE OR REPLACE FUNCTION set_modified_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.modified_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE SEQUENCE beats_id_seq;

ALTER SEQUENCE beats_id_seq
    OWNER TO admin;

CREATE TABLE beats
(
    id          BIGINT    DEFAULT NEXTVAL('beats_id_seq'::regclass) NOT NULL PRIMARY KEY,
    hash        VARCHAR(255) UNIQUE                                 NOT NULL,
    user_hash   VARCHAR(255)                                        NOT NULL,
    title       VARCHAR(255) UNIQUE                                 NOT NULL,
    description VARCHAR(255),
    bpm         INT,
    genre       VARCHAR(255)                                        NOT NULL,
    mood        VARCHAR(255)                                        NOT NULL,
    visibility  VARCHAR(255)                                        NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE media_files_id_seq;

ALTER SEQUENCE media_files_id_seq
    OWNER TO admin;

CREATE TABLE media_files
(
    id          BIGINT    DEFAULT NEXTVAL('media_files_id_seq'::regclass) PRIMARY KEY,
    beat_id     BIGINT       NOT NULL,
    file_type   VARCHAR(255) NOT NULL,
    file_url    VARCHAR(255) NOT NULL,
    mime_type   VARCHAR(255) NOT NULL,
    file_size   INT          NOT NULL,
    uploaded_at TIMESTAMP    NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_media_file_beat FOREIGN KEY (beat_id) REFERENCES beats (id) ON DELETE CASCADE
);

CREATE SEQUENCE license_limit_config_id_seq;

ALTER SEQUENCE license_limit_config_id_seq
    OWNER TO admin;

CREATE TABLE license_limit_config
(
    id                      BIGINT    DEFAULT NEXTVAL('license_limit_config_id_seq'::regclass) PRIMARY KEY,

    stream_limit            INT,                     -- np. 100000 (liczba streamów)
    physical_sales_limit    INT,                     -- np. 1000 (ile fizycznych kopii można sprzedać)

    allow_mp3_download      BOOLEAN   DEFAULT FALSE, -- czy można pobierać MP3
    allow_wav_download      BOOLEAN   DEFAULT FALSE, -- czy można pobierać WAV
    allow_stems_download    BOOLEAN   DEFAULT FALSE, -- czy można pobrać stems/trackout

    allow_live_performance  BOOLEAN   DEFAULT FALSE, -- czy można używać na płatnych koncertach
    allow_broadcast         BOOLEAN   DEFAULT FALSE, -- np. radio, tv

    allow_yt_monetization   BOOLEAN   DEFAULT FALSE, -- np. na YouTube z adsense
    use_in_paid_ads         BOOLEAN   DEFAULT FALSE, -- czy może być używane w reklamach płatnych
    use_in_video_projects   BOOLEAN   DEFAULT FALSE, -- np. w filmach, YouTube itp.

    license_duration_months INT,                     -- np. 12 miesięcy licencji

    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE licenses_id_seq;

ALTER SEQUENCE licenses_id_seq
    OWNER TO admin;

CREATE TABLE licenses
(
    id                      BIGINT    DEFAULT NEXTVAL('licenses_id_seq'::regclass) PRIMARY KEY,
    hash                    VARCHAR(255)   NOT NULL,
    name                    VARCHAR(255)   NOT NULL,
    default_price           NUMERIC(10, 2) NOT NULL,
    user_hash               VARCHAR(255)   NOT NULL,
    license_limit_config_id BIGINT         NOT NULL,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_license_license_limit_config FOREIGN KEY (license_limit_config_id) REFERENCES license_limit_config (id)
);

CREATE SEQUENCE beat_license_id_seq;

ALTER SEQUENCE beat_license_id_seq
    OWNER TO admin;

CREATE TABLE beat_license
(
    id           BIGINT    DEFAULT NEXTVAL('beat_license_id_seq'::regclass) PRIMARY KEY,
    beat_id      BIGINT NOT NULL,
    license_id   BIGINT NOT NULL,
    custom_price NUMERIC(10, 2),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_beat_license_beat FOREIGN KEY (beat_id) REFERENCES beats (id),
    CONSTRAINT fk_beat_license_license FOREIGN KEY (license_id) REFERENCES licenses (id),

    CONSTRAINT uq_beat_license UNIQUE (beat_id, license_id)
);

-- Dodanie triggera do każdej tabeli
CREATE TRIGGER trigger_beats_modified_at
    BEFORE UPDATE
    ON beats
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();

CREATE TRIGGER trigger_media_files_modified_at
    BEFORE UPDATE
    ON media_files
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();

CREATE TRIGGER trigger_licenses_modified_at
    BEFORE UPDATE
    ON licenses
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();

CREATE TRIGGER trigger_license_limit_config_modified_at
    BEFORE UPDATE
    ON license_limit_config
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();

CREATE TRIGGER trigger_beat_license_modified_at
    BEFORE UPDATE
    ON beat_license
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();