# marketplace

Największy serwis w repo, multi-moduł Maven. Warstwa **ofert sprzedaży i licencji** dla contentu (bitów). Port 8083. Pakiety bazowe: `com.beatstore.marketplaceservice` (serwis) + `com.beatstore.marketplacerestclient` (klient).

## Odpowiedzialność
Sprzedawca definiuje szablony licencji (`LicenseTemplate` + limity `LicenseLimitConfig`) i przypina je do swojego contentu, tworząc `ContentOffer` (= konkretna oferta: „ten bit na tej licencji, za tę cenę"). Marketplace **nie trzyma samych bitów ani plików** — content (nazwa, typ, pliki) mieszka w `content-service`, marketplace zna tylko `contentHash` i dociąga szczegóły przez `content-rest-client`. Serwis wystawia też kontrakt cenowo-walidacyjny konsumowany przy checkoucie przez `order-service`.

## Relacja modułów (multi-moduł, ale BEZ wspólnego reactora)
- `marketplace-service` — właściwy serwis (encje, kontrolery, logika). Zależy od `marketplace-rest-client` **oraz** od `content-rest-client` (klient content-service).
- `marketplace-rest-client` — współdzielone interfejsy Feign + DTO dla **innych** serwisów (dziś: `order-service`). Marketplace sam ich nie używa.
- **Oba `pom.xml` mają `spring-boot-starter-parent` jako parent — nie ma nadrzędnego pom agregującego.** Buduje się je osobno i w kolejności: najpierw `marketplace-rest-client` (`mvn install` do lokalnego repo), potem `marketplace-service`, który go rozwiązuje jako zależność `com.beatstore:marketplace-rest-client:0.0.1-SNAPSHOT`. Analogicznie `content-rest-client` musi być wcześniej zainstalowany.

## Uruchomienie
- Profil dev: `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev` → `application-dev.properties` (port 8083, DB `marketplace_service` na `localhost:5432`, user `admin`/`admin123`).
- `application.properties` ustawia tylko nazwę appki i `spring.main.allow-bean-definition-overriding=true`.
- **`spring.jpa.hibernate.ddl-auto=validate`, brak Flyway/Liquibase.** Schemat trzeba nałożyć ręcznie: `marketplace-service/src/main/resources/db_migrations/V1__0.0.0_0.0.1.sql` na bazę `marketplace_service` przed pierwszym startem (inaczej Hibernate `validate` wywali start). Migracja zakłada rolę `admin` (`ALTER SEQUENCE ... OWNER TO admin`).

## Struktura pakietów (`marketplace-service`)
- `controller` (publiczne, `/secured/**`) + `controller/internal` (`/internal/**`, wołane service-to-service) / `service` / `repository` / `model` / `dto`.
- `context` — `RequestContext` (`@RequestScope`, userHash) wypełniany z nagłówka `X-User-Hash` przez `UserRequestContextFilter`. Kontrolery czytają userHash **stąd**, nie z `SecurityContext`.
- `security` — `AuthenticationFilter` + `SecurityConfig` (patrz Auth niżej).
- `client` — `UserClient` (Feign do user-service). **Martwy** — używany tylko w zakomentowanym `BeatService`.
- `common/enums`, `utils` (`CollectionWrapper`, `PageWrapper`), `exceptions`.

## Model domenowy (jedyny żywy model)
- `LicenseTemplate` (N) —(`@OneToOne` cascade PERSIST)→ `LicenseLimitConfig` (limity: streamy, downloady mp3/wav/stems, broadcast, monetyzacja YT, czas trwania). `sellerHash` = właściciel.
- `LicenseTemplate` (1) → `ContentOffer` (N). `ContentOffer` = `contentHash` + `licenseTemplate` + opcjonalny `customPrice` + `active`. Cena efektywna = `customPrice` albo `licenseTemplate.defaultPrice`.
- Unikalność w bazie: `content_offer UNIQUE (content_hash, license_template_id)` — jeden content nie może mieć duplikatu tej samej licencji.

## Kluczowe przepływy
- **Tworzenie licencji** (`POST /secured/license`, `LicenseTemplateService.createLicense`): zapis `LicenseTemplate`. Jeśli `applyToAllBeats=true`, dociąga cały content sprzedawcy z content-service (`ContentClient.getContentForUserHash`) i tworzy `ContentOffer` dla każdego bita bez tej licencji.
- **Przypisanie licencji do contentu** (`POST /internal/license/assign/user/{userHash}`): tworzy `ContentOffer` z mapą `licenseHash → customPrice`.
- **Kontrakt checkout** (`/internal/content-offer/*`, `ContentOfferService`): `prices/fetch` (ceny), `prices/validate` (porównuje ceny z koszyka z aktualnymi → `PriceChange` + lista niedostępnych), `details/checkout` (dociąga nazwy z content-service, składa `ContentOfferCheckoutDetails`). Wszystkie filtrują `active = true`.

## Integracje
- **Wychodzące**: `content-rest-client` → `ContentClient` (nazwy/typy contentu, lista contentu sprzedawcy). Feign włączony w `MarketplaceServiceApplication` przez `@EnableFeignClients(basePackages = "com.beatstore.contentrestclient.client")` — uwaga, skanowany jest **tylko** ten pakiet.
- **Przychodzące (kontrakt dla innych)** w `marketplace-rest-client`: `ContentOfferClient` (`getPricesForContentOffers`, `validatePricesForContentOffers`, `getContentOffersDetailsForCheckout`) — konsumuje `order-service` przy checkoucie. `LicenseClient` (`assignLicenseToContent`) — zdefiniowany, ale **na razie bez konsumenta**.

## Auth / security
- Model zaufania jak w order-service: **brak lokalnej weryfikacji JWT**, serwis ufa nagłówkom `X-User-Hash` / `X-Roles` ustawionym przez gateway. `AuthenticationFilter` buduje `Authentication` z ról (`ROLE_*`), `SecurityConfig` ma `anyRequest().permitAll()`.
- **Różnica względem order-service**: tu włączone jest `@EnableMethodSecurity` i endpoint `POST /secured/license` ma `@PreAuthorize("hasAnyRole('ADMIN','USER')")` — czyli method-level security realnie działa (choć trywialnie, bo prawie każdy jest USER). Reszta `/secured/**` nic nie wymusza.
- `/internal/**` jest całkowicie bez auth — zakłada wyłącznie wywołania service-to-service (gateway ich nie eksponuje).

## Znane niespójności / gotchas
- **Ogromna ilość martwego kodu po refaktorze „beats → content".** Cała domena bitów jest zakomentowana w całości: encje `Beat`, `BeatLicense`, `MediaFile`(?), kontroler `BeatController`, `BeatService`, `FileStorageService`, `BeatRepository`, DTO `Beat*`, oraz tabele `beats`/`genres`/`media_files`/`beat_genres` i ich triggery w SQL. Pliki `.java` istnieją, ale to same komentarze. Content i pliki przeniesiono do `content-service`/`file-upload`. Przy liczeniu „67 plików" większość beat-owych to puste skorupy.
- **`ContentOffer.hash` nigdy nie jest ustawiany przy zapisie, a w SQL jest `NOT NULL`.** Konstruktor `ContentOffer(contentHash, licenseTemplate, active, customPrice)` pomija `hash`, encja nie ma `@PrePersist` ani `@GeneratedValue` na `hash`. `LicenseTemplateService` robi `new ContentOffer(...)` → `saveAll` bez `setHash`. **Insert do `content_offer` powinien wywalać NOT NULL violation na `hash`** (kontrast z `LicenseTemplate`, gdzie hash jest ustawiany buildererem `UUID.randomUUID()`). Do naprawy zanim tworzenie ofert zadziała.
- **`ContentOfferClient` i `LicenseClient` mają zaszyty `url = "http://localhost:8083"`** — omijają Eureka mimo że serwis jest klientem Eureki. Load-balancing/discovery nie zadziała, dev-only hardcode. (`UserClient` z kolei używa nazwy `user-service` bez url.)
- **`marketplace-rest-client` jest bibliotekowym modułem, ale ma `@SpringBootApplication` + `main` (`MarketplaceRestClientApplication`).** Zbędne dla współdzielonego JAR-a; łatwo przez to przypadkiem odpalić „aplikację" bez kontekstu.
- `ErrorCode` ma tylko `BEAT_NOT_FOUND` (pozostałość po beatach) + `INTERNAL_SERVER_ERROR`. Wyjątki `BeatNotFoundException`, `UserNotFoundException`, `MissingRequiredFileException` istnieją, ale nic ich realnie nie rzuca w żywym kodzie — `GlobalExceptionHandler` w praktyce łapie tylko generic 500.
- `ContentOfferService.getContentOffersDetailsForCheckout` zakłada, że content-service zwróci szczegóły dla każdego `contentHash` — brak obsługi `null` z mapy `contentHashToContentDetails` (NPE, jeśli content zniknął).

## Build / test
- `./mvnw clean install` osobno w każdym module; `marketplace-rest-client` przed `marketplace-service`.
- Testy to tylko `contextLoads()` w obu modułach — brak testów jednostkowych dla `ContentOfferService`/`LicenseTemplateService`.
