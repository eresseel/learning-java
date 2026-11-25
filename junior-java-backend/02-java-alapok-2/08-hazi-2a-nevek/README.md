# Házi feladat II.A – Keresztnevek

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Önálló (házi) feladat II.A – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjünk be a felhasználótól tíz keresztnevet; a neveket nagybetűs formában, vektorban tároljuk.
- Listázzuk ki a tömb minden második elemét.
- Állapítsuk meg, melyik a leghosszabb név.
- Írjuk ki, melyik névben van a legtöbb „s” betű.

## Mit mutat be?

- `String[]` tömb, `toUpperCase()`, `length()`, `toCharArray()`.
- Ciklusváltozó léptetése kettesével (`j += 2`).
- Maximumkiválasztás index alapján (az indexet jegyezzük meg, nem az értéket).

Értelmezések / saját döntések:

- „Minden második elem” = a 2., 4., 6., 8., 10. név (az 1, 3, 5, 7, 9 indexek).
- Holtverseny esetén (azonos hossz vagy azonos „s”-szám) az elsőként megadott név nyer.
- Ha egyik névben sincs „s”, azt külön üzenet jelzi.
- Üres nevet nem fogad el, újra kérdez.

## Fájlok

- `src/App.java` – a program és az `sBetukSzama` segédmetódus.

## Futtatás

A feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa:

```bash
printf 'Anna\nBéla\nSzilvia\nKrisztina\nIstván\nSebestyén\nÉva\nTamás\nZsófia\nSándor\n' | java -cp out App
```

```text
Minden második név: BÉLA KRISZTINA SEBESTYÉN TAMÁS SÁNDOR 
Leghosszabb név: KRISZTINA (9 betű)
A legtöbb 's' betű (2 db) ebben a névben van: SEBESTYÉN
```
