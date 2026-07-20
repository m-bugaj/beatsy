# user-service

Profile użytkowników (display name, bio, lokalizacja). Port 8085, DB `user_db`. Pakiet bazowy: `com.beatstore.userservice`.

## Odpowiedzialność
Bardzo wąska: trzyma `UsersProfile` (dane profilowe, nie konto/hasło — to jest w `auth-service`) i wystawia jeden endpoint do zbiorczego odczytu display name'ów po `userHash`, do użytku przez inne serwisy (np. wyświetlanie nazwy sprzedawcy w `marketplace`/`content`).

## Uruchomienie
- Profil dev: DB `user_db` na `localhost:5432`, `ddl-auto=validate` — jak w innych serwisach, schemat (`db_migrations/V1__0.0.0_0.0.1.sql`) trzeba nałożyć ręcznie przed pierwszym uruchomieniem (brak Flyway/Liquibase).

## Struktura
- Jeden kontroler: `UserInfoInternalController` pod `/internal/user` (`POST`, przyjmuje `Set<String>` userHash, zwraca `CollectionWrapper<UserInfoDTO>`). Brak jakiegokolwiek publicznego endpointu (np. do edycji własnego profilu) — to jeszcze nie istnieje.
- `/internal/**` jest blokowane na poziomie `api-gateway-web` (`BlockInternalEndpointsFilter`), więc ten endpoint jest osiągalny wyłącznie serwis-serwis (bezpośrednio po hoście/porcie albo przez Eureka/Feign), nigdy przez gateway z zewnątrz.
- Brak `SecurityConfig`/filtra czytającego `X-User-Hash` — serwis nie ma w ogóle warstwy security; ufa, że nikt z zewnątrz się do niego nie dostanie (patrz punkt wyżej).
- `model.Avatars`, `model.Followers`, `model.SocialLinks` — puste klasy-zaślepki (brak pól, brak `@Entity`), zapowiedź przyszłych funkcji, nieużywane nigdzie w kodzie.

## Model domenowy
- `UsersProfile`: `userHash`, `displayName`, `bio`, `location`. W SQL **`display_name` ma constraint `UNIQUE`** — nietypowe dla display name'u (zwykle nie jest unikalny); jeśli to nie jest świadoma decyzja biznesowa, przy pierwszej kolizji dwóch userów o tej samej nazwie insert się wywali.
- Brak relacji do `Avatars`/`Followers`/`SocialLinks` w kodzie mimo że klasy istnieją — nie są to jeszcze `@Entity` ani nie są podpięte do `UsersProfile`.

## Integracje
- Zależność od `spring-cloud-starter-openfeign` i Eureka client w `pom.xml`, ale serwis sam nie ma żadnego Feign klienta ani `@EnableFeignClients` — to prawdopodobnie przygotowanie pod przyszłe wywołania wychodzące, na razie nieużywane.
- Konsument: inne serwisy powinny wołać `POST /internal/user` żeby zamienić `userHash` na `displayName` (np. przy renderowaniu ofert w marketplace) — obecnie żaden ze zbadanych serwisów (`order-service`, `marketplace`, `content`) tego jeszcze nie robi.

## Build/test
- `./mvnw clean install` / `./mvnw test` w tym katalogu.
- Jedyny test to `contextLoads()`. Uwaga: pakiet testu to `com.beatstore.userservicee` (literówka, podwójne „e") — nieszkodliwe, ale rzuca się w oczy przy grepowaniu.
