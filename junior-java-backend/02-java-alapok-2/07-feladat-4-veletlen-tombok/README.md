# Feladat IV. – Két véletlen tömb statisztikái

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Feladat IV. – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Hozzunk létre két egydimenziós tömböt, elemszámuk legyen véletlenszerű (5 és 10 között).
- Töltsük fel mindkettőt 1 és 50 közötti véletlenszámokkal.
- Listázzuk ki tömbönként: az összes elemet, az elemek összegét, a páros számok darabszámát,
  a legnagyobb elemet és az első négyzetszámot.

A Feladat V. ugyanezt metódusokra bontja: [11-feladat-5-metodusokra-bontas](../11-feladat-5-metodusokra-bontas/).

## Mit mutat be?

- `Random` használata: `nextInt(5, 11)` és `nextInt(1, 51)` – a felső határ nem része a tartománynak, ezért +1.
- Az alapvető programozási tételeket: összegzés, megszámlálás, maximumkiválasztás, keresés (`break`-kel).
- Négyzetszám-vizsgálat: `(int) Math.sqrt(szam)` négyzete visszaadja-e a számot.
- Hogy miért rossz az ismétlődő kód: a második tömbre ugyanazt a kódot még egyszer leírtuk – ezt szünteti meg a Feladat V.

## Fájlok

- `src/App.java` – a program (minden a `main`-ben).

## Futtatás

A feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa kimenet (a számok véletlenszerűek, minden futáskor mások):

```text
=== Első tömb (10 elem) ===
Elemek: 23 27 43 37 20 7 25 49 47 17
Összeg: 295
Páros számok darabszáma: 1
Legnagyobb elem: 49
Első négyzetszám: 25
=== Második tömb (6 elem) ===
Elemek: 40 40 23 22 48 44
Összeg: 217
Páros számok darabszáma: 5
Legnagyobb elem: 48
Első négyzetszám: nincs a tömbben
```
