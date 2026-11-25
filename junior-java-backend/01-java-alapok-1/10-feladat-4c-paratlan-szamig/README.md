# Feladat IV.C – Bekérés, amíg páratlan számot nem kapunk

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (JuniorJavaBackend_01.pdf), Feladat IV.C – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjünk be egy számot mindaddig, amíg a felhasználó nem páros (tehát páratlan) számot ad meg.

## Mit mutat be?

- Hátultesztelős ciklus (`do-while`): a törzs legalább egyszer lefut, a feltételt a végén vizsgáljuk.
- A `continue` utasítást (hibás, nem szám bemenetnél újrakérdezés).
- Paritás vizsgálatát: `szam % 2 == 0` páros. Negatív páratlan számnál a maradék `-1`, ezért `!= 0`-t vizsgálunk, nem `== 1`-et.
- A bemenet végének (EOF) kezelését, hogy pipe-olt bemenetnél se kerüljön végtelen ciklusba.

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
printf '4\nxy\n-2\n7\n' | java -cp out App
```

```text
Adjon meg egy egész számot: 4 páros, kérek egy másikat.
Adjon meg egy egész számot: Ez nem egész szám: xy
Adjon meg egy egész számot: -2 páros, kérek egy másikat.
Adjon meg egy egész számot: Végre egy páratlan szám: 7
```
