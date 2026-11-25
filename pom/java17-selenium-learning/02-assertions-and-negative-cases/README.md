# 02 – Assertionök és hibás bemenetek

## Cél
Ne csak a normál működést teszteld: vizsgáld meg a határeseteket és a szerződés megsértését is.

## Feladat
Készíts `TextUtils` osztályt:
- `normalize(String)` távolítsa el a szélső szóközöket és alakítsa kisbetűssé;
- `requireNonBlank(String)` dobjon `IllegalArgumentException` kivételt null vagy üres/whitespace bemenet esetén.

## Mit tanulunk?
- `assertEquals`: értékek összehasonlítása;
- `assertThrows`: elvárt kivétel ellenőrzése;
- több teszteset külön metódusokban tartása.

## Futtatás
```bash
mvn clean test
```

## Továbbfejlesztési feladat
Döntsd el és dokumentáld, hogyan viselkedjen a `normalize` metódus `null` bemenetnél. Írj hozzá tesztet, mielőtt módosítod a kódot.
