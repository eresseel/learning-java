# Házi feladat I. – Tíz egész szám vizsgálata

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Önálló (házi) feladat I. – lásd a [lecke áttekintőjét](../README.md).

## Feladat

Töltsünk fel egy 10 elemű tömböt a felhasználótól bekért egész számokkal, majd:

- **A.** írjuk ki az adatokat egymás mellé, szóközzel elválasztva;
- **B.** írjuk ki az első elem négyzetét;
- **C.** írjuk ki a páratlan számokat és indexeiket (soronként egy szám–index pár);
- **D.** írjuk ki a hárommal osztható számokat a tömb utolsó elemétől az első felé haladva;
- **E.** írjuk ki az első öt elem összegét, és külön az utolsó öt elem összegét;
- **F.** hozzunk létre egy szintén 10 elemű, de kétdimenziós tömböt, és töltsük fel ugyanazokkal az értékekkel;
- **G.** listázzuk ki egymás alá a számokat úgy, hogy mindegyik mellé kiírjuk mindkét tömbbeli indexét.

## Mit mutat be?

- Tömbfeltöltés bekéréssel, hibás bemenet újrakérdezése.
- Bejárás előre és visszafelé, feltételes kiírás (`%` maradékos osztás).
- Típusválasztás: az első elem négyzetét `long`-ban számoljuk (egy nagy `int` négyzete túlcsordulna).
- Egy- és kétdimenziós index átváltása: a `k`. elem helye `[k / oszlopok][k % oszlopok]`.

Saját döntés: a 10 elemű kétdimenziós tömb mérete **2x5** (lehetne 5x2 is – a képlet bármelyikkel működik).

## Fájlok

- `src/App.java` – a program és az `egeszBekerese` segédmetódus.

## Futtatás

A feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa (a számok szóközzel is megadhatók, a `Scanner` szavanként olvas):

```bash
printf '3 8 -5 12 7 9 10 15 -6 1\n' | java -cp out App
```

```text
A) Az elemek:
3 8 -5 12 7 9 10 15 -6 1 

B) Az első elem (3) négyzete: 9

C) Páratlan számok és indexeik:
  szám: 3, index: 0
  szám: -5, index: 2
  ...
D) Hárommal osztható számok visszafelé:
-6 15 9 12 3 

E) Az első öt elem összege: 25, az utolsó öt elem összege: 29

F) A kétdimenziós (2x5-ös) tömb:
     3     8    -5    12     7
     9    10    15    -6     1

G) Számok és indexeik mindkét tömbben:
     3  egydimenziós: [0]  kétdimenziós: [0][0]
     ...
     1  egydimenziós: [9]  kétdimenziós: [1][4]
```
