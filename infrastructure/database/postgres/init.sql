CREATE DATABASE user_service;
CREATE DATABASE marketplace_service;

-- Tworzymy użytkowników z prostymi hasłami
CREATE USER root WITH PASSWORD 'root';
CREATE USER readonly_user WITH PASSWORD 'password';

-- Przyznajemy pełne uprawnienia użytkownikowi root
GRANT ALL PRIVILEGES ON DATABASE dev_beatsy TO root;
GRANT ALL PRIVILEGES ON DATABASE user_service TO root;
GRANT ALL PRIVILEGES ON DATABASE marketplace_service TO root;

-- Przyznajemy tylko uprawnienia do odczytu użytkownikowi readonly_user
GRANT CONNECT ON DATABASE dev_beatsy TO readonly_user;
GRANT USAGE ON SCHEMA public TO readonly_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly_user;

GRANT CONNECT ON DATABASE user_service TO readonly_user;
GRANT USAGE ON SCHEMA public TO readonly_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly_user;

GRANT CONNECT ON DATABASE marketplace_service TO readonly_user;
GRANT USAGE ON SCHEMA public TO readonly_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly_user;
