# auth-service

Uwierzytelnianie i wystawianie tożsamości dla całego marketplace. Port 8082. Pakiet bazowy: `com.beatstore.authservice`.

## Odpowiedzialność
Rejestracja + logowanie (username/email + hasło), podpisywanie JWT kluczem prywatnym RSA256, wystawianie refresh tokenów, przechowywanie sesji użytkownika oraz zwracanie danych użytkowników po `userHash` innym serwisom (endpoint `/internal/user`). To auth-service jest źródłem tożsamości: JWT, który podpisuje, weryfikuje potem `api-gateway-web` kluczem publicznym (patrz root `CLAUDE.md`) i na jego podstawie rozdaje nagłówki `X-User-Hash`/`X-Roles` do reszty serwisów.

## Uruchomienie
- Profil dev: `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev` → `application-dev.properties` (port 8082, DB `auth_db` na `localhost:5432`, user `admin`/`admin123`).
- **`spring.jpa.hibernate.ddl-auto=validate`, brak Flyway/Liquibase.** Schemat nakłada się ręcznie z `src/main/resources/db_migrations/V1__0.0.0_0.0.1.sql` na bazę `auth_db` przed pierwszym startem (inaczej Hibernate `validate` wywali start).
- **Seed ról obowiązkowy:** rejestracja wymaga wierszy w tabeli `role` (`USER`, `BUYER` z `is_default`), a migracja SQL ich nie wstawia. Bez ręcznego zasiania ról `registerUser` rzuca `RoleNotFoundException`.
- Klucze RSA: `src/main/resources/keys/private.pem` + `public.pem` (ładowane przez `RsaKeyConfig`, ścieżki w `jwt.*-key-path`). **`keys/` jest w `.gitignore` — pliki nie są w repo**, trzeba je wygenerować/dostarczyć lokalnie przed pierwszym uruchomieniem (inaczej `RsaKeyConfig` nie wystartuje).

## Struktura pakietów
- `controller` — `AuthController` (`/api/auth/register`, `/api/auth/login`), `OAuth2LoginController`, `RefreshTokenController`, `controller/internal/UserInfoInternalController` (`POST /internal/user`).
- `security` — `AuthService` (orkiestracja login/OAuth2), `JwtService` (podpis/weryfikacja JWT), `RefreshTokenService`, `CustomUserDetails(Service)`, `security/config` (`SecurityConfig`, `RsaKeyConfig`, `JwtConfig`), `security/filter/JwtAuthenticationFilter`.
- `service` — `UserAccountService` (rejestracja), `UserSessionService`, `UserInfoService`.
- `model` / `repository` / `dto` / `enums` / `exception` — standardowo. `context` — `RequestContext` (`@RequestScope`) wypełniany przez `RequestContextFilter` z zweryfikowanego JWT.

## Model domenowy
- `UserAccount` (`user_hash` UUID, `username`, `email`, `password_hash` BCrypt) → `UserRole` (N, tabela łącząca) → `Role` (`name` enum `UserRoleName`: USER/BUYER/ADMIN, `isDefault`).
- `RefreshToken` (UUID string, `expires`, FK do usera) — 1 user może mieć wiele wierszy (brak unikalności per user).
- `UserSession` — 1:1 z userem (`unique_user`), niesie `userHash`, `ipAddress`, `userAgent`, `expiresAt`, oraz nieużywany `subscriptionHash`.

## Kluczowe przepływy
- **Rejestracja** (`UserAccountService.registerUser`): sprawdza `existsByEmail` → generuje `userHash` (UUID) → pobiera domyślne role → BCrypt hasła → zapis. Brak walidacji (`@Valid`) DTO i brak sprawdzenia unikalności `username` (leci do constraintu DB).
- **Login** (`AuthService.login`): `AuthenticationManager` (DAO + BCrypt) po username **lub** emailu (`CustomUserDetailsService` próbuje obu) → `JwtService.generateToken` → `RefreshTokenService.generateRefreshToken` → `UserSessionService.createSession` → JWT ustawiany jako **httpOnly cookie `jwt`** (maxAge 1h na sztywno).
- **JWT payload** (`JwtService.generateToken`): `subject` = username, claim `userHash` = UUID, claim `roles` = lista nazw ról (stringi). Podpis `Algorithm.RSA256(publicKey, privateKey)`, TTL z `jwt.expiration` (3600000 ms = 1h). To dokładnie te dane, które czyta gateway i przekłada na `X-User-Hash`/`X-Roles`.
- **Weryfikacja lokalna** (`JwtAuthenticationFilter`, order 1): czyta token z cookie `jwt`, weryfikuje, wkłada `DecodedJWT` jako principal; `RequestContextFilter` (order 2) przepisuje `subject`/`userHash` do `RequestContext`. Oba to zwykłe beany `OncePerRequestFilter` (rejestrowane globalnie przez Boota), **nie są wpięte w `SecurityConfig` przez `addFilterBefore`**.

