# 05 – Locatorok és explicit várakozás

## Cél
Tanuld meg, hogyan találsz meg elemeket különböző locatorokkal, és hogyan vársz stabilan egy elem megjelenésére.

## Feladat
A helyi oldalon:
- keress elemet `id` alapján;
- keress CSS selectorral;
- keress XPath segítségével;
- kattints a gombra;
- várd meg, hogy az eredmény megjelenjen.

## Mit tanulunk?
- `By.id`, `By.cssSelector`, `By.xpath`;
- `WebDriverWait` és `ExpectedConditions`;
- miért jobb az explicit wait a rögzített `Thread.sleep` hívásnál.

## Futtatás
```bash
mvn clean test
```

## Továbbfejlesztési feladat
Adj hozzá olyan tesztet, amely ellenőrzi, hogy a gomb megnyomása után az eredmény pontosan `Button clicked` lesz.
