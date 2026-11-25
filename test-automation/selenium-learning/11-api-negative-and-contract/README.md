# 11 – Negatív esetek, JSON séma és szerződés-tesztek

**Nehézség:** ★★★☆☆

## Cél
Az API tesztek kiterjesztése a hibás bemenetekre és a válasz szerkezetének ellenőrzésére.

## Feladat
- Ellenőrizd, hogy nem létező erőforrásra és nem létező végpontra `404` jön.
- Futtasd ugyanazt a negatív tesztet több hibás azonosítóval.
- Ellenőrizd a válasz szerkezetét JSON sémával (`schemas/post-schema.json`).
- Ellenőrizd, hogy a válaszidő egy határon belül marad.
- Végezz adatvezérelt ellenőrzést `@CsvSource`-szal.

## Mit tanulunk?
- pozitív és negatív tesztek különbsége API szinten;
- a `@ParameterizedTest` használata API hívásokkal;
- JSON Schema validáció (`matchesJsonSchemaInClasspath`): a szerkezet automatikus ellenőrzése;
- válaszidő mérése (`time(lessThan(...))`) és annak korlátai nyilvános szolgáltatásnál.

## A séma fájlról
A `post-schema.json` nem tartalmazhat kommentet (a JSON nem támogatja), ezért itt magyarázzuk: az objektumnak kötelezően van `userId`, `id`, `title` és `body` mezője; az első kettő egész szám, a második kettő szöveg.

## Futtatás
```bash
cd 11-api-negative-and-contract
mvn clean test
```
Internetkapcsolat szükséges (nyilvános JSONPlaceholder).

## Továbbfejlesztési feladat
1. Írj sémát a `/users/1` válaszra, és tedd hozzá a tesztet.
2. Tedd a sémát szigorúbbá: `"additionalProperties": false`. Mi történik, ha a szerver új mezőt vezet be?
3. Gondold végig, miért veszélyes a nagyon szigorú válaszidő-határ CI környezetben.
