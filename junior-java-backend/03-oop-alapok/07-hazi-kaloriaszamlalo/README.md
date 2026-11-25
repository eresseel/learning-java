# Önálló feladat – Napi kalóriaszámláló

> Tananyag: Junior Java Backend fejlesztő III. (OOP alapok), Önálló (otthoni) feladat – lásd a [lecke áttekintőjét](../README.md).

## Feladat

Napi kalóriaszámláló alkalmazás egy osztállyal:

- adattagok: név, napi kalória;
- metódusok:
  - **táplálkozás** – a megadott kalóriamennyiséggel nő a napi érték;
  - **sportolás** – minden sportolással töltött óra 600 kalóriát éget el;
  - **pihenés** – minden pihenéssel töltött óra 40 kalóriát éget el;
- az adatok konzolról érkeznek, az alkalmazás kiírja a nap végi értéket.

Saját döntés: az eseményeket menüből lehet tetszőleges sorrendben és számban rögzíteni; az órák száma tört is lehet (pl. 1,5).

## Mit mutat be?

- egyszerű osztály adattagokkal és állapotot módosító metódusokkal;
- `static final` állandók a „mágikus számok” (600, 40) helyett;
- konzolos bemenet ellenőrzése.

## Fájlok

- `src/KaloriaSzamlalo.java` – a számláló osztály.
- `src/App.java` – bekérés, menü, végeredmény.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa:

```bash
printf 'Teszt Elek\n1\n2500\n2\n1,5\n3\n8\n4\n' | java -cp out App
```

```text
Teszt Elek nap végi kalóriaegyenlege: 1280 kcal
```
