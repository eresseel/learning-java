# Feladat III.A, 3. lépés – Egyjegyű, kétjegyű, többjegyű

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (JuniorJavaBackend_01.pdf), Feladat III.A – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjünk be egy számot.
- Írjunk ki egy üzenetet, ha a szám egyjegyű.
- Bővítsük a programot úgy, hogy azt is jelezze, ha a szám nem egyjegyű.
- További bővítés után a program az „egyjegyű”, „kétjegyű”, „többjegyű” kiírással reagáljon.

A feladat három lépésben épül fel, mindegyik lépés külön, önállóan futtatható mappa:
[1. lépés](../04-feladat-3a-lepes-1-egyjegyu/) → [2. lépés](../05-feladat-3a-lepes-2-nem-egyjegyu/) → [3. lépés](../06-feladat-3a-lepes-3-jegyek-szama/).

**Ez a 3. (végső) lépés:** a program „egyjegyű”, „kétjegyű” vagy „többjegyű” kiírással reagál.

## Mit mutat be?

- Többágú elágazás (`if` – `else if` – `else`): a feltételek sorrendben értékelődnek ki, és az első igaz ág fut le.
- Ezért a későbbi ágak feltételei egyszerűbbek lehetnek (a `kétjegyű` ágban már nem kell vizsgálni, hogy a szám nagyobb-e 9-nél).

## Mi változott az előző lépéshez képest?

- A kétirányú elágazásból háromágú lett, a szövegek a feladat szerinti „egyjegyű / kétjegyű / többjegyű”.
- Az abszolútértéket egy külön változóba (`abszolut`) mentjük, hogy ne kelljen többször kiszámolni.

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
$ printf '42\n' | java -cp out App
Adjon meg egy egész számot: kétjegyű
$ printf -- '-123\n' | java -cp out App
Adjon meg egy egész számot: többjegyű
```
