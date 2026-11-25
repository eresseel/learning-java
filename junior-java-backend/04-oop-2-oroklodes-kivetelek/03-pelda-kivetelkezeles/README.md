# Példa – kivételkezelés

> Tananyag: Junior Java Backend fejlesztő IV. (OOP II.), „Kivételkezelés I–IX.” és „Tiszta kód V.” diák – lásd a [lecke áttekintőjét](../README.md).

## Feladat

A diák témáinak bemutatása egy programban:

- `try` – több `catch` (specifikus, majd általános `Exception`) – `finally` szerkezet;
- a kivétel terjedése a hívási láncon visszafelé;
- ellenőrzött (fordító által kikényszerített, tipikusan I/O) és futási idejű kivételek;
- egyedi kivétel dobása: `throw new ...`, saját kivételosztállyal és beszédes hibaüzenettel.

## Mit mutat be?

- a `catch` ágak sorrendje: a specifikusabb előbb, az `Exception` utoljára;
- a `finally` mindig lefut;
- `throws` a metódus fejében ellenőrzött kivételnél;
- az objektum maga ellenőrzi az adatait, de a hiba kezelését a hívóra bízza.

## Fájlok

- `src/ErvenytelenEletkorException.java` – saját, ellenőrzött kivétel.
- `src/Szemely.java` – adatellenőrzés kivételdobással.
- `src/App.java` – a négy bemutató rész.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Részlet a kimenetből:

```text
=== 1. try – catch – finally ===
5 -> egyéb hiba: ArrayIndexOutOfBoundsException – Index 5 out of bounds for length 3
   (finally: 5 feldolgozva)
0 -> aritmetikai hiba: / by zero
   (finally: 0 feldolgozva)
```
