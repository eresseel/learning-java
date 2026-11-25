# Feladat III. – Felhasználói azonosító generálása

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Feladat III. – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjük be a felhasználó vezetéknevét, majd a keresztnevét.
- Képezzünk számára csupa kisbetűs felhasználói azonosítót:
  a vezetéknév első 3 betűje + a keresztnév első 3 betűje + a teljes névben található `e` betűk száma
  (kis- és nagybetű egyaránt számít).
- Írjuk ki a felhasználó teljes nevét és a generált azonosítót.
- Példa a tananyagból: *Straub Ingrid* → `string0`.

## Mit mutat be?

- Sztringfüggvények: `trim()`, `substring()`, `length()`, `toLowerCase()`, `charAt()`, `isEmpty()`.
- Karakterfüggvény: `Character.toLowerCase()`.
- Megszámlálás tétele egy szöveg karakterein.
- Védekezés a túl rövid nevek ellen: `Math.min(3, nev.length())`, így pl. az „Ó” vezetéknévnél sem keletkezik kivétel.

Feltételezés: az ékezetes `é` más betű, mint az `e`, ezért nem számít bele (a tananyag csak az „e” betűt említi).

## Fájlok

- `src/App.java` – a program, az `azonositoGeneralasa` és a `karakterDarab` metódus.

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
$ printf 'Straub\nIngrid\n' | java -cp out App
Vezetéknév: Keresztnév: Teljes név: Straub Ingrid
Azonosító:  string0
$ printf 'Kerekes\nEszter\n' | java -cp out App
Vezetéknév: Keresztnév: Teljes név: Kerekes Eszter
Azonosító:  keresz5
```
