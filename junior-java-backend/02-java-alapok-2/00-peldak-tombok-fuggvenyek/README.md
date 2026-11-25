# Bemutató példák: tömbök, beépített függvények, véletlenszámok, metódusok

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), elméleti diák (Tömb I–III., Karakter-, Sztring-, Matematikai és Dátumfüggvények, Véletlenszám generátor, Metódusok I–IV., Alprogram túltöltés) – lásd a [lecke áttekintőjét](../README.md).

## Feladat

Nem számozott feladat, hanem a diákon szereplő kódrészletek futtatható bemutatója:

- tömb deklarálása, inicializálása, definiálása (minden dián szereplő írásmód), `length`;
- kétdimenziós tömb és eltérő hosszú sorokból álló („csipkés”) tömb;
- `Character` metódusok (`isLetter`, `isDigit`, `toUpperCase`, `compare` …);
- `String` metódusok (`length`, `substring`, `replace`, `split`, `contains`, `indexOf`, `trim`, `equals` …);
- `Math` metódusok és konstansok; `LocalDate`, `LocalTime`, `LocalDateTime`;
- `Random`: `nextInt(max)`, `nextInt(min, max)`, `nextDouble()`;
- visszatérési értékkel rendelkező és nélküli metódus, túltöltött `udvozlo()` / `udvozlo(String)`.

## Mit mutat be?

- A dián `toUpper()` / `toLower()` szerepel – a valódi metódusnevek `toUpperCase()` és `toLowerCase()`.
- A `nextInt(min, max)` felső határa **nem** része a tartománynak (1 és 50 között: `nextInt(1, 51)`).
- Szövegeket `equals`-szal hasonlítunk össze, nem `==` operátorral.

## Fájlok

- `src/App.java` – a bemutató (nem interaktív; a dátumos és véletlenszámos rész minden futáskor mást ír ki).

## Futtatás

A feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```
