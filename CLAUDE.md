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
- `api-gateway-web` weryfikuje JWT z ciasteczka `jwt` kluczem publicznym (`JwtVerifier`, `RsaPublicKeyConfig`, `JwtAuthenticationFilter`), wyciąga claimy `userHash`/`roles` i **mutuje request**, dopisując nagłówki `X-User-Hash` / `X-Roles` — to jedyne źródło tożsamości usera dla serwisów za bramą.
- Serwisy downstream (`order-service` i inne) **ufają tym nagłówkom bez własnej weryfikacji JWT** — nie ma drugiej linii obrony, jeśli coś ominie gateway.

## Wzorce cross-service (z onboardingu Dni 1-6, szczegóły w CLAUDE.md danego serwisu)
- **Discovery jest podłączony, ale nieużywany do routingu.** Wszystkie serwisy rejestrują się w Eurece, ale realne wywołania międzyserwisowe idą po zahardkodowanym `localhost:port` — tak w trasach `api-gateway-web` (`spring.cloud.gateway...routes[n].uri=http://localhost:808x`, warianty `lb://SERVICE` zakomentowane), jak i w Feign klientach `marketplace-service` (`url=http://localhost:8083`). Nie zakładaj, że dodanie serwisu do Eureki wystarczy, żeby zaczął być osiągalny.
- **Luka w routingu gateway:** obecnie brak tras do `order-service` (8086) i `user-service` (8085) w `api-gateway-web` — te serwisy nie są dziś osiągalne z zewnątrz mimo że mają gotowe endpointy `/secured/...`. Sprawdź `api-gateway-web/CLAUDE.md` zanim założysz, że coś z frontu powinno działać.
- **`/internal/**` = tylko serwis-serwis.** `BlockInternalEndpointsFilter` w gateway zwraca 404 dla każdego path zawierającego `/internal/` z zewnątrz (np. `user-service` GET display name'ów, `content` internal lookup) — te endpointy nie mają i nie potrzebują własnej warstwy auth, bo z założenia nie są osiągalne przez bramę.
- **Migracje SQL są ręczne wszędzie.** Żaden serwis nie ma Flyway/Liquibase — każdy ma `src/main/resources/db_migrations/V1__*.sql`, `ddl-auto=validate`, i trzeba ten SQL nałożyć ręcznie na bazę przed pierwszym uruchomieniem. W kilku miejscach schemat SQL i encje JPA już się rozjechały (patrz gotchas w `order-service`/`marketplace`/`content` CLAUDE.md) — przy dotykaniu modelu w dowolnym serwisie warto zweryfikować zgodność z migracją, nie tylko z encją.
- **Klucze RSA (`keys/` w `auth-service` i `api-gateway-web`) są w `.gitignore` — nie ma ich w repo.** Trzeba je dostarczyć lokalnie (para prywatny/publiczny, zgodna między obydwoma serwisami) przed pierwszym uruchomieniem — inaczej `RsaKeyConfig`/`RsaPublicKeyConfig` nie wystartują.

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
- **Wdrażając funkcjonalność, obowiązkowo twórz lub aktualizuj testy jednostkowe** obejmujące zmienioną logikę (nowe reguły, przypadki brzegowe, naprawione błędy). Zmiana logiki bez odpowiadających jej testów jest niekompletna.
