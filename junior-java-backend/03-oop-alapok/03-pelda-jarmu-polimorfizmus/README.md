# Példa – Jármű polimorfizmus (repülő dolgok lánc, 1. lépés)

> Tananyag: Junior Java Backend fejlesztő III. (OOP alapok), „Öröklés” és „Polimorfizmus” diák – lásd a [lecke áttekintőjét](../README.md).

## Feladat

A diák szöveges példájának megvalósítása:

- van egy `Jarmu` osztály, ebből származik az `Auto` és a `Repulo`;
- `Jarmu val = new Auto(…); Jarmu val = new Repulo(…);` – ős típusú változóban leszármazott objektum;
- a `Jarmu` előírja az `inditas()` funkciót, de az autó és a repülő másképp indul.

## Mit mutat be?

- öröklés (`extends`), `super(...)` konstruktorhívás, `protected` mező;
- metódus felülírás (`@Override`);
- polimorfizmus: ős típusú tömb, és mindig a tényleges objektum metódusa fut.

Az öröklést részletesen a 4. lecke tárgyalja; itt csak a fogalmak szemléltetése a cél.

## A „repülő dolgok” példalánc

Ez a mappa a leckéken átívelő lánc első lépése:

1. **ez a mappa** – `Jarmu` → `Auto`, `Repulo` (öröklés, polimorfizmus);
2. [04 / 01 – RepuloDolgok öröklődés](../../04-oop-2-oroklodes-kivetelek/01-pelda-repulodolgok-oroklodes/);
3. [04 / 02 – RepuloDolgok absztrakt osztály](../../04-oop-2-oroklodes-kivetelek/02-pelda-repulodolgok-absztrakt/);
4. [05 / 01 – RepulInterface](../../05-interfesz-solid-kollekciok/01-pelda-repul-interfesz/).

## Fájlok

- `src/Jarmu.java` – az ősosztály, általános `inditas()`.
- `src/Auto.java`, `src/Repulo.java` – leszármazottak, felülírt `inditas()`.
- `src/App.java` – belépési pont, polimorf tömb és ciklus.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```
