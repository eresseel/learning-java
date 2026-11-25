# Feladat II.B – Téglalap kerülete és területe

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (JuniorJavaBackend_01.pdf), Feladat II.B – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjük be a konzolról egy téglalap két oldalának méretét.
- Írjuk ki a téglalap kerületét (`2 * (a + b)`) és területét (`a * b`).
- A kiírandó eredményt formázzuk.

## Mit mutat be?

- Valós szám bekérése (`nextDouble()`), bemenet ellenőrzése (`hasNextDouble()`) – hibás bemenetnél nem száll el a program.
- Formázott kiírás: `printf` a `%.2f` (két tizedesjegy), `%8.2f` (szélesség + igazítás) és `%n` (sortörés) formázókkal.
- Korai kilépés a `main`-ből `return`-nel.

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

Példa:

```bash
printf '3.5\n4\n' | java -cp out App
```

```text
A téglalap oldalai: 3.50 cm és 4.00 cm
Kerület:    15.00 cm
Terület:    14.00 cm²
```

Megjegyzés: a `Scanner` a tizedesjelet a rendszer területi beállítása (locale) szerint várja –
angol beállításnál `3.5`, magyarnál `3,5` a helyes alak. Ugyanez igaz a `printf` kimenetére.
