# 04 – API-tesztek REST Assured-del

> Tananyag: *Tesztautomatizálás* (ISTQB CTAL-TAE), II. feladat (RestAssured mint API-teszteszköz), V. fejezet (API-tesztautomatizálás, tesztszintek a CI/CD-ben), VIII. feladat – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- A 03. lépés API-ját **valódi HTTP-n** teszteljük: a Spring Boot véletlen porton (`RANDOM_PORT`) elindítja a szervert, a tesztek REST Assured-del hívják.
- Ellenőrizzük a státuszkódokat, a válasz törzsét (Hamcrest matcherek, GPath), a POJO (de)szerializációt, a negatív eseteket.
- A közös beállításokat kérés- és válasz-specifikációkban (`RequestSpecification`, `ResponseSpecification`) hasznosítjuk újra.

## Mit mutat be?

- **REST Assured given / when / then** DSL – a II. feladatban értékelt „Java alapú, fejlesztőközpontú” eszköz a gyakorlatban.
- **Feketedobozos API-teszt**: a teszt csak a HTTP interfészt látja (SUT interfész – III. fejezet), nem nyúl a Spring bean-ekhez. Az előfeltételt (üres kosár) is API-n át teremti meg (`DELETE /cart`).
- **Spec-újrahasznosítás**: `AbstractApiTest.api` (alap URI, port, JSON, BigDecimal számkezelés, naplózás hibánál), `okJson`, `problem(status, errorCode)`.
- **Body matcherek**: `equalTo`, `hasSize`, `contains`, `everyItem`, `comparesEqualTo` (BigDecimal skálafüggetlen összehasonlítás), GPath: `items.find { it.productId == 'P001' }.quantity`.
- **POJO szerializáció/deszerializáció**: kérés-törzs recordból (`AddCartItemRequest`), válasz `CartResponse`, `Product[]` és a tesztoldali `Problem` recordba – utána AssertJ ellenőrzés.
- **Negatív tesztek**: határérték (0, 100), ismeretlen termék, hiányzó mező, rossz típus, ismeretlen mező, nem létező tétel, rossz HTTP metódus.
- **Naplózás hibánál** (`enableLoggingOfRequestAndResponseIfValidationFails`): sikeres futásnál csend, bukásnál a teljes kérés/válasz a kimeneten – előkészíti a VI. fejezet naplózási témáját.
- Egyszerű nem-funkcionális ellenőrzés: válaszidő `time(lessThan(2000L))`.

### MockMvc (03. lépés) vs. REST Assured + RANDOM_PORT (04. lépés)

| Szempont | MockMvc | REST Assured + RANDOM_PORT |
|---|---|---|
| Valódi HTTP szerver / hálózat | nincs | van (beágyazott Tomcat) |
| Sebesség | gyorsabb | kicsit lassabb |
| Szerializáció, szűrők, hibakezelés | igen | igen, a kliens oldali Jackson is |
| Futtatható külső (telepített) SUT ellen | nem | igen – csak a base URI-t kell átírni (09–10. lépés) |
| Tesztszint | komponensintegráció | komponensintegráció / rendszerteszt |

## Mi változott az előző lépéshez képest?

- `pom.xml`: új `io.rest-assured:rest-assured` (teszt scope; verzióját a Spring Boot BOM adja).
- Új tesztcsomag `src/test/java/.../api/`: `AbstractApiTest`, `Problem`, `UsernameApiTest`, `ProductApiTest`, `CartApiTest` (17 új teszt, összesen 78).
- Az alkalmazás kódja **változatlan** – csak tesztek kerültek hozzá.

## Fájlok

- `src/test/java/.../api/AbstractApiTest.java` – `@SpringBootTest(RANDOM_PORT)`, közös specifikációk, kosárürítés.
- `src/test/java/.../api/Problem.java` – a ProblemDetail hibaválasz tesztoldali modellje.
- `src/test/java/.../api/UsernameApiTest.java`, `ProductApiTest.java`, `CartApiTest.java` – API-tesztek.
- A többi fájl a 03. lépésből.

## Futtatás

```bash
mvn -B -q verify
```

Csak az API-tesztek:

```bash
mvn test -Dtest='*ApiTest'
```

Gyakorlás: írd át a `CartApiTest.addThenUpdateQuantity` várt végösszegét – a bukáskor a konzolon megjelenik a teljes kérés és válasz.

## Továbblépés

Az [05-schema-es-contract-teszt](../05-schema-es-contract-teszt/) lépés a válaszok szerkezetét JSON sémával és fogyasztói szerződéssel ellenőrzi.
