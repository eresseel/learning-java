# Házi feladat II.B – Mátrix transzponálása

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Önálló (házi) feladat II.B – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Töltsünk fel egy 4x3-as mátrixot 50 és 100 közötti véletlenszámokkal.
- Transzponáljuk egy másik mátrixba.
- Listázzuk ki mindkét mátrixot a megfelelő sor/oszlop formátumban.

## Mit mutat be?

- Transzponálás: az új mátrix mérete fordított (3x4), és `transzponalt[j][i] = eredeti[i][j]`.
- A mátrix méretének lekérdezése (`eredeti.length`, `eredeti[0].length`) fix számok helyett.
- Közös kiíró metódus (`matrixKiirasa`), amely bármilyen méretű `int[][]`-et kiír.

Feltételezés: az 50 és a 100 is előfordulhat (zárt intervallum, `nextInt(50, 101)`).

## Fájlok

- `src/App.java` – a program és a `matrixKiirasa` metódus.

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
Eredeti mátrix (4x3):
   89   52   68
  100   70   97
   78   90   75
   63   71   75
Transzponált mátrix (3x4):
   89  100   78   63
   52   70   90   71
   68   97   75   75
```
