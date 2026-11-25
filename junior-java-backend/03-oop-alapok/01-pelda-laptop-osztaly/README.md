# Példa – Laptop osztály

> Tananyag: Junior Java Backend fejlesztő III. (OOP alapok), „Valósvilág modell” és „Osztályok felépítése” diák – lásd a [lecke áttekintőjét](../README.md).

## Feladat

A diasor példájának megvalósítása:

- a valós világ laptop objektumából csak a kereskedés számára fontos adatokat tartjuk meg (gyártó, modell, cikkszám, ár);
- `Laptop` osztály privát mezőkkel, paraméteres konstruktorral, getterekkel és setterekkel;
- két példány létrehozása a diákon szereplő adatokkal (`Samsung Series 9`, `HP Pavilion`);
- a láthatósági szintek (`private`, csomagszintű, `protected`, `public`) és a láthatatlan default konstruktor bemutatása.

## Mit mutat be?

- osztály és objektum (példány) kapcsolata, `new` operátor;
- konstruktor, `this` kulcsszó;
- getter/setter: csak olvasható mező (cikkszám), ellenőrzött írás (ár);
- adatrejtés a láthatósági szintekkel;
- `toString()` felülírása a kényelmes kiíráshoz.

## Fájlok

- `src/Laptop.java` – a laptop modellje (mezők, konstruktor, getter/setter).
- `src/PeldaOsztaly.java` – üres konstruktorú osztály a négy láthatósági szinttel.
- `src/App.java` – belépési pont, példányosítás és használat.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Részlet a kimenetből:

```text
--- Két példány ugyanabból az osztályból ---
Samsung Series 9 (LTS001): 300000 Ft
HP Pavilion (N3W03EA): 350000 Ft
```
