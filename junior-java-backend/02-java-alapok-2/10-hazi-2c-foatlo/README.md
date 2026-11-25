# Házi feladat II.C – Főátló

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Önálló (házi) feladat II.C – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Töltsünk fel egy 3x3-as mátrixot egyjegyű véletlenszámok négyzetre emelt értékeivel.
- Listázzuk ki a főátlóban található számokat és összegüket.
- Listázzuk ki fordított sorrendben (az utolsó elemtől az elsőig) az eredetileg generált számokat.

## Mit mutat be?

- Négyzetes mátrix főátlója: azok az elemek, ahol a sor- és oszlopindex egyenlő (`[i][i]`) – ehhez egyetlen ciklus elég.
- Két párhuzamos mátrix: az eredeti számokat is eltároljuk, mert a végén ezeket kell kiírni.
  (Alternatíva: `(int) Math.sqrt(negyzet)` – a tárolás egyszerűbb és egyértelműbb.)
- Fordított bejárás kétdimenziós tömbön.

Feltételezés: egyjegyű szám a 0 is, ezért a generált számok 0 és 9 közöttiek (`nextInt(10)`).

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

Példa kimenet (véletlenszámokkal):

```text
A négyzetre emelt értékek mátrixa:
  64  64   1
  81  25  81
   4   0   4

A főátló elemei: 64 25 4 
A főátló összege: 93

Az eredeti számok fordított sorrendben: 2 0 2 9 5 9 1 8 8 
```
