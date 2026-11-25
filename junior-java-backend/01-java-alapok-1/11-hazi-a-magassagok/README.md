# Házi feladat A – Ki a magasabb?

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (JuniorJavaBackend_01.pdf), Önálló (házi) feladat A – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjük be két ember magasságát centiméterben.
- Állapítsuk meg és írjuk ki, melyikük a magasabb és mennyivel.
- Legyen megfelelő kiírás akkor is, ha egyforma magasak.

## Mit mutat be?

- Háromágú elágazás a három lehetséges esetre (nagyobb / kisebb / egyenlő).
- Bemenet-ellenőrzés: nem szám és nem pozitív érték kiszűrése.

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
$ printf '180\n172\n' | java -cp out App
Az első ember magassága (cm): A második ember magassága (cm): Az első ember a magasabb, 8 cm-rel.
$ printf '170\n170\n' | java -cp out App
Az első ember magassága (cm): A második ember magassága (cm): Egyforma magasak (170 cm).
```
