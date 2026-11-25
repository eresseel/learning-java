# Feladat I.B – Osztályzatok bővített for ciklussal

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Feladat I.B – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjük be egy tanuló hat tantárgyának év végi eredményét, az adatokat tömbben tároljuk.
- Listázzuk ki egymás mellé az osztályzatokat (szóközzel elválasztva).
- Írjuk ki a tanuló átlagát.
- B. rész: oldjuk meg a feladatot – ahol lehet – bővített for ciklussal (for-each) is.

Ez a **B** rész. Az A rész: [01-feladat-1a-osztalyzatok](../01-feladat-1a-osztalyzatok/).

## Mit mutat be?

- A bővített for ciklust (`for (int jegy : osztalyzatok)`): nincs index, nem lehet elrontani a határokat.
- Mikor **nem** használható: a feltöltésnél, mert a ciklusváltozó csak az elem másolata, rajta keresztül nem írhatunk a tömbbe.

## Mi változott az előző lépéshez képest?

- A listázás és az összegzés for-each ciklussal történik.
- A feltöltés maradt indexes `for` ciklus (ott szükség van az indexre).
- A kimenet ugyanarra a bemenetre azonos az A részével.

## Fájlok

- `src/App.java` – a program és az `osztalyzatBekerese` segédmetódus.

## Futtatás

A feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa: `printf '5 4 3 5 4 2\n' | java -cp out App` → `Osztályzatok: 5 4 3 5 4 2`, `Átlag: 3.83`.
