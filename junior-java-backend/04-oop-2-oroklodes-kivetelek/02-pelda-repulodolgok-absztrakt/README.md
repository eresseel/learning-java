# Példa – RepuloDolgok absztrakt osztály (repülő dolgok lánc, 3. lépés)

> Tananyag: Junior Java Backend fejlesztő IV. (OOP II.), „Absztrakt osztály I–VII.” diák – lásd a [lecke áttekintőjét](../README.md).

## Feladat

A diák hierarchiája:

```text
           RepuloDolgok (abstract, repul() abstract)
          /       |        \
      Madar     Repulo    Labda
      /    \
    Sas   Vereb
```

- A `RepuloDolgok` egyetlen feladata a közös ős biztosítása és a `repul()` előírása.
- Önállóan nem jöhet létre, a `repul()`-t sem tudja megvalósítani, ezért absztrakt.
- `public abstract void repul();` – a leszármazottak kötelesek megvalósítani.

## Mit mutat be?

- `abstract class`, `abstract` metódus;
- absztrakt osztályban is lehet konstruktor és teljesen megvalósított metódus (`bemutat()`), amely az absztrakt metódust úgy hívja, mintha meg lenne valósítva;
- a leszármazottban megvalósított metódus tovább felülírható (`Sas`), vagy örökölhető (`Vereb`).

## Mi változott az előző lépéshez képest?

- Az [előző lépésben](../01-pelda-repulodolgok-oroklodes/) a `RepuloDolgok` normál osztály volt, saját `repul()` törzzsel, és példányosítható volt.
- Most `abstract`, a `repul()` törzs nélküli, előírt metódus.
- Bekerült a `Repulo` és a `Labda` mint a `Madar` testvérei.
- Folytatás: a [következő lépésben](../../05-interfesz-solid-kollekciok/01-pelda-repul-interfesz/) a repülés képessége interfészbe kerül.

## Fájlok

- `src/RepuloDolgok.java` – absztrakt ős.
- `src/Madar.java`, `src/Sas.java`, `src/Vereb.java` – madarak.
- `src/Repulo.java`, `src/Labda.java` – további leszármazottak.
- `src/App.java` – belépési pont.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```
