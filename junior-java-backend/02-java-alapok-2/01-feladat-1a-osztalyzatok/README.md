# Feladat I.A – Osztályzatok tömbben

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Feladat I.A – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjük be egy tanuló hat tantárgyának év végi eredményét, az adatokat tömbben tároljuk.
- Listázzuk ki egymás mellé az osztályzatokat (szóközzel elválasztva).
- Írjuk ki a tanuló átlagát.
- B. rész: oldjuk meg a feladatot – ahol lehet – bővített for ciklussal (for-each) is.

Ez az **A** rész: hagyományos, indexes `for` ciklusokkal. A B rész: [02-feladat-1b-osztalyzatok-foreach](../02-feladat-1b-osztalyzatok-foreach/).

## Mit mutat be?

- Egydimenziós tömb létrehozása (`new int[6]`), feltöltése és bejárása index alapján; a `length` adattag használata.
- Összegzés, átlagszámítás – az egész osztás elkerülése kasztolással (`(double) osszeg / length`).
- Bemenet-ellenőrzés külön metódusban: csak 1–5 közötti egész fogadható el, hibás értéknél újrakérdez.

## Fájlok

- `src/App.java` – a program és az `osztalyzatBekerese` segédmetódus.

## Futtatás

A feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa:

```bash
printf '5\n4\n3\n5\n4\n2\n' | java -cp out App
```

```text
1. tantárgy osztályzata (1-5): ... 6. tantárgy osztályzata (1-5): Osztályzatok: 5 4 3 5 4 2 
Átlag: 3.83
```
