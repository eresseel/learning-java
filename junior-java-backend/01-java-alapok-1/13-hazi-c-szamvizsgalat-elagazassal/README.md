# Házi feladat C – Számvizsgálat elágazásokkal

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (JuniorJavaBackend_01.pdf), Önálló (házi) feladat C – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjünk be egy egyjegyű pozitív egész számot, és vizsgáljuk meg.
- A vizsgálatnak megfelelő üzenetet írjuk ki:
  - „A szám nem nagyobb háromnál” (1–3),
  - „A szám 4 és 6 közötti” (4–6),
  - „A szám nagyobb hatnál” (7–9),
  - „Nem megfelelő értéket adott meg” (minden más, pl. 0, negatív, kétjegyű vagy nem szám).
- Kétféleképpen oldjuk meg: elágazásokkal és switch case szerkezettel is.

A két megoldás két külön mappában van, hogy egymás mellé téve összehasonlíthatók legyenek:
[elágazással](../13-hazi-c-szamvizsgalat-elagazassal/) és [switch-csel](../14-hazi-c-szamvizsgalat-switch/).
Ugyanarra a bemenetre mindkettő ugyanazt írja ki.

## Mit mutat be?

- Többágú `if` – `else if` – `else` elágazás tartományok vizsgálatára.
- Azt a mintát, hogy először a hibás értékeket szűrjük ki, így a további ágak feltételei egyszerűek.
- Elágazással tartományt (`szam <= 3`) könnyű vizsgálni – ez az előnye a `switch`-csel szemben.

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
$ printf '5\n' | java -cp out App
Adjon meg egy egyjegyű pozitív egész számot: A szám 4 és 6 közötti
$ printf '12\n' | java -cp out App
Adjon meg egy egyjegyű pozitív egész számot: Nem megfelelő értéket adott meg
```
