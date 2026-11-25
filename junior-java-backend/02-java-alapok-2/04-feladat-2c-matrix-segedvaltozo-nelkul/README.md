# Feladat II.C – Mátrix sorszámozása segédváltozó nélkül

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Feladat II.C (és II.B) – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- **A.** Hozzunk létre egy 5x3-as kétdimenziós tömböt, és töltsük fel 1-től kezdődő futó sorszámmal.
  Listázzuk ki az elemeket az elsőtől az utolsóig, majd az utolsótól az elsőig.
- **B.** Debugger használatával lépkedjünk végig a feltöltő és kiíró algoritmus utasításain
  (töréspont, értékek változásának figyelése) – ehhez nem tartozik külön kód, lásd a
  „Gyakorlás a debuggerrel” részt.
- **C.** Oldjuk meg a sorszámozást segédváltozó bevezetése nélkül.

Ez a **C** rész. Az A rész: [03-feladat-2a-matrix-sorszamozas](../03-feladat-2a-matrix-sorszamozas/).

## Mit mutat be?

- Az elem sorszáma kiszámolható az indexekből: `i * oszlopokSzama + j + 1`
  (az i-edik sor előtt i teljes sor, azaz `i * 3` elem áll; a soron belül a j-edik elem jön; 1-től számozunk).
- Ugyanez a képlet visszafelé is használható: a `k`. elem helye `[k / 3][k % 3]` (lásd a házi I. F részét).

## Mi változott az előző lépéshez képest?

- Eltűnt a `sorszam` segédváltozó és a `sorszam++`; helyette képlet számolja az értéket.
- Az oszlopok számát az első sor hosszából vesszük (`matrix[0].length`), nem írjuk be fixen.
- A kimenet változatlan.

## Gyakorlás a debuggerrel (II.B)

1. Tegyünk töréspontot a `matrix[i][j] = i * oszlopokSzama + j + 1;` sorra, és indítsuk Debug módban.
2. A *Watch / Expressions* nézetbe vegyük fel az `i * oszlopokSzama + j + 1` kifejezést: léptetéskor (*Step Over*) látszik, hogy 1, 2, 3, … sorban nő, pedig nincs számláló változó.
3. Próbáljuk ki a feltételes töréspontot `j == 0` feltétellel: csak minden sor elején áll meg – a kifejezés értéke ilyenkor 1, 4, 7, 10, 13.
4. Hasonlítsuk össze az A rész futásával: a mátrix tartalma (a *Variables* nézetben lenyitva) mindkét esetben azonos.

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

Kimenet (megegyezik az A részével):

```text
Az elsőtől az utolsóig:
  1  2  3
  4  5  6
  7  8  9
 10 11 12
 13 14 15
Az utolsótól az elsőig:
 15 14 13
 12 11 10
  9  8  7
  6  5  4
  3  2  1
```
