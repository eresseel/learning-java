# Példa – beépített interfészek

> Tananyag: Junior Java Backend fejlesztő V., „Interface VIII.” dia (fontosabb beépített interfészek) – lásd a [lecke áttekintőjét](../README.md).

## Feladat

A diasoron felsorolt beépített interfészek egy-egy rövid példája:

- **Comparable** – az implementáló osztály sorrend szerint összehasonlítható (`compareTo()`);
- **Cloneable** – az implementáló osztály klónozható (`clone()`);
- **Closeable** – az implementáló osztály felszabadíthatja a használt erőforrásokat (`close()`);
- **Iterable** – az implementáló osztály bejárható foreach szerkezettel.

## Mit mutat be?

- generikus interfész implementálása (`Comparable<Termek>`, `Iterable<Termek>`);
- `Arrays.sort` a természetes rendezéssel;
- sekély másolat `clone()`-nal;
- saját `Iterator` névtelen belső osztállyal;
- try-with-resources.

## Fájlok

- `src/Termek.java` – `Comparable` és `Cloneable`.
- `src/Polc.java` – `Iterable`.
- `src/Naplo.java` – `Closeable`.
- `src/App.java` – belépési pont.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```
