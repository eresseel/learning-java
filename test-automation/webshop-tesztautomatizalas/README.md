# Webshop tesztautomatizálás lépésről lépésre (ISTQB CTAL-TAE)

A *Tesztautomatizálás* tananyag (ISTQB CTAL Test Automation Engineer) gyakorlati feldolgozása tíz, egymásra épülő Java/Maven projektben. A tesztelt rendszer (SUT) a tananyag webáruháza: regisztráció (felhasználónév-ellenőrzés), termékkeresés, kosár, fizetés PayPal integrációval, Spring Boot backenddel.

Minden lépés **önálló, teljes Maven projekt**: az előző lépés másolata, kiegészítve. Minden lépés README-je leírja, mi változott az előzőhöz képest. A kód magyar kommentekkel, a tananyag fogalmaira hivatkozva magyarázza a megoldásokat. Felhasználói felület (Selenium) nincs benne – azt a repó [selenium-learning](../selenium-learning/) és [orangehrm-lepesrol-lepesre](../orangehrm-lepesrol-lepesre/) mappája mutatja be.

## Lépések és a tananyag megfeleltetése

| Lépés | Téma | Tananyag fejezet | Feladat |
|---|---|---|---|
| [01 – Felhasználónév-validátor TDD-vel](01-username-validator-tdd/) | `UsernameValidator`, határérték-elemzés (5/6/7/8), paraméterezett tesztek, TDD ciklusok | III. (megközelítések: TDD) | X. (az aktuális szabály) |
| [02 – Webshop domain](02-webshop-domain/) | `Product`, `Cart` üzleti szabályokkal, komponenstesztek | CTFL ismétlés (tesztszintek) | I., VIII. (komponens szint) |
| [03 – Spring Boot REST API](03-spring-boot-rest-api/) | `POST /validate-username`, `/products`, `/cart`; ProblemDetail; MockMvc szelet- és integrációs teszt | II. (tesztelhetőség), V. | I., VIII. |
| [04 – API-tesztek REST Assured-del](04-api-tesztek-restassured/) | `RANDOM_PORT`, státusz- és törzsellenőrzés, POJO, negatív esetek, spec-újrahasznosítás | II., V. (API-tesztautomatizálás) | II. (RestAssured), VIII. |
| [05 – Séma- és szerződésteszt](05-schema-es-contract-teszt/) | JSON séma validáció, fogyasztó által vezérelt szerződés, a Pact bemutatása | V. (szerződéses tesztelés), VIII. (séma validáció), VII. | – |
| [06 – PayPal szimulálása WireMock-kal](06-paypal-mock-wiremock/) | `PaymentClient`, `POST /checkout`; siker, 4xx, 5xx, timeout; eszközértékelés | II. (harmadik fél emulálása, eszközértékelés) | **II.** (Postman vs. RestAssured vs. Karate) |
| [07 – Adatvezérelt és kulcsszóvezérelt tesztek](07-adatvezerelt-es-kulcsszo-vezerelt/) | `@CsvFileSource`, kulcsszó-táblázat (`ADD_TO_CART`, `UPDATE_QTY`, `CHECK_TOTAL`) és futtató | III. (DDT, KDT, megközelítések) | **IV.** |
| [08 – TAF rétegek és tervezési minták](08-taf-retegek-mintak/) | tesztszkript → üzleti logika → alapkönyvtár; Facade, Singleton, (POM) | III. (TAF rétegek, tervezési minták) | **III.**, **V.** |
| [09 – Konfiguráció, naplózás, hibaelemzés](09-konfiguracio-naplozas-hibaelemzes/) | env/system property konfiguráció, preflight, korrelációs ID, Passed/Failed/TAS error/Inconclusive, a X. feladat elemzése | V. (tesztver konfiguráció), VI., VII. | **IX.**, **X.** (A, B és elemzés) |
| [10 – CI pipeline](10-ci-pipeline/) | GitHub Actions; Surefire (`*Test`) vs. Failsafe (`*IT`), `build-gate` / `deploy-gate` profilok; elméleti feladatok | IV., V. (build vs. telepítés), VIII. | **VIII.**, VI., VII., XI., XII., XIII. |

Az I. feladat (a SUT elemzése) a 02–03. lépésben kel életre: a webáruház funkciói (regisztráció, keresés, kosár, PayPal fizetés) és rétegei (Spring Boot backend, adatbázis helyett memóriabeli repository, külső PayPal API – WireMock-kal emulálva). A React frontend és a MySQL adatbázis szándékosan nem része a demónak.

## Közös futtatási útmutató

Előfeltétel: JDK 21 vagy újabb, Maven 3.9+. Internet csak az első függőségletöltéshez kell; a tesztek offline futnak (a külső PayPal-t WireMock helyettesíti).

Egy lépés ellenőrzése (minden lépésnek zöldnek kell lennie):

```bash
cd 04-api-tesztek-restassured
mvn -B -q verify
```

A 03. lépéstől az alkalmazás el is indítható (`http://localhost:8080`):

```bash
mvn spring-boot:run
```

Az összes lépés egymás után:

```bash
for d in [0-9][0-9]-*/; do (cd "$d" && echo "== $d" && mvn -B -q verify) || break; done
```

## Verziók

Java 21 (nyelvi szint), JUnit 6.1.3 (01–02. lépés), Spring Boot 3.5.16 (03. lépéstől; a JUnit, a REST Assured és a json-schema-validator verzióját a Boot BOM adja), WireMock 3.13.2 (`wiremock-standalone`), maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.1 (01–02. lépés).

A mintaadatok (termékek, árak, tesztadatok) saját készítésűek, az eredeti tesztadatbázis nem volt a tananyag része.
