# 06 – A PayPal szimulálása WireMock-kal

> Tananyag: *Tesztautomatizálás* (ISTQB CTAL-TAE), I. feladat (külső API: PayPal), II. fejezet (harmadik féltől származó alkalmazások emulálása), **II. feladat** (Postman / RestAssured / Karate összehasonlítás) – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- A webáruház fizetése PayPal integrációval történik (I. feladat). Készítsünk:
  - `PaymentClient`-et, amely a külső „PayPal” API-t hívja (`POST /v2/checkout/orders` – a PayPal Orders v2 API leegyszerűsített mása);
  - `POST /checkout` végpontot: a kosár végösszegét kifizeti, siker esetén kiüríti a kosarat.
- A teszteléshez a valódi PayPal helyett **WireMock**-ot használunk: siker, 4xx (elutasítás), 5xx (szolgáltatói hiba), időtúllépés, kapcsolatbontás.
- **II. feladat**: töltsük ki az eszközértékelő táblázatot a PayPal API teszteléséhez szóba jött három eszközre (lásd lent).

## Mit mutat be?

- **Külső függőség emulálása** (II. fejezet: „harmadik féltől származó alkalmazások emulálása”): a hibaágak (4xx, 5xx, timeout) a valódi szolgáltatóval szinte kipróbálhatatlanok – WireMock-kal determinisztikusak és gyorsak.
- **Stub** (`stubFor(...)`) és **verify** (`verify(1, postRequestedFor(...))`): nem csak a válasz feldolgozását, hanem a *kimenő* kérés tartalmát (fejléc, JSON törzs) is ellenőrizzük.
- Két tesztszint:
  - `PaymentClientWireMockTest` – a kliens önmagában, **Spring nélkül** (komponensintegráció);
  - `CheckoutApiTest` – a teljes webshop valódi HTTP-n (REST Assured) + WireMock mögötte (rendszerintegrációs határ).
- **Konfigurálhatóság**: a PayPal címe és időkorlátja `paypal.*` beállításból jön; a teszt `@DynamicPropertySource`-szal a WireMock véletlen portjára irányítja.
- Hibaleképezés és üzleti szabály: sikertelen fizetésnél a kosár **megmarad**.
- Technikai részlet: a JDK `HttpClient` + WireMock (Jetty 12) párosnál `http2PlainDisabled(true)` kell.

### Hibaleképezés

| PayPal (WireMock) viselkedése | `PaymentClient` kivétel | `POST /checkout` válasz | Kosár |
|---|---|---|---|
| 200 + `status: COMPLETED` | – | 201 `status: PAID` | kiürül |
| 4xx (pl. 422 `INSTRUMENT_DECLINED`) | `PaymentDeclinedException` | 402 `PAYMENT_DECLINED` | megmarad |
| 5xx | `PaymentProviderException` | 502 `PAYMENT_PROVIDER_ERROR` | megmarad |
| kapcsolatbontás, nem elérhető | `PaymentProviderException` | 502 `PAYMENT_PROVIDER_ERROR` | megmarad |
| 200, de nem `COMPLETED` | `PaymentProviderException` | 502 | megmarad |
| késleltetett válasz > `paypal.timeout` | `PaymentTimeoutException` | 504 `PAYMENT_TIMEOUT` | megmarad |
| (üres kosár – PayPal nincs hívva) | – | 409 `EMPTY_CART` | – |

## II. feladat – eszközértékelés: Postman vs. RestAssured vs. Karate (a PayPal API teszteléséhez)

A tananyag szempontjai (13. dia): technológia és IDE, konfigurálhatóság, tesztadatok kezelhetősége, teszttípusonkénti eltérések, riportálás, integrálhatóság, bővíthetőség.

