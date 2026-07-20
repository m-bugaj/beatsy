# discovery-server

Eureka server (`@EnableEurekaServer`), port 8761 — jedyny ze "szkieletów", który realnie coś robi (rejestr service discovery), nie tylko `Application` + test.

W praktyce ma dziś ograniczoną wartość: `api-gateway-web` ma routing do innych serwisów zahardkodowany (`localhost:port`, warianty `lb://SERVICE` zakomentowane) i `marketplace`'s Feign klienci też mają zaszyty `localhost:8083` — czyli serwisy się rejestrują w Eurece, ale nic jeszcze realnie z tego rejestru nie korzysta przy wywołaniach. Traktuj to jako dług do odkręcenia razem, nie osobno per serwis (patrz root `CLAUDE.md` / `api-gateway-web/CLAUDE.md`).
