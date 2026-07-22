# order-service

Koszyk i zamówienia dla marketplace bitów. Port 8086. Pakiet bazowy: `com.beatstore.orderservice`.

## Odpowiedzialność
Cart (koszyk) → walidacja cen/dostępności przy checkoucie → Order. Bez integracji z płatnościami/fulfillmentem — `payment-service` i `fulfillment-service` to nadal szkielety, więc checkout kończy się na utworzeniu `Order` ze statusem `PENDING_PAYMENT`-podobnym, nic dalej go nie procesuje.

## Uruchomienie
- Profil dev: `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev` → `application-dev.properties` (port 8086, DB `order_db` na `localhost:5432`).
- DB startuje przez `infrastructure/database/postgres/docker-compose.dev.yml` (mountuje `init.sql`, który tylko tworzy puste bazy per serwis, w tym `order_db`).
- **`spring.jpa.hibernate.ddl-auto=validate`, brak Flyway/Liquibase w `pom.xml`.** Schemat trzeba nałożyć ręcznie: `src/main/resources/db_migrations/V1__0.0.0_0.0.1.sql` na bazę `order_db`, zanim serwis pierwszy raz wystartuje (inaczej Hibernate `validate` wywali błąd startowy o brakujących tabelach/kolumnach).

## Struktura pakietów
- `controller` / `service` / `repository` / `model` / `dto` / `mapper` — standardowy układ warstwowy.
- `context` — `RequestContext` (`@RequestScope` bean: userHash/role) wypełniany przez `UserRequestContextFilter` z nagłówka `X-User-Hash`.
- `security` — `AuthenticationFilter` buduje `Authentication` z nagłówków `X-User-Hash` + `X-Roles` (role rozdzielone przecinkiem → `ROLE_*`). **Brak lokalnej weryfikacji JWT** — serwis ufa nagłówkom, zakładając że tylko gateway (po weryfikacji JWT, patrz root `CLAUDE.md`) je ustawia. `SecurityConfig` ma `authorizeHttpRequests().anyRequest().permitAll()` — sam serwis niczego nie wymusza, mimo że endpointy wiszą pod `/secured/...`.
- `exceptions` — `DomainException` (abstrakcyjna, niesie `ErrorCode` + HTTP status) + `GlobalExceptionHandler` (`@RestControllerAdvice`): łapie `DomainException` → strukturalny `ApiErrorResponse`, wszystko inne → 500 generic.

## Model domenowy
- `Cart` (1) → `CartItem` (N), po `buyerHash` (`CartRepository.findFirstByBuyerHash` — brak unikalności wymuszonej w bazie, teoretycznie może powstać więcej niż jeden koszyk na usera).
- `Order` (1) → `OrderItem` (N) + `OrderStatusHistory` (N, encja istnieje ale nic jeszcze do niej nie zapisuje — brak zapisywania historii zmian statusu w kodzie).
- `Cart.active` — kolumna/pole dodane niedawno (`aaceb04 Rename table and add active flag to cart`), ale **nic go nie ustawia ani nie filtruje** — `CartService` go ignoruje całkowicie.
- Tylko zakupy jednosztukowe: `AddItemsToCartCommand` niesie `Set<contentOfferHash>`, `CartService.createOrUpdateCart` na sztywno wymusza `quantity=1` (komentarz w kodzie: "system przewiduje tylko jednosztukowe zakupy").

## Przepływ checkout (`CheckoutService.checkout`)
1. `CartService.getCartForUser` — `CartNotFoundException` gdy usera nie ma koszyka wcale.
2. `EmptyCartException` gdy koszyk pusty.
3. `validateAndUpdateCart` — woła `ContentOfferClient.validatePricesForContentOffers` (Feign → marketplace).
4. Gdy walidacja wykryje zmiany (item niedostępny / cena inna) → zwraca `CheckoutResult.validationFailed` z poprawionym `CartDTO`, **ale to tylko obiekt w pamięci — `cart_items` w bazie nie są aktualizowane ani usuwane**. Klient musi sam zdecydować co dalej; stan bazy i to, co widział frontend, mogą się rozjechać.
5. Gdy walidacja OK → dociąga szczegóły (`getContentOffersDetailsForCheckout`), buduje `Order` + `OrderItem`, zapisuje.
6. **Koszyk nie jest czyszczony po udanym checkout** — krok „4. Clear the buyer's cart" zostawiony jako komentarz/TODO w `CheckoutService`, niezaimplementowany. Ponowny checkout na tym samym koszyku prawdopodobnie utworzy duplikat zamówienia.

## Integracje
- `marketplace-rest-client` (moduł współdzielony z `marketplace-service`) → Feign `ContentOfferClient`: ceny (`getPricesForContentOffers`), walidacja (`validatePricesForContentOffers`), szczegóły do checkoutu (`getContentOffersDetailsForCheckout`).
- `@EnableFeignClients(basePackages = "com.beatstore.marketplacerestclient.client")` w `OrderServiceApplication`, plus standardowy Eureka client.
- `payment-service`, `fulfillment-service`, `license-service` — brak jakiejkolwiek integracji, wszystkie nadal szkielety.

## Znane niespójności (do naprawy przy okazji, nie teraz)
- `OrderRepository extends JpaRepository<Order, Integer>` i `OrderItemRepository extends JpaRepository<OrderItem, Integer>`, mimo że `@Id` obu encji to `Long`. Działa dopóki ktoś nie użyje `findById`/`deleteById` z realnym ID.
- SQL migracja ma kolumnę `cart.cart_hash VARCHAR NOT NULL` nieobecną w encji `Cart` — Hibernate nigdy jej nie ustawi przy insercie, więc insert do `cart` wywali NOT NULL violation dopóki się tego nie ujednolici (encja albo migracja). (Bliźniaczy problem `order_items.product_id` został już usunięty migracją `V2__0.0.1_0.0.2.sql` — kolumna skasowana, bo identyfikator produktu niesie `content_hash`.)
- `OrderController` jest pusty — brak endpointu do odczytu zamówień/historii statusów.
- `ErrorCode` zawiera `BEAT_NOT_FOUND` — wygląda na wklejone z innego serwisu (marketplace), nieużywane tu.

## Build/test
- `./mvnw clean install` / `./mvnw test` w tym katalogu.
- Jedyny test to `OrderServiceApplicationTests.contextLoads()` — brak testów jednostkowych/integracyjnych dla `CartService`/`CheckoutService`.
