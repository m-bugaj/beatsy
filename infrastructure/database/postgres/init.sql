-- Tworzymy użytkowników z prostymi hasłami
CREATE USER root WITH PASSWORD 'root';
CREATE USER readonly_user WITH PASSWORD 'password';

-- Tworzymy bazę danych, jeśli jeszcze nie istnieje
CREATE DATABASE dev_beatsy;

-- Przyznajemy pełne uprawnienia użytkownikowi root
GRANT ALL PRIVILEGES ON DATABASE dev_beatsy TO root;

-- Przyznajemy tylko uprawnienia do odczytu użytkownikowi readonly_user
GRANT CONNECT ON DATABASE dev_beatsy TO readonly_user;
GRANT USAGE ON SCHEMA public TO readonly_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly_user;
