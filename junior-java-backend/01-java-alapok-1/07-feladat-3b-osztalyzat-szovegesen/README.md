# Feladat III.B – Osztályzat szöveges formában

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (JuniorJavaBackend_01.pdf), Feladat III.B – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjük be egy tanuló osztályzatát (1–5).
- Írjuk ki az érdemjegyet szöveges formában: elégtelen, elégséges, közepes, jó, jeles.

## Mit mutat be?

- Esetszétválasztásos szerkezet (`switch` – `case` – `break` – `default`), pontosan a dián szereplő formában.
- Mi a szerepe a `break`-nek (nélküle a következő `case` is lefutna – „átesés”).
- A `default` ág a hibás értékekre („hibás érték”).
- Kommentben: a Java 14 óta elérhető nyilas (`case 1 ->`) szintaxis.

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

```text
$ printf '4\n' | java -cp out App
Adja meg a tanuló osztályzatát (1-5): jó
$ printf '9\n' | java -cp out App
Adja meg a tanuló osztályzatát (1-5): hibás érték
```
