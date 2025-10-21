CREATE DATABASE auth_db;
CREATE DATABASE user_db;
CREATE DATABASE marketplace_service;
CREATE DATABASE api_gateway;

-- Tworzymy użytkowników z prostymi hasłami
CREATE USER root WITH PASSWORD 'root';
CREATE USER readonly_user WITH PASSWORD 'password';

-- Przyznajemy pełne uprawnienia użytkownikowi root
GRANT ALL PRIVILEGES ON DATABASE dev_beatsy TO root;
GRANT ALL PRIVILEGES ON DATABASE auth_db TO root;
GRANT ALL PRIVILEGES ON DATABASE user_db TO root;
GRANT ALL PRIVILEGES ON DATABASE marketplace_service TO root;
GRANT ALL PRIVILEGES ON DATABASE api_gateway TO root;

-- Przyznajemy tylko uprawnienia do odczytu użytkownikowi readonly_user
GRANT CONNECT ON DATABASE dev_beatsy TO readonly_user;
GRANT USAGE ON SCHEMA public TO readonly_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly_user;

GRANT CONNECT ON DATABASE auth_db TO readonly_user;
GRANT USAGE ON SCHEMA public TO readonly_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly_user;

GRANT CONNECT ON DATABASE user_db TO readonly_user;
GRANT USAGE ON SCHEMA public TO readonly_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly_user;

GRANT CONNECT ON DATABASE marketplace_service TO readonly_user;
GRANT USAGE ON SCHEMA public TO readonly_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly_user;

GRANT CONNECT ON DATABASE api_gateway TO readonly_user;
GRANT USAGE ON SCHEMA public TO readonly_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly_user;
