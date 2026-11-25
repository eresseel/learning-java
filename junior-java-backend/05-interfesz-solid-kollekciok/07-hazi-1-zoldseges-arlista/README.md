# Házi feladat – Zöldséges árlista (Map)

> Tananyag: Junior Java Backend fejlesztő V., Házi feladat – lásd a [lecke áttekintőjét](../README.md).

## Feladat

Egy zöldséges árlistája (Ft/kg): eper 2900; alma 590; burgonya 350; cukkini 890; gomba 750; kivi 850.

- Az adatokat `Map` típusú adatszerkezetben tároljuk.
- Menü:
  1. új termék/ár felvitel – adatok bekérése konzolról, hozzáfűzés;
  2. termék törlése – név bekérése, törlés;
  3. árváltozás – termék és új ár bekérése, módosítás;
  4. terméklista – termékek és árak kiírása;
  5. vásárlás – termék neve és mennyisége, árkalkuláció;
  6. kilépés.

Saját döntések: a terméknevek kis- és nagybetűtől függetlenek; a mennyiség kilogrammban, tizedes értékkel is megadható; a fizetendő összeg egész forintra kerekül; meglévő terméket az 1. menüpont nem ír felül.

## Mit mutat be?

- `TreeMap<String, Integer>`: `put`, `putIfAbsent`, `replace`, `remove`, `get`, `entrySet`;
- autoboxing/unboxing (`Integer` ár szorzása `double` mennyiséggel).

## Fájlok

- `src/Arlista.java` – a Map és a rajta végzett műveletek.
- `src/App.java` – menü, bekérés, vásárlás.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa:

```bash
printf '5\neper\n0,5\n6\n' | java -cp out App
```

```text
eper: 0,50 kg × 2900 Ft/kg = 1450 Ft
```

A tizedesjel a gép területi beállításától függ (angol beállításnál `0.50`).
