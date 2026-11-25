# Házi feladat B – Hatványozás ciklussal

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (JuniorJavaBackend_01.pdf), Önálló (házi) feladat B – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjünk be két számot (alap, kitevő), majd végezzünk hatványozást ciklus alkalmazásával.
- Számoljuk ki és jelenítsük meg az eredményt.

## Mit mutat be?

- Gyűjtő (akkumulátor) változó használatát ciklusban: az eredmény 1-ről indul, és minden körben megszorozzuk az alappal.
- A `for` ciklus 0 kört is futhat (`a^0 = 1`).
- Ellenőrzésként a beépített `Math.pow()` eredményét is kiírja.

Feltételezés: az alap tetszőleges valós szám, a kitevő nemnegatív egész (negatív kitevőt a program elutasít, mert ciklussal „ennyiszer szorzunk” értelemben nem értelmezhető).

## Fájlok

- `src/App.java` – a program.

## Futtatás

A feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa:

```text
$ printf '2\n10\n' | java -cp out App
Alap: Kitevő (nemnegatív egész): 2.0^10 = 1024.0
Ellenőrzés Math.pow-val: 1024.0
```
