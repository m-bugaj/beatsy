# Beatsy — root

Marketplace bitów muzycznych. Backend: mikroserwisy Spring Boot za bramą API. Ten plik to mapa orientacyjna — szczegóły serwisu są w jego własnym `CLAUDE.md`.

## Tech stack
- Java 23, Spring Boot 3.5.3, Spring Cloud 2025.0.0, Maven (każdy serwis ma własny `pom.xml`, brak wspólnego reactora).
- Pakiet bazowy: `com.beatstore.<serwis>`.
- PostgreSQL (docker-compose w `infrastructure/database/postgres`), Spring Data JPA.
- Service discovery: Eureka (`discovery-server`). Auth: JWT podpisany RSA256.
- Wrapper Mavena: `./mvnw` (jest w każdym serwisie).

## Mapa serwisów (`backend/microservices/`) i porty
| Serwis | Port | Stan |
|---|---|---|
| `discovery-server` (Eureka) | 8761 | szkielet |
| `api-gateway-web` (brama) | 8080 | mały |
| `auth-service` | 8082 | aktywny |
| `marketplace` → `marketplace-service` | 8083 | duży |
| `content` → `content-service` | 8084 | duży |
| `user-service` | 8085 | mały |
| `order-service` (koszyk/zamówienia) | 8086 | **bieżąca praca** |
| `payment-service` | 8087 | szkielet |
| `fulfillment-service` | 8088 | szkielet |
| `license-service` | 8089 | szkielet |
| `subscriptions` | — | szkielet |
| `file-upload` | — | mały |

`marketplace` i `content` to multi-moduł: `*-service` (właściwy serwis) + `*-rest-client` (współdzielony klient dla innych serwisów).

## Przepływ auth
- `auth-service` podpisuje JWT kluczem prywatnym RSA (`security/JwtService`, `RsaKeyConfig`) i wystawia refresh tokeny.
- `api-gateway-web` weryfikuje JWT kluczem publicznym (`security/JwtVerifier`, `RsaPublicKeyConfig`, `JwtAuthenticationFilter`) — ruch klienta wchodzi przez bramę.

## Struktura repo
- `backend/microservices/` — serwisy (patrz wyżej).
- `backend/gateway/` (`internal-api`, `mobile-api`, `web-api`) oraz `backend/bff/` — **puste szkielety** (`.gitkeep`), jeszcze nie zaimplementowane.
- `frontend/` — pusty szkielet.
- `infrastructure/` — `database/postgres` (docker-compose tst/prod), `kubernetes`, `nginx` (puste).
- `devops/` — pusty.

## Komendy (uruchamiać w katalogu serwisu)
- Build: `./mvnw clean install`
- Test: `./mvnw test`
- Uruchomienie: `./mvnw spring-boot:run` (profil dev: `-Dspring-boot.run.profiles=dev`)
- DB: `docker compose -f infrastructure/database/postgres/docker-compose.tst.yml up -d`

## Konwencje pracy z Claude Code
- Nowa sesja na serwis/zadanie zamiast jednego długiego wątku.
- Do szerokiego "gdzie jest X w repo" używać subagenta `Explore`.
- Onboarding kolejnych serwisów wg `tmp-ai-files/docs/worflow-plan/claude-workflow-plan.md`.
