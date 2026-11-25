# Java 17 + JUnit 5 + Selenium tanulóprojekt

Ez a repository számozott, egymásra épülő gyakorlófeladatokon keresztül mutatja be a Java-alapú tesztelést, a Selenium WebDriver használatát, a Maven projektkezelést, a Maven Surefire tesztfuttatót és a Page Object Model (POM) tervezési mintát.

## Technológiai stack

- Java 17
- Maven
- JUnit 5
- Selenium WebDriver 4
- Maven Surefire Plugin
- Page Object Model

A verziók a `pom.xml` fájlokban vannak rögzítve a reprodukálható build érdekében.

## Előfeltételek

- JDK 17 telepítve
- Maven 3.8+ (ajánlott a projekted által használt újabb Maven-verzió)
- Google Chrome telepítve a Selenium-feladatokhoz
- Internetkapcsolat az első Maven build során, hogy a függőségek letöltődjenek

Ellenőrzés:

```bash
java -version
mvn -version
```

A Java-verziónak 17-esnek vagy kompatibilis 17-es futtatókörnyezetnek kell lennie. Ha több JDK van telepítve, állítsd be a `JAVA_HOME` változót a JDK 17-re.

## Felépítés

| Könyvtár | Téma | Új készség |
|---|---|---|
| `01-junit-basics` | Első JUnit 5 tesztek | `@Test`, `assertEquals`, `assertTrue` |
| `02-assertions-and-negative-cases` | Elvárt hibák és határesetek | `assertThrows`, null/üres bemenet |
| `03-parameterized-tests` | Paraméterezett tesztelés | `@ParameterizedTest`, `@CsvSource` |
| `04-selenium-first-test` | Első böngészőteszt | WebDriver, böngésző-életciklus |
| `05-selenium-locators` | Elemkeresés és ellenőrzések | ID, CSS selector, XPath, explicit wait |
| `06-selenium-form-workflow` | Űrlap és felhasználói folyamat | kitöltés, kattintás, validáció |
| `07-page-object-model` | Page Object Model | Page osztályok, felelősségek szétválasztása |
| `08-pom-test-data-and-reuse` | Újrafelhasználható POM | tesztadatok, több teszt, stabilabb felépítés |
| `09-api-restassured-basics` | Első API tesztek | REST Assured, given-when-then, GET, queryParam |
| `10-api-crud-and-pojo` | CRUD és API kliens osztály | POST/PUT/PATCH/DELETE, POJO, RequestSpecification |
| `11-api-negative-and-contract` | Negatív és szerződés-tesztek | 404/500, JSON séma, válaszidő |
| `12-api-mock-wiremock` | Külső API szimulálása | WireMock stub, hibaszimuláció, verify |
| `13-selenium-advanced-elements` | Haladó Selenium elemek | Select, checkbox, alert, iframe, új ablak |
| `14-test-organization-and-config` | Tesztszervezés és konfiguráció | @Tag, @Nested, Config, Maven profil |
| `15-screenshot-on-failure` | Képernyőkép elbukott tesztnél | JUnit 5 extension, AfterTestExecutionCallback |
| `16-parallel-execution` | Párhuzamos futtatás | ThreadLocal, DriverFactory, junit-platform.properties |
| `17-hybrid-api-ui-capstone` | Hibrid API + UI zárófeladat | API és UI réteg kombinálása |

Minden szint önálló Maven-projekt. Így egy-egy szint külön is megnyitható IDE-ben, és nem függ a többi könyvtártól.

## Futtatás

Lépj be a kiválasztott szint könyvtárába, majd futtasd:

```bash
mvn clean test
```

Csak a JUnit-alapú első három szinthez nem kell böngésző. A 4–8. és a 13–17. szintekhez Chrome szükséges. A 9–12. szintekhez és a 17. szint API részéhez internetkapcsolat kell (JSONPlaceholder). A 12. szint WireMock tesztjei offline is futnak.

A tesztfuttatás eredménye a terminálban jelenik meg, a részletes Surefire riportok pedig itt találhatók:

```text
target/surefire-reports/
```

A tesztosztályok neve `*Test.java` mintát követ, hogy a Maven Surefire alapértelmezett tesztfelderítése felismerje őket.

## Javasolt tanulási sorrend

1. Olvasd el az adott mappa `README.md` fájlját.
2. Futtasd a megoldást: `mvn clean test`.
3. Olvasd el a kommenteket a forrásban és a tesztekben.
4. Módosíts egy elvárást vagy adj hozzá új tesztet.
5. Futtasd újra a teszteket, és figyeld meg, hogyan változik az eredmény.
6. Csak ezután lépj tovább a következő szintre.

## Megjegyzés a Seleniumhoz

A Selenium Manager a támogatott környezetekben automatikusan megpróbálja kezelni a megfelelő böngésző-illesztőprogramot. Ehhez az első futtatáskor internetkapcsolat szükséges lehet. Céges proxy vagy szigorú hálózati szabályok esetén a driver letöltése külön konfigurációt igényelhet.

A tesztek helyi HTML-fájlokat nyitnak meg, ezért nem igényelnek külső webalkalmazást. A böngésző grafikus felülettel indul; headless futtatásra a későbbi feladatokban lehet továbbfejleszteni a konfigurációt.

## Fogalmak röviden

- **JUnit 5:** tesztek írására és futtatására szolgáló tesztkeretrendszer.
- **Selenium WebDriver:** böngészőautomatizálási API.
- **Maven:** függőségkezelés és buildfolyamat.
- **Surefire:** a Maven `test` fázisában a tesztek futtatásáért és riportolásáért felelős plugin.
- **Page Object Model:** tervezési minta, amely az oldal elemeinek és műveleteinek kezelését külön osztályokba szervezi.
- **REST Assured:** given-when-then szerkezetű HTTP API tesztelési könyvtár.
- **WireMock:** programozható helyi HTTP szerver, külső API-k szimulálásához.
- **ThreadLocal:** Java mechanizmus, amellyel szálankénti külön értéket tárolhatunk (párhuzamos tesztekhez).
- **JUnit 5 extension:** a tesztek életciklusába beavatkozó kiegészítő (pl. képernyőkép-készítés).

## Fontos

A projekt megoldott példákat tartalmaz tanulási céllal. A ZIP elkészítésekor a források és konfigurációk kerültek létrehozásra; a tényleges tesztfuttatás a te gépeden, a telepített JDK/Maven/Chrome környezetben történik.
