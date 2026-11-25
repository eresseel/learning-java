# Példa – statikusság

> Tananyag: Junior Java Backend fejlesztő III. (OOP alapok), „Statikusság I–V.” diák – lásd a [lecke áttekintőjét](../README.md).

## Feladat

A diák példái egy futtatható programban:

- statikus adattagok: `static String intezmeny = "Ruander Oktatóközpont"`, `static int oktatokSzama`, mellettük a példányszintű `oktatoNeve`;
- statikus metódus, amely példányosítás nélkül hívható;
- statikus blokk (`static { ... }`), kettő is, a futási sorrend megfigyeléséhez;
- statikus beágyazott osztály (`static class Tantargy`) mint segítő osztály.

## Mit mutat be?

- a statikus elemből csak egy van, az osztályhoz és nem a példányhoz tartozik;
- a statikus blokkok az osztály első használatakor, a forráskódbeli sorrendben futnak le;
- a statikus metódus nem éri el a nem statikus mezőket, fordítva viszont igen;
- a beágyazott osztály elérése: `Oktato.Tantargy`.

## Fájlok

- `src/Oktato.java` – statikus mezők, blokkok, metódus és beágyazott osztály.
- `src/App.java` – belépési pont, a statikus elemek használata.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Kimenet eleje (látszik, hogy a statikus blokk csak az első hivatkozáskor fut):

```text
Program indul – az Oktato osztályt még nem használtuk.
[statikus blokk 1] Az Oktato osztály betöltődött.
[statikus blokk 2] Intézmény: Ruander Oktatóközpont
Intézmény: Ruander Oktatóközpont
```
