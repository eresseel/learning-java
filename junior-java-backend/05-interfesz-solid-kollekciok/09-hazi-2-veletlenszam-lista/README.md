# Házi feladat II – Véletlenszámok listában

> Tananyag: Junior Java Backend fejlesztő V., Házi feladat II – lásd a [lecke áttekintőjét](../README.md).

## Feladat

Töltsünk fel egy listát 30 darab véletlenszámmal 1 és 100 között, majd:

1. írjuk ki a lista elemeit tízesével egymás mellé, tabulátorral elválasztva;
2. hány páros szám van a listában?
3. van-e a listában a lista elemszámával egyező értékű elem?
4. képezzünk részlistát a középső 10 elemből, és írjuk ki kötőjellel elválasztva (az utolsó után ne legyen elválasztójel);
5. töröljünk minden 20 alatti páratlan számot a listából, majd írjuk ki soronként a megmaradt elemeket; hány páros szám maradt?

## Mit mutat be?

- `ArrayList<Integer>` feltöltése, autoboxing;
- `contains`, `subList`, törlés bejárás közben `Iterator.remove()`-val (és a `removeIf` alternatíva);
- elválasztójeles kiírás úgy, hogy az utolsó elem után ne legyen jel.

## Fájlok

- `src/App.java` – a teljes megoldás.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Megismételhető futáshoz a véletlenszám-generátor kezdőértéke (seed) argumentumként megadható:

```bash
java -cp out App 42
```
