# Plan wdrożenia Claude Code w projekcie Beatsy

> Dokument roboczy, nie instrukcje dla agenta (to nie jest `CLAUDE.md`). Cel: rozłożyć w czasie onboarding Claude Code do repo i zoptymalizować wykorzystanie limitów planu Pro.
>
> Utworzono: 2026-07-20

## Kontekst / ustalenia wyjściowe

- Prawdziwy root gita to `beatsy/beatsy/` — zawiera `backend/` (`bff`, `gateway`, `microservices`), `frontend/`, `devops/`, `infrastructure/`.
- Claude Code wczytuje pliki `CLAUDE.md` idąc w górę drzewa katalogów od cwd aż do roota — więc `CLAUDE.md` w `beatsy/beatsy/` jest widoczny nawet z sesji otwartej głębiej (np. w `backend/microservices/order-service`). Można więc budować hierarchię plików `CLAUDE.md`, a nie trzymać wszystko w jednym.
- 12 katalogów w `backend/microservices`, bardzo nierówno rozwiniętych (stan na dzień utworzenia planu):
  - **Duże/aktywne**: `marketplace` (67 plików .java), `auth-service` (43), `order-service` (38), `content` (35)
  - **Małe**: `user-service` (12), `api-gateway-web` (8), `file-upload` (3)
  - **Puste szkielety** (tylko klasa `Application` + test): `discovery-server`, `fulfillment-service`, `license-service`, `payment-service`, `subscriptions`
- Każdy mikroserwis ma własny, niezależny `pom.xml` (brak wspólnego reactora Mavenowego) — nadają się do indeksowania osobno, sesja po sesji.
- Ostatnie commity dotyczą `order-service` / koszyka — to bieżący obszar pracy i naturalny priorytet.
- W repo nie ma jeszcze żadnego pliku `CLAUDE.md`.

## Co właściwie oznacza "indeksowanie" w Claude Code

Claude Code nie ma trwałego indeksu wektorowego — każda sesja czyta pliki na żądanie (Glob/Grep/Read). "Indeksowanie" w praktyce to **jednorazowa inwestycja**: sesja, w której Claude eksploruje serwis (np. przez wbudowany skill `/init`) i zapisuje wnioski do `CLAUDE.md`. Ten plik ładuje się potem automatycznie w każdej kolejnej sesji — płacimy tokenami raz, korzystamy wielokrotnie.

**Pułapka**: `CLAUDE.md` jest wstrzykiwany do kontekstu przy *każdej* wiadomości w sesji, więc zbyt szczegółowy plik (pełny opis każdej klasy) na stałe zjada budżet tokenów. Cel: krótki, gęsty plik — architektura, konwencje, gotchas, komendy build/test — a nie dokumentacja API 1:1 z kodem. Orientacyjnie: root ~50-80 linii, per-serwis ~100-150 linii.

## Harmonogram

### Dzień 0 — przygotowanie
- Root `beatsy/beatsy/CLAUDE.md` — cienki plik orientacyjny: mapa serwisów, porty, tech stack, kto z kim gada (Eureka, JWT/RSA256, gateway).
- `.claude/settings.json` z whitelistą typowych komend (mvn, git status itp.), żeby ograniczyć prompty o zgodę — skill `fewer-permission-prompts`.
- Szybki przegląd `.gitignore` / katalogów `target/`, żeby Claude nie skanował artefaktów builda.

### Dni 1-4 — jeden aktywny serwis dziennie
Dla każdego: `/init` (lub prowadzona eksploracja) + ręczna redukcja wyniku do rozsądnej długości.

1. **`order-service`** (najbardziej aktywny wg historii commitów)
2. **`marketplace`** (największy)
3. **`auth-service`**
4. **`content`**

### Dzień 5 — mniejsze serwisy razem
`user-service` + `api-gateway-web` + `file-upload` w jednej sesji — wystarczająco małe, nie potrzebują osobnego dnia.

### Dzień 6 — puste szkielety (opcjonalnie, można pominąć)
`discovery-server`, `fulfillment-service`, `license-service`, `payment-service`, `subscriptions` — obecnie puste projekty. Pełne `/init` to strata budżetu. Lepiej: `CLAUDE.md` w 2 zdania ("szkielet, do zbudowania") albo poczekać, aż realnie zacznie się w nich kodować.

### Dzień 7 — spięcie całości
`backend/gateway`, `backend/bff` + dopisanie do root `CLAUDE.md` wzorców cross-service (auth flow, discovery, komunikacja między serwisami).

## Nawyki na później

- **Nowa sesja na serwis / zadanie**, nie jeden maraton — długa sesja wymusza auto-compact, co samo kosztuje tokeny i gubi precyzję.
- Do szerokich, eksploracyjnych pytań ("gdzie jest X w całym repo") używać subagenta `Explore` zamiast grzebać ręcznie w głównym wątku — trzyma śmieci poza głównym kontekstem.
- `CLAUDE.md` odświeżać tylko gdy architektura realnie się zmienia, nie "co sesję".
- Limit planu Pro to okno czasowe / liczba wiadomości, nie cena za token — realny zysk to mniej powtarzanej eksploracji, nie mikrooszczędzanie na pojedynczych promptach.

## Status wykonania

- [x] Dzień 0 — root CLAUDE.md + settings.json (2026-07-20)
- [ ] Dzień 1 — order-service
- [ ] Dzień 2 — marketplace
- [ ] Dzień 3 — auth-service
- [ ] Dzień 4 — content
- [ ] Dzień 5 — user-service, api-gateway-web, file-upload
- [ ] Dzień 6 — szkielety (opcjonalnie)
- [ ] Dzień 7 — gateway, bff, spięcie root CLAUDE.md
