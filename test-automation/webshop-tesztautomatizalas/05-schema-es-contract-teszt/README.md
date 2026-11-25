# 05 – JSON séma validáció és szerződésteszt

> Tananyag: *Tesztautomatizálás* (ISTQB CTAL-TAE), V. fejezet (API-tesztautomatizálási függőségek: szerződéses tesztelés, fogyasztó által vezérelt megközelítés), VIII. fejezet (séma validáció), VII. fejezet (TAF komponensek tesztelése) – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- A 04. lépés API-tesztjeit egészítsük ki:
  - **JSON séma validációval**: a válaszok szerkezetét (kötelező mezők, típusok, tartományok, tiltott extra mezők) egy-egy sémafájl írja le;
  - **fogyasztó által vezérelt szerződésteszttel**: a frontend (fogyasztó) egy JSON fájlban rögzíti, milyen kéréseket küld és a válaszból mire támaszkodik; a backend (szolgáltató) buildje ezt ellenőrzi.
- Maradjon könnyűsúlyú: Pact broker és extra keretrendszer nélkül.

## Mit mutat be?

### Séma validáció (VIII. fejezet)

- `JsonSchemaTest`: egyetlen `body(matchesJsonSchemaInClasspath("schemas/cart.json"))` sor kiváltja a mezőnkénti ellenőrzések tucatjait – a tananyag szerint „rövidebb és hatékonyabb kód”.
- A sémák (draft-04, a REST Assured `json-schema-validator` modulja ezt támogatja) a `src/test/resources/schemas/` alatt: termék, terméklista, kosár, hibaválasz, felhasználónév-válasz. A felhasználónév-sémában `oneOf` írja elő: `valid=true` esetén **nincs** `reason`, `valid=false` esetén **kötelező**.
- `schemaRejectsBrokenPayloads`: a tesztorákulumot is ellenőrizzük – a séma szándékosan hibás JSON-t elutasít.

### Szerződésteszt (V. fejezet)

```
 fogyasztó (React frontend)                 szolgáltató (webshop backend)
 ───────────────────────────                ──────────────────────────────
 1. megírja az elvárásait  ──── contracts/webshop-frontend--webshop-api.json ────►
                                             2. a build lejátssza minden interakciót
                                                a valóban futó API ellen
                                             3. eltérés → a BUILD bukik el
```

- A szerződés interakciói: kérés (metódus, útvonal, query, törzs), *provider state* (előfeltétel, pl. „a kosárban 1 db P001 van”), elvárt státusz, fejlécek, törzs-példa és `exact` szabályok.
- **Típus szerinti egyeztetés** (`JsonShapeMatcher`): a példában szereplő mezőknek létezniük kell azonos JSON-típussal; az érték csak az `exact` (JSON Pointer → érték) szabályoknál számít; extra mező megengedett (a fogyasztó csak azt rögzíti, amit használ). Ez ugyanaz az elv, amit a Pact „type matcher”-ei követnek.
- `ContractVerificationTest`: `@TestFactory` – minden interakció külön dinamikus teszt, olvasható névvel.
- `JsonShapeMatcherTest`: a TAF saját komponensét is teszteljük (VII. fejezet).

### Séma vs. szerződés – mi a különbség?

| | JSON séma | Fogyasztói szerződés |
|---|---|---|
| Ki írja? | szolgáltató (vagy közösen) | a fogyasztó |
| Mit ír le? | a válasz **teljes** szerkezetét | csak amit a fogyasztó **használ** + a hozzá vezető kéréseket és állapotokat |
| Extra mező | tiltható (`additionalProperties: false`) | megengedett |
| Mit véd? | a szolgáltató saját API-leírását | egy konkrét fogyasztót a törő változásoktól |

### Pact – az iparági eszköz

A **Pact** (pact.io) a fogyasztó által vezérelt szerződéstesztelés legelterjedtebb eszköze:

1. A fogyasztó oldali unit tesztben a Pact egy **mock szolgáltatót** indít; a fogyasztó kódja ez ellen fut, és közben a Pact rögzíti az interakciókat egy *pact fájlba* (JSON – nagyon hasonló a mi szerződésfájlunkhoz).
2. A pact fájl egy **Pact Brokerbe** (vagy PactFlow-ba) kerül, verzióval és címkékkel (pl. `main`, `prod`).
3. A szolgáltató buildje letölti a rá vonatkozó pact fájlokat, és `@Provider` / `@State` annotációkkal **lejátssza** őket (pont, amit a `ContractVerificationTest` + `ProviderStates` csinál).
4. A `can-i-deploy` parancs telepítés előtt megkérdezi a brokert: a telepítendő verzió kompatibilis-e a célkörnyezetben futó összes fogyasztóval – ez egy **telepítési minőségkapu** (V. fejezet, lásd 10. lépés).

Ebben a lépésben szándékosan saját, ~150 soros megoldást használunk: a mechanizmus így teljesen átlátható, és nincs szükség brokerre. Éles projektben a Pact (JVM: `au.com.dius.pact`) ajánlott, mert a fogyasztó oldali generálást, a verziózást és a `can-i-deploy` kaput is megadja.

## Mi változott az előző lépéshez képest?

- `pom.xml`: új `io.rest-assured:json-schema-validator` (verzió a Spring Boot BOM-ból).
- Új: `src/test/resources/schemas/*.json` (5 séma), `src/test/resources/contracts/webshop-frontend--webshop-api.json` (7 interakció).
- Új tesztek: `api/JsonSchemaTest` (5), `contract/ContractVerificationTest` (7 dinamikus), `contract/JsonShapeMatcherTest` (4) + segédosztályok `JsonShapeMatcher`, `ProviderStates`. Összesen 94 teszt.
- `AbstractApiTest` `public` lett, hogy a `contract` csomag is örökölhessen belőle.
- Az alkalmazás kódja változatlan.

## Fájlok

- `src/test/resources/schemas/` – JSON sémák.
- `src/test/resources/contracts/webshop-frontend--webshop-api.json` – a fogyasztó szerződése.
- `src/test/java/.../api/JsonSchemaTest.java` – séma validáció.
- `src/test/java/.../contract/ContractVerificationTest.java` – szolgáltató oldali verifikáció.
- `src/test/java/.../contract/JsonShapeMatcher.java`, `ProviderStates.java` – a verifikáló „mini-Pact”.
- `src/test/java/.../contract/JsonShapeMatcherTest.java` – a TAF komponens tesztje.

## Futtatás

```bash
mvn -B -q verify
```

```bash
mvn test -Dtest='ContractVerificationTest,JsonSchemaTest'
```

**Kísérlet – törő változás:** a `CartItemResponse` recordban nevezd át a `quantity` mezőt `qty`-re. A `ContractVerificationTest` két interakciója és a `JsonSchemaTest` elbukik, olvasható üzenettel (`/items/0/quantity: hiányzik`) – a hiba a backend buildjében derül ki, mielőtt a frontend eltörne.

## Továbblépés

A [06-paypal-mock-wiremock](../06-paypal-mock-wiremock/) lépés a külső PayPal API-t szimulálja WireMock-kal.
