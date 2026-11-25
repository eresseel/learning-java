# 02 – Webshop domain: termék, katalógus, kosár

> Tananyag: *Tesztautomatizálás* (ISTQB CTAL-TAE), I. feladat (a SUT: webáruház), VIII. feladat (a „Kosár” funkció tesztszintjei) – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Az I. feladat webáruháza lehetővé teszi a termékek keresését és a kosárba helyezést.
- A VIII. feladat szerint a „Kosár” funkció felel a termékek kosárba helyezéséért és a mennyiségük frissítéséért; a csapat CI/CD pipeline-t épít, és minden tesztszinten automatizálni akar.
- Ebben a lépésben a **legalsó szintet** készítjük el: a domain-osztályokat és a **komponensteszteket**.

## Mit mutat be?

- A kosár üzleti szabályainak **egyértelmű, tesztelhető megfogalmazása** – a tesztorákulum alapja (a tananyag szerint az automatizálás „csak azt tudja tesztelni, amire programozták”).
- Komponensteszt (fehérdobozos, a lokális fejlesztői és a build környezetben fut).
- Határérték-elemzés a mennyiségre (0 / 1 / 99 / 100), ekvivalenciaosztályok (ismert / ismeretlen termék).
- `assertAll` (minden ellenőrzés lefut), `assertThrows` (kivétel és üzenet ellenőrzése), `@Nested` csoportok.
- Tervezés tesztelhetőségre: a `Cart` egy `ProductCatalog` **interfésztől** függ, nem egy konkrét adatbázistól (SOLID – függőség-inverzió), ezért adatbázis nélkül tesztelhető.
- `BigDecimal` pénzösszegekhez (nincs lebegőpontos kerekítési hiba).

### A kosár szabályai (saját meghatározás)

| # | Szabály | Megsértésekor |
|---|---|---|
| 1 | Egy tétel mennyisége 1..99 | `InvalidQuantityException` |
| 2 | Csak katalógusban létező termék tehető a kosárba | `UnknownProductException` |
| 3 | Ugyanaz a termék újra hozzáadva: a mennyiségek összeadódnak; ha az összeg > 99, elutasítás, **a kosár változatlan** | `InvalidQuantityException` |
| 4 | Mennyiség-módosítás (felülírás) és törlés csak kosárban lévő tételre; módosításnál a 0 sem megengedett (arra a törlés való) | `ItemNotInCartException` / `InvalidQuantityException` |
| 5 | Végösszeg = Σ egységár × mennyiség, 2 tizedesjegyre; üres kosárnál 0.00 | – |
| 6 | A termék azonosítója és neve kötelező, az ára pozitív | `IllegalArgumentException` |
| 7 | Keresés: kis-/nagybetűtől független részszöveg a névben, üres keresés = minden termék | – |

A mintakatalógus (6 termék, forintárak) saját készítésű, az eredeti adatbázis nem volt a tananyag része.

## Mi változott az előző lépéshez képest?

- Új csomag: `domain` – `Product`, `ProductCatalog` (interfész), `InMemoryProductCatalog`, `Cart`, `CartItem` és a kivételek (`WebshopException` ős + 3 alosztály).
- Új tesztek: `ProductTest`, `InMemoryProductCatalogTest`, `CartTest` (összesen 32 új teszteset).
- A `validation` csomag és a tesztjei változatlanok (52 teszt fut összesen).
- A `pom.xml`-ben csak az `artifactId` és a leírás változott – még mindig nincs keretrendszer.

## Fájlok

- `src/main/java/.../domain/Product.java` – termék (record, invariáns-ellenőrzéssel).
- `src/main/java/.../domain/ProductCatalog.java`, `InMemoryProductCatalog.java` – katalógus (repository) és memóriabeli megvalósítás mintaadattal.
- `src/main/java/.../domain/Cart.java`, `CartItem.java` – kosár és tétel.
- `src/main/java/.../domain/*Exception.java` – üzleti hibák.
- `src/main/java/.../validation/*` – az 01. lépés validátora.
- `src/test/java/.../domain/*Test.java` – komponenstesztek.

## Futtatás

```bash
mvn -B -q verify
```

```bash
mvn test
```

Várt eredmény: 4 tesztosztály, 52 teszteset, mind zöld.

## Továbblépés

A [03-spring-boot-rest-api](../03-spring-boot-rest-api/) lépésben ezt a domaint REST API-n keresztül tesszük elérhetővé.
