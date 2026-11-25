# Feladat V. – Feladat IV. metódusokra bontva

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Feladat V. – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Optimalizáljuk a Feladat IV. megoldását metódusok használatával úgy, hogy a belépési `main`
  függvény csak deklarációkat és metódushívásokat tartalmazzon.

Az eredeti Feladat IV.:

- Hozzunk létre két egydimenziós tömböt, elemszámuk legyen véletlenszerű (5 és 10 között).
- Töltsük fel mindkettőt 1 és 50 közötti véletlenszámokkal.
- Listázzuk ki tömbönként: az összes elemet, az elemek összegét, a páros számok darabszámát,
  a legnagyobb elemet és az első négyzetszámot.

Előző lépés: [07-feladat-4-veletlen-tombok](../07-feladat-4-veletlen-tombok/).

## Mit mutat be?

- Visszatérési értékkel rendelkező (`int[] veletlenTomb()`, `int osszeg(int[])`) és visszatérési érték nélküli (`void statisztikaKiirasa(...)`) metódusokat.
- Tömb átadását paraméterként és visszaadását visszatérési értékként.
- A „tiszta kód” elveit: egy metódus egy dolgot csinál, a neve elárulja, mit; nincs ismétlődés.
- Konstansokat a „mágikus számok” helyett (`MIN_ELEMSZAM`, `MAX_ERTEK`, `NINCS`).

## Mi változott az előző lépéshez képest?

- A `main` mindössze négy sor: két tömb létrehozása és két kiíró hívás.
- A duplikált kód eltűnt: a statisztikák minden tömbre ugyanazokkal a metódusokkal készülnek.
- Minden programozási tétel külön metódus: `osszeg`, `parosDarab`, `maximum`, `elsoNegyzetszam` (+ `negyzetszamE`).
- A `Random` osztályszintű konstans lett, így minden metódus ugyanazt a generátort használja.
- A kimenet formája változatlan.

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
