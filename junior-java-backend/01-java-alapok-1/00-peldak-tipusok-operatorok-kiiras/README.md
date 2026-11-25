# Bemutató példák: típusok, konverzió, operátorok, bekérés, kiírás

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (JuniorJavaBackend_01.pdf), elméleti diák (Java adattípusok, Típuskonverzió, Java operátorok, Változók, Konstans, Adatbekérés I–II., Adatkiírás) – lásd a [lecke áttekintőjét](../README.md).

## Feladat

Ez nem számozott feladat, hanem a diákon szereplő kódrészletek és fogalmak futtatható bemutatója:

- a nyolc primitív típus és értéktartománya, túlcsordulás;
- implicit (bővítő) és explicit (szűkítő, kasztolás) konverzió, `Integer.parseInt()`;
- aritmetikai, relációs, logikai, értékadó és egyéb operátorok, precedencia;
- deklaráció, definíció, blokkszintű hatókör, `final` konstans;
- `print` / `println`, vezérlőkarakterek (`\n`, `\t`, `\"`, `\\`), `printf` / `format` formázó karakterek;
- adatbekérés `Scanner`-rel és `BufferedReader`-rel.

## Mit mutat be?

- Miért fontos a típus megválasztása (pl. `(byte) 300` eredménye 44, `17 / 5` eredménye 3).
- A `++i` és `i++` különbségét, a rövidzáras `&&` / `||` működését.
- A `Scanner` és a `BufferedReader` különbségét: a `Scanner` közvetlenül tud számot olvasni,
  a `BufferedReader` csak sorokat ad vissza, és kötelező kezelni (vagy továbbdobni) az `IOException`-t.

## Fájlok

- `src/App.java` – típusok, konverzió, operátorok, hatókör, konstans és formázott kiírás (nem interaktív).
- `src/BekeresScanner.java` – név és életkor bekérése `Scanner`-rel (saját `main` metódus).
- `src/BekeresBufferedReader.java` – ugyanez `BufferedReader`-rel és `Integer.parseInt`-tel.

## Futtatás

A feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
java -cp out BekeresScanner
java -cp out BekeresBufferedReader
```

Példa (részlet az `App` kimenetéből):

```text
Explicit: (int) 9.99 = 9
Explicit: (byte) 300 = 44  (adatvesztés!)
a / b = 3 (egész osztás), a % b = 2 (maradék)
i++ -> 5, ++i -> 7, i = 7
2 + 3 * 4 = 14, (2 + 3) * 4 = 20
```

A tizedesjel (pont vagy vessző) a `printf` kimenetében az operációs rendszer területi beállításától (locale) függ.
