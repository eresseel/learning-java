# 7. lépés – Egyenleg és tranzakciótörténet fájlba / fájlból

> Tananyag: Bankszámla TDD feladatlap – lásd a [lánc áttekintőjét](../README.md).

## Feladat

- Egyenleg betöltése fájlból: a fájl neve `"egyenleg" + számlaszám + ".txt"`.
- Egyenleg kiírása ugyanebbe a fájlba.
- Tranzakciótörténet kiírása: a fájl neve `"tranzakcio" + dátum + ".txt"` (pl. `tranzakcio2026-11-25.txt`).

## Mit mutat be?

- Fájlkezelés `java.nio.file.Files`-szal (`readString`, `writeString`, `write`).
- Tesztelés valódi fájlokkal, de **ideiglenes könyvtárban**: JUnit `@TempDir`.
- Tesztelhetőségi döntés: a mappa és a dátum **paraméter**, nem beégetett érték – így a teszt nem függ a futtatás helyétől és napjától.
- Ellenőrzött kivétel (`IOException`) továbbadása `throws`-szal.

## Mi változott az előző lépéshez képest?

Előző lépés: [06-napi-kamat](../06-napi-kamat/)

- Új metódusok: `egyenlegFajl`, `tranzakcioFajl`, `egyenlegBetoltes`, `egyenlegKiiras`, `tranzakciokKiirasa` (2 változat).
- Új tesztosztály: `BankszamlaFajlTest`.

## A TDD ciklus ebben a lépésben (piros → zöld → refaktor)

1. **Piros:** `fajlnevek` – a fájlnév-képző metódusok hiányoznak.
2. **Zöld:** `mappa.resolve("egyenleg" + szamlaszam + ".txt")`.
3. **Piros:** `egyenlegBetoltes` – a teszt előbb létrehozza a fájlt a `@TempDir` mappában, a metódus még nem létezik.
4. **Zöld:** `Files.readString` + `Long.parseLong`.
5. **Piros:** a fájl végi sortörés miatt `NumberFormatException` – **refaktor:** `strip()`.
6. **Piros → zöld:** `hibasTartalom`, `hianyzoFajl`, `egyenlegKiirasEsVisszatoltes`, `tranzakciokKiirasa`.
7. **Refaktor:** a fájlnév-előtagok konstansba kerültek, a dátum nélküli kényelmi változat a dátumos változatot hívja.

## Fájlok

- `pom.xml` – Maven projekt (Java 21, JUnit 6).
- `src/main/java/hu/learning/feladatlapok/bankszamla/Bankszamla.java` – a bankszámla osztály.
- `src/main/java/hu/learning/feladatlapok/bankszamla/NincsFedezetException.java` – saját kivétel fedezethiányra.
- `src/test/java/hu/learning/feladatlapok/bankszamla/BankszamlaTest.java` – a TDD során először megírt tesztek.
- `src/test/java/hu/learning/feladatlapok/bankszamla/BankszamlaFajlTest.java` – fájlkezelés tesztjei `@TempDir`-rel.

## Futtatás

Ebben a lépésben nincs `main` metódus: a „program” a tesztkészlet.

```bash
mvn -B -q verify
```

Részletes, tesztenkénti kimenet (a `@DisplayName` szövegekkel):

```bash
mvn -B test
```
