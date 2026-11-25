# Feladat VII.B – Városnevek ábécérendben

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Feladat VII.B – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Egy tömb elemei: Budapest, Debrecen, Szeged, Miskolc, Pécs, Győr, Nyíregyháza, Kecskemét, Székesfehérvár, Szombathely.
- Rendezzük a tömbelemeket ábécé szerint növekvő sorrendbe, és listázzuk ki őket.
- A VII.A feladattól különböző rendezési algoritmust kell használni.

## Mit mutat be?

- **Beszúrásos rendezés** (a VII.A kiválasztásos rendezése helyett): a tömb eleje mindig rendezett, a következő elemet a helyére „csúsztatjuk”, mint a kártyalapokat a kézben.
- Szövegek összehasonlítása: a `String.compareTo` a karakterek Unicode kódja szerint dönt, ezért az ékezetes betűket rossz helyre tenné
  (pl. a „Székesfehérvár” a „Szombathely” mögé kerülne). A `java.text.Collator` magyar beállítással (`Locale.of("hu", "HU")`) a magyar ábécé szerint hasonlít.

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

Kimenet (a rendezett rész):

```text
Ábécé szerint növekvő sorrendben:
 1. Budapest
 2. Debrecen
 3. Győr
 4. Kecskemét
 5. Miskolc
 6. Nyíregyháza
 7. Pécs
 8. Szeged
 9. Székesfehérvár
10. Szombathely
```