## Integracje
- **Eureka client** (dependency w `pom.xml`), brak jawnej konfiguracji Eureki w properties.
- `/internal/user` (`UserInfoInternalController`): przyjmuje `Set<userHash>`, zwraca `Set<UserInfoDTO>{userHash, username}` — konsumowane service-to-service (np. przez marketplace do wzbogacania danych sprzedawcy/kupującego).
- OAuth2 Google skonfigurowany w properties, ale **client-id/secret to placeholdery** (`YOUR_GOOGLE_CLIENT_ID`).

## Znane niespójności / gotchas (nie naprawiać teraz, tylko wiedzieć)
- **Refresh token jest martwy end-to-end.** `RefreshTokenController` ma cały endpoint zakomentowany → brak `/auth/refresh-token`. Dodatkowo login zwraca refresh token tylko w `AuthResponse` w pamięci — `AuthController` odsyła klientowi wyłącznie cookie z JWT, refresh tokena **nie ma w odpowiedzi**. Tokeny są więc generowane i zapisywane do bazy, ale nigdy nie wracają do klienta ani nie da się ich zużyć.
- **OAuth2 login nie działa.** `OAuth2LoginController` **nie ma adnotacji `@RestController`/`@Controller`**, więc `/auth/oauth2/success` nie jest zarejestrowany. Ścieżka rejestracji OAuth2 (`AuthService.createNewUser` → `registerUser` z DTO bez `username`/`password`) i tak wywaliłaby się na `BCrypt.encode(null)` / NOT NULL.
- **Brak logoutu i rewokacji.** Nigdzie nie unieważnia się JWT ani refresh tokenów; `UserSessionService.clearSession` jest `private` i nieużywane, `validateAndRefreshSessionOrThrow` wołane tylko z martwego kodu. Sesje w `user_session` powstają przy loginie, ale nic ich potem nie sprawdza — de facto dead weight. TTL: JWT 1h, refresh 1 tydzień (`plusWeeks(1)` na sztywno w kodzie).
- **Rozjazdy SQL vs encje:** `user_account.username`/`password_hash` są w SQL nullable, w encji `nullable=false`; `refresh_token.expires` ma `UNIQUE` (dwa tokeny nie mogą mieć tego samego czasu wygaśnięcia — kruche); `refresh_token.user_account_id BIGSERIAL` zamiast `BIGINT` (zbędna sekwencja na kolumnie FK). `first_name`/`last_name` są `NOT NULL` w SQL — rejestracja bez nich = DataIntegrityViolation.
- **`/internal/user` wpada pod `anyRequest().authenticated()`**, a wywołania service-to-service nie niosą cookie `jwt` → prawdopodobnie 401, chyba że idą przez gateway z tożsamością.
- **Dwie biblioteki JWT w `pom.xml`:** używana jest tylko `com.auth0:java-jwt`; `io.jsonwebtoken:jjwt` jest martwy (został po zakomentowanym kodzie HMAC).
- Drobne: `UserAccountService` tworzy `new BCryptPasswordEncoder()` zamiast wstrzykiwać bean; `spring.main.allow-bean-definition-overriding=true`; poświadczenia DB w plaintext w properties; brak rate-limitingu na `/login`; sporo zakomentowanego kodu w każdej klasie security.

## Build/test
- `./mvnw clean install` / `./mvnw test` w tym katalogu.
- Jedyny test to `AuthServiceApplicationTests.contextLoads()` — brak testów jednostkowych/integracyjnych dla logowania, rejestracji, JWT czy sesji.
