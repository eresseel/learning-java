# 03 – A webshop REST API-ja (Spring Boot)

> Tananyag: *Tesztautomatizálás* (ISTQB CTAL-TAE), I. feladat (SUT: Spring Boot backend), II. fejezet (tesztelhetőség: megfigyelhetőség, irányíthatóság), V. fejezet / VIII. feladat (tesztszintek) – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- A tananyag webáruházának backendje Java – Spring Boot. Tegyük elérhetővé REST API-n a 01–02. lépés logikáját:
  - `POST /validate-username` – a X. feladatban naplózott hívás (`admin12` → `{"valid": true}`);
  - `GET /products`, `GET /products?q=...`, `GET /products/{id}` – termékek listázása, keresése;
  - `POST /cart/items`, `PATCH /cart/items/{id}`, `DELETE /cart/items/{id}`, `GET /cart`, `DELETE /cart` – kosár.
- Adatbázis helyett memóriabeli repository (a tananyag SUT-ja MySQL-t használ – ezt a demóban kiváltjuk).
- Egységes hibaválaszok **ProblemDetail** (RFC 9457) formátumban.
- Tesztek MockMvc-vel: szelet (slice) teszt és integrációs teszt.

## Mit mutat be?

- **Tesztelhetőségre tervezés** (II. fejezet):
  - *megfigyelhetőség*: minden módosító végpont visszaadja a kosár új állapotát; a hibák géppel olvasható `errorCode`-ot kapnak;
  - *irányíthatóság*: `DELETE /cart` – a tesztek determinisztikus kiinduló állapotot tudnak teremteni.
- A domain keretrendszer-független marad (`WebshopConfig` „drótozza be”), így a 01–02. lépés komponenstesztjei változatlanul, gyorsan futnak.
- **Tesztszintek** egy projekten belül (VIII. feladat):

| Szint | Teszt | Mi indul el? | Futásidő |
|---|---|---|---|
| Komponens | `CartTest`, `UsernameValidatorTest`, ... | csak a Java osztály | ms |
| Komponensintegráció (webréteg szelet) | `UsernameControllerWebMvcTest` | controller + JSON + hibakezelő, MockMvc | ~0,3 s |
| Komponensintegráció (teljes kontextus) | `CartApiIntegrationTest` | teljes Spring kontextus, HTTP szerver nélkül | ~3 s (első indítás) |

- HTTP szemantika: 201 Created (hozzáadás), 204 No Content (törlés), 400 / 404 hibák.

### API összefoglaló

| Metódus és útvonal | Törzs | Siker | Hibák (`errorCode`) |
|---|---|---|---|
| `POST /validate-username` | `{"username":"admin12"}` | 200 `{"valid":true}` vagy `{"valid":false,"reason":"..."}` | 400 `MALFORMED_REQUEST` |
| `GET /products?q=egér` | – | 200 terméklista | – |
| `GET /products/{id}` | – | 200 termék | 404 `UNKNOWN_PRODUCT` |
| `GET /cart` | – | 200 kosár | – |
| `POST /cart/items` | `{"productId":"P001","quantity":2}` | 201 kosár | 400 `VALIDATION_ERROR` / `INVALID_QUANTITY`, 404 `UNKNOWN_PRODUCT` |
| `PATCH /cart/items/{productId}` | `{"quantity":5}` | 200 kosár | 400 `INVALID_QUANTITY`, 404 `ITEM_NOT_IN_CART` |
| `DELETE /cart/items/{productId}` | – | 204 | 404 `ITEM_NOT_IN_CART` |
| `DELETE /cart` | – | 204 | – |

Kosár válasz példa:

```json
{"items":[{"productId":"P001","name":"Vezeték nélküli egér","unitPrice":7990.00,"quantity":2,"lineTotal":15980.00}],
 "itemCount":2,"total":15980.00,"currency":"HUF"}
```

Hibaválasz példa (`Content-Type: application/problem+json`):

```json
{"type":"about:blank","title":"Ismeretlen termék","status":404,"detail":"Ismeretlen termék: P999",
 "instance":"/cart/items","errorCode":"UNKNOWN_PRODUCT"}
```

### Saját döntések

- Egyetlen, közös kosár (nincs bejelentkezés) – szálbiztos `CartService`-szel.
- Az érvénytelen felhasználónév **nem hiba**, hanem üzleti válasz: 200 + `valid:false`.
- A mennyiség tartományát nem Bean Validation, hanem a domain ellenőrzi (egy helyen él a szabály); a Bean Validation csak a hiányzó mezőt szűri.
- Ismeretlen JSON mező a kérésben → 400 (`fail-on-unknown-properties=true`).

## Mi változott az előző lépéshez képest?

- `pom.xml`: Spring Boot 3.5.16 szülő, `spring-boot-starter-web`, `-validation`, `-test`; a JUnit verzióját mostantól a Boot adja.
- Új: `WebshopApplication`, `config/WebshopConfig`, `service/CartService`, `web/*Controller`, `web/ApiExceptionHandler`, `web/dto/*`, `application.properties`.
- Új tesztek: `web/UsernameControllerWebMvcTest` (3), `web/CartApiIntegrationTest` (6) – összesen 61 teszt.
- A `domain` és `validation` csomag változatlan.

## Fájlok

- `src/main/java/.../WebshopApplication.java` – belépési pont.
- `src/main/java/.../config/WebshopConfig.java` – domain-objektumok bean-ként.
- `src/main/java/.../service/CartService.java` – a közös kosár szálbiztos kezelése.
- `src/main/java/.../web/` – controllerek, hibakezelő, DTO-k.
- `src/test/java/.../web/` – MockMvc tesztek.

## Futtatás

```bash
mvn -B -q verify
```

Az alkalmazás elindítása és kipróbálása:

```bash
mvn spring-boot:run
```

```bash
curl -s -X POST localhost:8080/validate-username -H 'Content-Type: application/json' -d '{"username":"admin12"}'
curl -s 'localhost:8080/products?q=egér'
curl -s -X POST localhost:8080/cart/items -H 'Content-Type: application/json' -d '{"productId":"P001","quantity":2}'
curl -s -X PATCH localhost:8080/cart/items/P001 -H 'Content-Type: application/json' -d '{"quantity":5}'
curl -s localhost:8080/cart
```

## Továbblépés

A [04-api-tesztek-restassured](../04-api-tesztek-restassured/) lépés valódi HTTP-n, REST Assured-del teszteli ugyanezt az API-t.
