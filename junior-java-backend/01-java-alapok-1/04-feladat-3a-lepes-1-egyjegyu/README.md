# Feladat III.A, 1. lépés – Egyjegyű-e a szám?

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (JuniorJavaBackend_01.pdf), Feladat III.A – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjünk be egy számot.
- Írjunk ki egy üzenetet, ha a szám egyjegyű.
- Bővítsük a programot úgy, hogy azt is jelezze, ha a szám nem egyjegyű.
- További bővítés után a program az „egyjegyű”, „kétjegyű”, „többjegyű” kiírással reagáljon.

A feladat három lépésben épül fel, mindegyik lépés külön, önállóan futtatható mappa:
[1. lépés](../04-feladat-3a-lepes-1-egyjegyu/) → [2. lépés](../05-feladat-3a-lepes-2-nem-egyjegyu/) → [3. lépés](../06-feladat-3a-lepes-3-jegyek-szama/).

**Ez az 1. lépés:** csak akkor írunk ki üzenetet, ha a szám egyjegyű.

## Mit mutat be?

- Egyszerű elágazás `else` ág nélkül: ha a feltétel hamis, nem történik semmi.
- Összetett feltétel a logikai ÉS (`&&`) operátorral: `szam >= -9 && szam <= 9`.
- Döntés: a negatív számokat is kezeljük (pl. `-7` is egyjegyű).

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
$ printf '7\n' | java -cp out App
Adjon meg egy egész számot: A szám egyjegyű.
$ printf '17\n' | java -cp out App
Adjon meg egy egész számot:
```