| Szempont | Postman | RestAssured | Karate |
|---|---|---|---|
| **Jelleg** | Grafikus API-kliens és tesztelő; a tesztek JavaScript szkriptek a kérések mellett | Java könyvtár (DSL: `given/when/then`), fejlesztőközpontú | BDD-stílusú keretrendszer saját Gherkin-szerű DSL-lel (`.feature` fájlok), JVM-en fut |
| **Technológia, IDE** | Saját asztali/webes alkalmazás; CLI futtató: Newman / Postman CLI | Bármely Java IDE (IntelliJ, Eclipse), Maven/Gradle, JUnit/TestNG | Java IDE + `.feature` szerkesztő plugin; Maven/Gradle, JUnit 5 futtató |
| **Szükséges tudás** | Alacsony belépési küszöb, manuális tesztelők is használják | Java programozási ismeret | Kevés programozás; a DSL-t meg kell tanulni, komplex logikához JS/Java |
| **Konfigurálhatóság** | Környezetek (environment) és változók; titkok a vaultban | Teljes Java rugalmasság: property-k, profilok, környezeti változók, Spring integráció | `karate-config.js`, környezetenkénti konfiguráció (`karate.env`) |
| **Tesztadatok kezelése** | CSV/JSON adatfájl a Collection Runnerben / Newmanben | JUnit paraméterezés (`@CsvFileSource`, `@MethodSource`), bármilyen Java adatforrás | Beépített `Examples` táblák, JSON/CSV beolvasás, JSON-natív kezelés |
| **Teszttípusok** | Funkcionális API, egyszerű monitorozás, mock szerver; terheléshez korlátozott | Funkcionális API, integrációs, szerződés (+ json-schema-validator); UI nincs | Funkcionális API, **beépített mock szerver**, teljesítményteszt (Gatling integráció), UI (Karate UI) |
| **Riportálás** | Beépített futási nézet; Newman reporterek (HTML, JUnit XML) | JUnit/Surefire XML, Allure, ExtentReports integrációval | Beépített, részletes HTML riport kérés/válasz naplóval; JUnit XML |
| **Integrálhatóság (CI/CD)** | Newman/Postman CLI parancssorból, bármely CI-ban | Natív: `mvn verify`, bármely CI; a SUT kódjával együtt verziózható | Natív Maven/Gradle futtatás, párhuzamos végrehajtás |
| **Bővíthetőség** | Szkriptek, de az architektúra kevésbé bővíthető | Korlátlan: saját szűrők, spec-ek, TAF rétegek (08. lépés), tervezési minták | Java/JS hívható a feature-ökből; egyedi lépések korlátozottabbak |
| **Verziókezelés, karbantartás** | JSON export; diffelése, review-ja nehézkes | Forráskód → kódáttekintés, refaktorálás, statikus elemzés | Szöveges `.feature` fájlok → jól verziózható |
| **Licenc** | Freemium (csapatfunkciók fizetősek) | Nyílt forrású (Apache 2.0) | Nyílt forrású (MIT) |
| **Ajánlott, ha...** | Feltáró API-tesztelés, gyors ellenőrzés, nem fejlesztő tesztelők | Java/Spring csapat, a tesztek a backend kódja mellett élnek, CI-ban futnak | Vegyes csapat, BDD-szemlélet, üzleti olvashatóság fontos, beépített mock kell |

**Javaslat a tananyag webáruházához** (Spring Boot backend, Java csapat): elsődleges eszköz a **RestAssured** (a tesztek a backend repójában, Maven-nel, CI-ban, a SUT-tal együtt verziózva – V. fejezet: „Teszteszközök és SUT együtt verziózva”), a PayPal szimulálására **WireMock**. A **Postman** a feltáró teszteléshez és a PayPal sandbox kézi kipróbálásához hasznos kiegészítő. **Karate** akkor jó választás, ha az üzleti elemzők is írnak/olvasnak teszteket (BDD), vagy ha egy eszközben kell API + mock + terhelés.

## Mi változott az előző lépéshez képest?

- `pom.xml`: új `org.wiremock:wiremock-standalone` 3.13.2 (teszt scope).
- Új főkód: `payment/` (`PaymentClient`, `PaypalProperties`, PayPal DTO-k, 3 kivétel + közös ős), `config/PaymentConfig`, `service/CheckoutService`, `web/CheckoutController`, `web/dto/CheckoutResponse`, `domain/EmptyCartException`.
- `ApiExceptionHandler`: 4 új hibaleképezés (409, 402, 502, 504); a `problem(...)` segédmetódus szöveges `detail`-t kap.
- `application.properties`: `paypal.base-url`, `paypal.timeout`.
- Új tesztek: `payment/PaymentClientWireMockTest` (6), `api/CheckoutApiTest` (5); új séma `schemas/checkout.json`, a `problem.json` hibakód-listája bővült. Összesen 105 teszt.

## Fájlok

- `src/main/java/.../payment/PaymentClient.java` – a PayPal kliens (RestClient + JDK HttpClient, timeout, hibaleképezés).
- `src/main/java/.../service/CheckoutService.java`, `web/CheckoutController.java` – a fizetési folyamat.
- `src/test/java/.../payment/PaymentClientWireMockTest.java` – a kliens WireMock-tesztje Spring nélkül.
- `src/test/java/.../api/CheckoutApiTest.java` – végpontteszt REST Assured + WireMock.

## Futtatás

```bash
mvn -B -q verify
```

```bash
mvn test -Dtest='PaymentClientWireMockTest,CheckoutApiTest'
```

Az alkalmazás futtatásakor a PayPal címe környezeti változóval adható meg (Spring relaxed binding):

```bash
PAYPAL_BASE_URL=http://localhost:8089 mvn spring-boot:run
```

## Továbblépés

A [07-adatvezerelt-es-kulcsszo-vezerelt](../07-adatvezerelt-es-kulcsszo-vezerelt/) lépés adatvezérelt (CSV) és kulcsszóvezérelt teszteket ad hozzá.
