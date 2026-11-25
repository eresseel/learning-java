# Példa – RepuloDolgok öröklődés (repülő dolgok lánc, 2. lépés)

> Tananyag: Junior Java Backend fejlesztő IV. (OOP II.), „Öröklődés I–VI.” és „Alprogram felülbírálás I–II.” diák – lásd a [lecke áttekintőjét](../README.md).

## Feladat

A diák példájának megvalósítása:

- `class Madar extends RepuloDolgok`, a leszármazott konstruktora `super(...)`-rel hívja az őst;
- `RepuloDolgok obj = new Madar(...)` – a leszármazott ős típusként is kezelhető;
- a hierarchia: `RepuloDolgok` → `Madar` → `Sas`, `Vereb`;
- ha a `Madar` felülírja a `repul()` metódust, akkor a `Sas` és a `Vereb` már a `Madar` változatát futtatja, hacsak maguk újra felül nem írják.

## Mit mutat be?

- öröklődés (`extends`), `super(...)` és `super.metodus()`;
- `protected` láthatóság;
- metódus-felülbírálás (`@Override`) több szinten;
- polimorfizmus, `instanceof`.

## Mi változott az előző lépéshez képest?

Az [előző lépésben](../../03-oop-alapok/03-pelda-jarmu-polimorfizmus/) a `Jarmu` → `Auto`, `Repulo` kétszintű hierarchia szerepelt. Itt háromszintű a hierarchia (`RepuloDolgok` → `Madar` → `Sas`/`Vereb`), és látszik, hogyan öröklődik tovább egy felülírt metódus. Az ős még példányosítható – a [következő lépésben](../02-pelda-repulodolgok-absztrakt/) absztrakttá tesszük.

## Fájlok

- `src/RepuloDolgok.java` – az ősosztály.
- `src/Madar.java` – felülírja a `repul()`-t.
- `src/Sas.java` – újra felülírja, `super.repul()`-lal.
- `src/Vereb.java` – nem írja felül, a `Madar` változatát örökli.
- `src/App.java` – belépési pont.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```
