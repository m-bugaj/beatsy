# api-gateway-web

Brama API — jedyny punkt wejścia z zewnątrz. Port 8080. Pakiet bazowy: `com.beatstore.apigatewayweb`. Reaktywny (WebFlux + Spring Cloud Gateway `gateway-server-webflux`), **nie** blokujący MVC — stąd brak `spring-boot-starter-data-jpa` (świadomie zakomentowany w `pom.xml`, "nie powinno się łączyć z reaktywnym springiem").

## Odpowiedzialność
1. Weryfikuje JWT (RSA256, klucz publiczny) z ciasteczka `jwt`, wyciąga claimy `userHash`/`roles` i **mutuje request**, dopisując nagłówki `X-User-Hash` / `X-Roles` przed przekazaniem dalej — to jest źródło tych nagłówków, którym ufają `order-service`/inne serwisy (patrz ich `CLAUDE.md`, sekcja security).
2. Routing do serwisów downstream (Spring Cloud Gateway routes).
3. Blokuje z zewnątrz każdy path zawierający `/internal/` (`BlockInternalEndpointsFilter` → 404) — endpointy `/internal/**` (np. w `user-service`, `content`) są więc dostępne wyłącznie serwis-serwis.

## Auth flow (`JwtAuthenticationFilter`, `GlobalFilter`)
- Endpoint uznany za `securedEndpoint`, jeśli path zawiera `/secured/` → brak ciasteczka `jwt` = od razu 401.
- Brak ciasteczka na endpointach niesekurowanych → request przechodzi dalej bez nagłówków usera (anonimowo).
- JWT jest weryfikowany (`JwtVerifier`, `com.auth0:java-jwt`), błąd weryfikacji → 401.
- **Spring Security samo w sobie nic nie egzekwuje** — `SecurityConfig` ma `anyExchange().permitAll()` w obu profilach (dev i !dev). Cała faktyczna kontrola dostępu to ręczna logika w `JwtAuthenticationFilter` + `BlockInternalEndpointsFilter`, nie deklaratywne reguły Spring Security.

## Routing — WAŻNE: Eureka jest podłączony, ale nieużywany do routingu
- `application-dev.properties` ma zdefiniowane trasy z **zahardkodowanymi adresami**: `/api/auth/**` → `http://localhost:8082`, `/api/marketplace/**` → `http://localhost:8083`, `/api/content/**` → `http://localhost:8084`.
- Warianty przez discovery (`uri=lb://USER-SERVICE`, `discovery.locator.enabled=true`, `eureka.client...`) są w pliku, ale **zakomentowane**. Ten sam wzorzec (Eureka zarejestrowany, ale realny routing/wywołania idą po hardkodowanym `localhost:port`) powtarza się w `marketplace`'s Feign clientach — to systemowy nawyk w tym repo, nie wyjątek. Przy dodawaniu nowej trasy pamiętaj, że **nie ma automatycznego podłączenia nowego serwisu** — trzeba ręcznie dopisać wpis `routes[n]`.
- **Brak tras dla `order-service` (8086) i `user-service` (8085)** — te dwa serwisy nie są w ogóle osiągalne przez gateway z zewnątrz w obecnej konfiguracji, mimo że `order-service` ma endpointy pod `/secured/cart`, `/secured/checkout`. Jeśli coś z frontu/Postmana nie dobija do order-service — to dlatego, nie błąd w samym order-service.

## Gotchas
- `CorsConfig`: `allowCredentials(true)` razem z `addAllowedOrigin("*")` — Spring Framework rzuca `IllegalArgumentException` w runtime dla tej kombinacji przy realnym request z credentials (`"*" cannot be used with allowCredentials=true"`), zależnie od wersji może się objawiać dopiero przy pierwszym faktycznym CORS request z przeglądarki z cookies. Jest tam komentarz `//TODO MB: W PRZYSZŁOŚCI ZMIENIĆ NA ORIGIN FRONTU` — to jednocześnie znany dług i potencjalna bomba przy pierwszym prawdziwym froncie.
- `RsaPublicKeyConfig` czyta klucz z `classpath:keys/public.pem` — `keys/` jest w `.gitignore`, plik NIE jest w repo, trzeba go dostarczyć lokalnie (musi być parą do `private.pem` z `auth-service`, inaczej weryfikacja JWT zawsze zawiedzie).

## Build/test
- `./mvnw clean install` / `./mvnw test` w tym katalogu.
- Jedyny test to `contextLoads()`.
