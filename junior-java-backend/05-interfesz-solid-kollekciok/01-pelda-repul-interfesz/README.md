# Példa – RepulInterface (repülő dolgok lánc, 4. lépés)

> Tananyag: Junior Java Backend fejlesztő V. (Interface, S.O.L.I.D, kollekciók), „Interface I–VII.” diák – lásd a [lecke áttekintőjét](../README.md).

## Feladat

A diák példája: a program többi része miatt más hierarchiát kell követnünk.

```text
                         Minden  ┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄  «interface» RepulInterface
            /              |              \            (repul(), maxSebesseg)
       Allatok           Gepek          Targyak
       /     \          /     \         /     \
  Emlosok  Madarak  Elektronika Jarmuvek Diszek Jatekok
            /   \                /   \            |
          Sas  Vereb          Auto  Repulo      Labda
```

- A `Sas`, `Vereb`, `Repulo` és `Labda` továbbra is repül, de legközelebbi közös ősük a `Minden`, amely nem kényszerítheti a `repul()`-t minden leszármazottra (pl. az `Auto` nem repül).
- Megoldás: `RepulInterface` a `maxSebesseg = 300000` konstanssal és a `repul()` metódussal; a repülő osztályok implementálják.

## Mit mutat be?

- `interface`, `implements`; az interfész mezői `public static final`, metódusai `public abstract`;
- egy osztály egy őst örököl, de több interfészt implementálhat (a dupla öröklés helyett);
- interfész mint gyűjtő típus (`RepulInterface[]`), `instanceof` mintaillesztéssel;
- absztrakt osztályból származó absztrakt osztály (több szintű absztrakt hierarchia).

## Mi változott az előző lépéshez képest?

- Az [előző lépésben](../../04-oop-2-oroklodes-kivetelek/02-pelda-repulodolgok-absztrakt/) a `RepuloDolgok` absztrakt osztály volt a közös ős, és ő írta elő a `repul()`-t.
- Most a hierarchia gyökere a `Minden`, a repülők szétszóródnak a különböző ágakon, a `repul()` előírása pedig a `RepulInterface`-be került.
- Ez a lánc utolsó lépése (az első: [03 – Jármű polimorfizmus](../../03-oop-alapok/03-pelda-jarmu-polimorfizmus/)).

## Fájlok

- `src/RepulInterface.java` – az interfész.
- `src/Minden.java` – a hierarchia absztrakt gyökere.
- `src/Allatok.java`, `src/Gepek.java`, `src/Targyak.java`, `src/Emlosok.java`, `src/Madarak.java`, `src/Elektronika.java`, `src/Jarmuvek.java`, `src/Diszek.java`, `src/Jatekok.java` – absztrakt köztes osztályok.
- `src/Sas.java`, `src/Vereb.java`, `src/Repulo.java`, `src/Labda.java` – repülő, konkrét osztályok.
- `src/Auto.java` – nem repülő konkrét osztály (ellenpélda).
- `src/App.java` – belépési pont.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```
