# Feladat III.A, 2. lépés – Egyjegyű vagy nem egyjegyű

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (JuniorJavaBackend_01.pdf), Feladat III.A – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjünk be egy számot.
- Írjunk ki egy üzenetet, ha a szám egyjegyű.
- Bővítsük a programot úgy, hogy azt is jelezze, ha a szám nem egyjegyű.
- További bővítés után a program az „egyjegyű”, „kétjegyű”, „többjegyű” kiírással reagáljon.

A feladat három lépésben épül fel, mindegyik lépés külön, önállóan futtatható mappa:
[1. lépés](../04-feladat-3a-lepes-1-egyjegyu/) → [2. lépés](../05-feladat-3a-lepes-2-nem-egyjegyu/) → [3. lépés](../06-feladat-3a-lepes-3-jegyek-szama/).

**Ez a 2. lépés:** azt is kiírjuk, ha a szám nem egyjegyű.

## Mit mutat be?

- Kétirányú elágazás (`if` – `else`): pontosan az egyik ág fut le.
- A `Math.abs()` abszolútérték-függvényt, amellyel egyetlen feltétel elég a negatív számokhoz is.

## Mi változott az előző lépéshez képest?

- Új `else` ág: „A szám nem egyjegyű.” üzenet.
- A `szam >= -9 && szam <= 9` feltétel helyett az egyenértékű, rövidebb `Math.abs(szam) <= 9` szerepel.

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
$ printf '17\n' | java -cp out App
Adjon meg egy egész számot: A szám nem egyjegyű.
```
