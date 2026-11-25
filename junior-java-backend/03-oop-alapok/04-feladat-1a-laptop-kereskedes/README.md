# Feladat I.A – Laptop kereskedés

> Tananyag: Junior Java Backend fejlesztő III. (OOP alapok), Feladat I.A – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Implementáljuk a diasoron példaprogramként megjelenő laptopkereskedés adatszerkezetét.
- A laptopról tárolt adatok (a diasor absztrakciója szerint): gyártó neve, modell neve, cikkszám, ár.
- Példányok: `new Laptop("Samsung", "Series 9", "LTS001", 300000)`, `new Laptop("HP", "Pavilion", "N3W03EA", 350000)`.

A feladat csak az adatszerkezetet kéri; a kereskedést összefogó `LaptopKereskedes` osztály és a lekérdezések saját kiegészítések, hogy a modell ki is próbálható legyen.

## Mit mutat be?

- valósvilág-modell → OO típus → példányosítás;
- egységbezárás és adatrejtés: a készlettömb privát, csak metódusokon át érhető el;
- `static final` osztályszintű állandó;
- a 2. leckéből ismert programozási tételek (keresés, összegzés, maximumkiválasztás) objektumokon.

## Fájlok

- `src/Laptop.java` – egy laptop adatai, getterek, ármódosítás, akció.
- `src/LaptopKereskedes.java` – a készlet (max. 10 elemű tömb) és műveletei.
- `src/App.java` – belépési pont, kipróbálás.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```
