# Feladat IV.B – Száztól hármasával az első 16-tal osztható számig

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (JuniorJavaBackend_01.pdf), Feladat IV.B – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Írjuk ki száztól hármasával a számokat, az első 16-tal maradék nélkül osztható számig.

## Mit mutat be?

- Feltételes ciklusok: ha nem tudjuk előre, hány kör kell, `while` vagy `do-while` a megfelelő.
- A maradékos osztás (`%`) használatát oszthatóság vizsgálatára.
- Két megoldást: hátultesztelős (`do-while`) és elöltesztelős (`while`) ciklussal.
  Értelmezés: a 16-tal osztható számot (112) is kiírjuk, ott áll meg a sorozat.

## Fájlok

- `src/App.java` – mindkét megoldás, egymás után.

## Futtatás

A feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Kimenet:

```text
100 103 106 109 112 
100 103 106 109 112  <- az első 16-tal osztható szám
```
