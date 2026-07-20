# file-upload

Nieużywany szkielet — mimo klasyfikacji "mały" w root `CLAUDE.md`, w praktyce nie różni się od pustych serwisów (`discovery-server`, `payment-service` itd.): jest tylko `FileUploadApplication` (`@SpringBootApplication`, brak nawet portu/configu w `resources/` — folder `resources` nie istnieje).

`pom.xml` ma już `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `postgresql`, `spring-boot-devtools` — więc jest przygotowany pod storage plików (prawdopodobnie do obsługi uploadu bitów/coverów, komplementarnie do `content-service`, który obecnie przyjmuje tylko JSON metadanych bez samego pliku — patrz `content/CLAUDE.md`), ale nie ma ani jednego kontrolera/serwisu/encji.

Jedyna osobliwość: dwie klasy testowe `contextLoads()` w różnych pakietach (`com.beatstore.file_upload` i `com.beatstore.fileupload`) — duplikat po literówce w nazwie pakietu, nieszkodliwy, ale do posprzątania przy pierwszym realnym PR na ten serwis.

Pełne `/init`/eksploracja nie ma sensu, dopóki ktoś nie zacznie tu realnie kodować — wtedy przepisać ten plik od zera.
