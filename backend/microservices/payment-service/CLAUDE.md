# payment-service

Szkielet, port 8087 (dev) — tylko `PaymentServiceApplication` + test `contextLoads()`, brak logiki biznesowej. `Order` w `order-service` ma już pola `paymentProvider`/`paymentReference`/`PaymentStatus` gotowe pod integrację (patrz `order-service/CLAUDE.md`) — to naturalny punkt zaczepienia, gdy ten serwis zacznie być implementowany.
