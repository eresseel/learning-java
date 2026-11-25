# Feladat I – ABC raktárkezelés

> Tananyag: Junior Java Backend fejlesztő IV. (OOP II.), Feladat I – lásd a [lecke áttekintőjét](../README.md).

## Feladat

Vegyeskereskedés (ABC) alapszintű raktárkezelése:

- minden termékről tudjuk: megnevezés, cikkszám, ár, darabszám;
- két termékkategória:
  - **tejtermék** – plusz adat: zsírtartalom;
  - **pékáru** – plusz adat: teljes kiőrlésű-e;
- ami egyik kategóriába sem tartozik, általános termékként tároljuk;
- hozzuk létre az adatszerkezetet és egy-egy objektumot minden típushoz.

Kiegészítés (a lecke kivételkezelés témája alapján): érvénytelen adatnál (üres név/cikkszám, nem pozitív ár, negatív darabszám, 0–100%-on kívüli zsírtartalom, ismétlődő cikkszám, túl nagy kiadás) az osztályok kivételt dobnak, a program elkapja és kiírja őket.

## Mit mutat be?

- öröklődés: `Tejtermek` és `Pekaru` → `Termek`, `super(...)` konstruktorhívás;
- felülbírálás: `kategoria()`, `extraInfo()`, a polimorf `toString()`;
- polimorfizmus: egy `Termek[]` tömbben minden típus;
- kivételdobás (`throw new IllegalArgumentException(...)`) és elkapás, multi-catch.

## Fájlok

- `src/Termek.java` – az ős, egyben az általános termék.
- `src/Tejtermek.java`, `src/Pekaru.java` – a két kategória.
- `src/Raktar.java` – a termékek tárolása (tömb), keresés, összérték.
- `src/App.java` – objektumok létrehozása, hibás adatok kipróbálása.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Részlet a kimenetből:

```text
=== Érvénytelen adatok kezelése ===
Elutasítva: A zsírtartalom 0 és 100% között lehet (kapott: 120.0)!
Elutasítva: Az árnak pozitívnak kell lennie (kapott: -60)!
```
