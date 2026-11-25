# Feladat II – Családi kassza

> Tananyag: Junior Java Backend fejlesztő III. (OOP alapok), Feladat II – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Egyszerűsített családi kasszát kezelő program, a kezdő egyenleg 300 000 Ft.
- Menüpontok:
  1. Bevétel rögzítése
  2. Kiadás könyvelése
  3. Egyenleg lekérdezése
  4. Kilépés
- A kiválasztott feladatot a kassza osztály valósítsa meg (a diasoron „Kassza”, itt a repó elnevezési szokása szerint `Kassa`).

Saját döntés: a kassza nem mehet mínuszba, a fedezetet meghaladó kiadást elutasítja; nulla vagy negatív összeget sem fogad el.

## Mit mutat be?

- egységbezárás: az egyenleg és a rajta végzett műveletek egy osztályban;
- adatrejtés: az egyenleghez csak getter van, írni csak a `bevetel` / `kiadas` metódussal lehet;
- `static final` állandó a kezdő egyenleghez;
- menüvezérelt program `do-while` ciklussal és `switch` kifejezéssel.

## Fájlok

- `src/Kassa.java` – a kassza logikája.
- `src/App.java` – menü és bekérés.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa (pipe-olt bemenettel):

```bash
printf '1\n50000\n2\n120000\n3\n4\n' | java -cp out App
```

```text
Választás: 3
Aktuális egyenleg: 230 000 Ft
```

A tizedes- és ezreselválasztó a gép területi beállításától (locale) függ, angol beállításnál pl. `12.57`, illetve `230,000`.
