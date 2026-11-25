# 12 – Külső szolgáltatás szimulálása WireMock-kal

**Nehézség:** ★★★★☆

## Cél
Olyan kód tesztelése, amely egy külső HTTP szolgáltatást hív. A külső szolgáltatást helyi mock szerverrel váltjuk ki, így a teszt gyors, megbízható és offline is lefut.

## Feladat
- Nézd meg a `PostService` osztályt (`src/main`): ez a tesztelendő kód, a Java beépített `HttpClient`-jét használja.
- Készíts WireMock stubokat a sikeres válaszra.
- Szimulálj hibákat: `404`, `500`, hibás JSON, hiányzó mező, lassú válasz, megszakadó kapcsolat.
- Ellenőrizd, hogy a szolgáltatás a megfelelő hívást küldte-e (`verify`).

## Mit tanulunk?
- mock és stub fogalma, és mikor érdemes használni;
- a WireMock JUnit kiegészítője (`@RegisterExtension`), dinamikus port;
- hibaszimuláció (státuszkód, késleltetés, kapcsolat megszakítása), amit éles szolgáltatással nehéz kiváltani;
- a hibakezelés tesztelése: a kód érthető kivételt dob, nem omlik össze;
- különbség az API teszt (11. lépés: a szolgáltatás tesztelése) és a mockolt egységteszt (itt: a mi kódunk tesztelése).

## Futtatás
```bash
cd 12-api-mock-wiremock
mvn clean test
```
Internetkapcsolat csak az első Maven letöltéshez kell, a tesztek maguk offline futnak. Ez az egyik legmegbízhatóbb szint CI környezetben.

## Továbbfejlesztési feladat
1. Adj a `PostService`-hez `fetchAllTitles()` metódust (`GET /posts`), és írj hozzá tesztet több elemű JSON-nal.
2. Használj WireMock *scenario*-t: az első hívásra `503`, a másodikra `200` jöjjön, és valósíts meg újrapróbálkozást (retry) a szolgáltatásban.
3. Ellenőrizd `verify`-jal, hogy a kérés tartalmazta az `Accept: application/json` fejlécet.
