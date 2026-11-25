# Házi feladat C – Számvizsgálat switch szerkezettel

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

- A `switch` csak konkrét értékeket tud vizsgálni, ezért minden lehetséges értéket fel kell sorolni.
- A szándékos „átesést” (fall-through): az egymás alá írt `case 1: case 2: case 3:` címkék ugyanarra a kódrészre futnak.
- A `default` ág fogja meg a hibás értékeket (a nem szám bemenetet 0-ként kezeljük, így az is ide kerül).
- Kommentben a Java 14+ tömör változat: `case 1, 2, 3 -> ...`.

## Mi változott az előző megoldáshoz képest?

- Az `if` – `else if` lánc helyett `switch` szerkezet; a tartományok helyett felsorolt értékek.
- A kimenet ugyanarra a bemenetre azonos.

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
$ printf '8\n' | java -cp out App
Adjon meg egy egyjegyű pozitív egész számot: A szám nagyobb hatnál
$ printf '0\n' | java -cp out App
Adjon meg egy egyjegyű pozitív egész számot: Nem megfelelő értéket adott meg
```
