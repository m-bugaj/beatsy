# content

Katalog treści (bity + ich metadane) dla marketplace. Multi-moduł Maven, analogicznie do `marketplace`:
- `content-service` — właściwy serwis. Port 8084. Pakiet bazowy: `com.beatstore.contentservice`.
- `content-rest-client` — współdzielony klient Feign (`ContentClient`) + DTO kontraktu, dependency dla innych serwisów. Pakiet: `com.beatstore.contentrestclient`.

Parent POM (`content/pom.xml`, packaging `pom`) spina oba moduły + BOM-y Spring Boot/Cloud.

## Odpowiedzialność
CRUD metadanych treści: upload/edycja/odczyt **bitów** (`ContentType.BEAT`), przypisanie gatunków, widoczność. Encja `Content` jest bazą, `BeatDetails` (1:1, `@MapsId`) dokłada pola specyficzne dla bitu (bpm/key/mood). Katalog dla marketplace: udostępnia innym serwisom listę treści usera i szczegóły po hashach (kontroler `/internal`).

**Czego serwis NIE robi (mimo nazwy „content"):** żadnej obsługi plików. Upload przyjmuje wyłącznie JSON z metadanymi — `@RequestPart`/`MultipartFile` w `BeatController` są zakomentowane, a cała logika storage (S3/GCS/pre-signed URL, `file-upload`) to TODO w `BeatService.uploadNewBeat` (docelowo front ma wgrywać pliki bezpośrednio do storage, tu ma trafiać tylko referencja). `ContentType.SOUND_KIT` istnieje w enumie, ale nie ma dla niego żadnej implementacji — tylko BEAT jest obsłużony.

## Uruchomienie
- **Jest tylko profil `dev`** (`application-dev.properties`: port 8084, DB `content_db` na `localhost:5432`, user `admin/admin123`). Bazowy `application.properties` niesie tylko `spring.application.name`. Bez `-Dspring-boot.run.profiles=dev` serwis wstanie bez konfiguracji DB.
- `spring.jpa.hibernate.ddl-auto=validate`, **brak Flyway/Liquibase**. Schemat trzeba nałożyć ręcznie z `content-service/src/main/resources/db_migrations/V1__0.0.0_0.0.1.sql` na bazę `content_db` zanim serwis pierwszy raz wystartuje — inaczej Hibernate `validate` wywali start.
- Migracja tworzy tabele `content`, `genres` (z seedem 10 gatunków), `content_genres`, `beat_details` + trigger `set_modified_at` na `content`. Kolumny `created_at`/`modified_at` wypełnia DB (DEFAULT + trigger), encja `Content` ich nie ustawia.

## Struktura pakietów (`content-service`)
- `controller` — `BeatController` (`/secured/beat`, ruch klienta) + `controller/internal/ContentControllerInternal` (`/internal/content`, ruch serwis-serwis).
- `service` — `BeatService` (całość logiki; sporo zakomentowanego martwego kodu po starym modelu `Beat`/`MediaFile`/`License`).
- `model` / `repository` / `dto` — standardowo. `common/enums` — `ContentType` (w rest-client!), `ContentVisibility`, `MusicGenre`, `beat/BeatMood`.
- `context` — `RequestContext` (`@RequestScope`, tylko `userHash` jest realnie wypełniany) przez `UserRequestContextFilter` z nagłówka `X-User-Hash`.
- `security` — `AuthenticationFilter` buduje `Authentication` z `X-User-Hash` + `X-Roles` (role → `ROLE_*`). **Brak lokalnej weryfikacji JWT** — serwis ufa nagłówkom od gateway. `SecurityConfig` ma `anyRequest().permitAll()` — nic nie jest wymuszane, mimo `/secured/...` w ścieżkach. Dwa równoległe filtry (context + security) czytają ten sam nagłówek.
- `exceptions` — `DomainException` (abstrakcyjna, `ErrorCode` + status) + `GlobalExceptionHandler` (`@RestControllerAdvice`, `DomainException` → `ApiErrorResponse`, reszta → 500).

## Model domenowy
- `Content` (bazowa: `hash`, `userHash`, `type`, `title`, `description`, `visibility`, `@ManyToMany genres`). `hash` = losowy UUID nadawany w serwisie, to publiczny identyfikator używany między serwisami (nie `id`).
- `BeatDetails` — `@OneToOne @MapsId` do `Content` (współdzieli PK, `cascade = ALL`). Zapis idzie przez `BeatDetailsRepository.save` i kaskadowo zapisuje `Content`.
- `Genre` — słownik `MusicGenre`, seedowany w migracji. Bity linkują po `content_genres`.
- `ContentVisibility` = PUBLIC/PRIVATE/UNLISTED/PROTECTED — pole zapisywane, ale kontrola dostępu wg widoczności **nie istnieje** (TODO w `BeatController.getBeatDetails`: nikt nie sprawdza, czy user ma dostęp do treści nie-PUBLIC).

## Kluczowe przepływy
- **Upload bitu** (`POST /secured/beat/upload` → `BeatService.uploadNewBeat`): dociąga `Genre` po nazwach, zapisuje `BeatDetails`+`Content`, po czym **synchronicznie** woła `LicenseClient.assignLicenseToContent` (Feign → marketplace 8083). Metoda **nie jest `@Transactional`** (adnotacja zakomentowana) — zapis DB i wywołanie licencji nie są atomowe; padnięcie Feign zostawi bit bez licencji. Docelowo (TODO) ma to iść eventem Kafka.
- **Update** (`POST /secured/beat/{beatHash}`) i **odczyt** (`GET /secured/beat/{beatHash}`) — po `content.hash`.
- **Internal** (`ContentControllerInternal`): `getContentForUser` (lista treści usera danego typu) i `getContentDetails` (mapa hash→`ContentDetailsDto` dla listy hashy).

## Integracje
- **Konsumuje** `marketplace-rest-client` → `LicenseClient.assignLicenseToContent` (przypisanie licencji do świeżo uploadowanego bitu).
- **Jest konsumowany** przez `marketplace-service` przez własny `content-rest-client` → `ContentClient`: `getContentDetails` (`ContentOfferService` — nazwy/typy treści do ofert) i `getContentForUserHash` (`LicenseTemplateService` — wszystkie bity usera przy „apply to all beats").
- `@EnableFeignClients(basePackages = "com.beatstore")` w `ContentServiceApplication` — skanuje szeroko, rejestruje też **własny** `ContentClient` (content-service jest w swoim pom) wskazujący na samego siebie; nieużywany wewnętrznie. Klienci Feign mają `url` zaszyty na sztywno (`localhost:8084`/`8083`) — omijają Eureka discovery mimo eureka-client na classpath.

## Znane niespójności / gotchas
- **HTTP method mismatch na kontrakcie `getContentForUser`.** `ContentControllerInternal` wystawia je jako `@GetMapping("/user/{userHash}")`, a `ContentClient` w rest-client woła `@PostMapping("/internal/content/user/{userHash}")`. Wywołanie przez Feign (robi to `LicenseTemplateService`) trafi w GET-endpoint POST-em → **405**. `getContentDetails` (oba POST) jest spójne.
- **Typ ID w repozytoriach.** `BeatDetailsRepository extends JpaRepository<BeatDetails, Integer>` i `GenreRepository<Genre, Integer>`, mimo że `@Id` obu encji to `Long`. Działa dopóki nikt nie użyje `findById`/`deleteById` z realnym ID. `ContentRepository` jest poprawnie `<Content, Long>`.
- **`beat_details.mood NOT NULL` w SQL, ale encja/`BeatRequest` tego nie wymusza.** Upload bitu bez `mood` → NOT NULL violation. Brak jakiejkolwiek walidacji `@Valid`/`@NotNull` na `BeatRequest` (TODO: rozdzielić na create/update).
- **`content.title UNIQUE`** w migracji — dwa bity nie mogą mieć tego samego tytułu (nawet różnych userów). Wygląda na przeoczenie, nie decyzję biznesową.
- `ErrorCode`/`ContentNotFoundException` (CONTENT_NOT_FOUND) istnieją, ale serwis rzuca wyłącznie `BeatNotFoundException` — `ContentNotFoundException` jest nieużywana.
- Dużo martwego, zakomentowanego kodu w `BeatService`/`BeatController` po nieistniejącym już modelu (`Beat`, `MediaFile`, `FileStorageService`, `UserClient`, `getBeatSummaries`/feed) — nie mylić z aktualnym `Content`/`BeatDetails`.

## Build/test
- `./mvnw clean install` / `./mvnw test` w katalogu `content` (parent) buduje oba moduły; `content-service` zależy od zbudowanego `content-rest-client` i od `marketplace-rest-client` (musi być wcześniej w lokalnym `.m2`).
- Testy: tylko `contextLoads()` w obu modułach — brak testów `BeatService`/kontrolerów.
