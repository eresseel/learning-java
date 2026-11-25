# Feladat II.A – 5x3-as mátrix futó sorszámmal

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Feladat II.A és II.B – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- **A.** Hozzunk létre egy 5x3-as kétdimenziós tömböt, és töltsük fel 1-től kezdődő futó sorszámmal.
  Listázzuk ki az elemeket az elsőtől az utolsóig, majd az utolsótól az elsőig.
- **B.** Debugger használatával lépkedjünk végig a feltöltő és kiíró algoritmus utasításain
  (töréspont, értékek változásának figyelése) – ehhez nem tartozik külön kód, lásd a
  „Gyakorlás a debuggerrel” részt.
- **C.** Oldjuk meg a sorszámozást segédváltozó bevezetése nélkül.

Ez az **A** rész (és a B rész gyakorlása). A C rész: [04-feladat-2c-matrix-segedvaltozo-nelkul](../04-feladat-2c-matrix-segedvaltozo-nelkul/).

## Mit mutat be?

- Kétdimenziós tömb: `matrix.length` a sorok, `matrix[i].length` az oszlopok száma.
- Egymásba ágyazott ciklusok: a külső a sorokon, a belső az oszlopokon halad végig.
- Segédváltozó (`sorszam`), amely minden értékadás után eggyel nő.
- Visszafelé bejárás: a ciklus `length - 1`-től `0`-ig, csökkenő ciklusváltozóval.
- Formázott, oszlopokba rendezett kiírás: `printf("%3d", ...)`.

## Gyakorlás a debuggerrel (II.B)

A feladat B része az IDE beépített hibakeresőjének (debugger) használatát gyakoroltatja. Javasolt lépések (Eclipse, IntelliJ IDEA vagy VS Code esetén hasonlóan):

1. **Töréspontok:** tegyünk töréspontot (a sorszám melletti margóra kattintva)
   - a `matrix[i][j] = sorszam;` sorra (a feltöltés belső ciklusa),
   - a visszafelé listázás `System.out.printf("%3d", matrix[i][j]);` sorára.
2. **Indítás Debug módban** (Eclipse: *Debug As → Java Application*; IntelliJ: zöld bogár ikon). A program az első töréspontnál megáll.
3. **Változók figyelése:** a *Variables* nézetben figyeljük az `i`, `j` és `sorszam` értékét. Nyissuk le a `matrix` változót: látszik, hogy 5 db 3 elemű tömbből áll, és hogyan telnek fel az elemek.
4. **Léptetés:**
   - *Step Over* (F6 / F8): egy utasítás végrehajtása – figyeljük meg, hogy a `sorszam++` után nő az érték, és hogy a belső ciklus végén `j` 0-ra áll vissza, `i` pedig nő.
   - *Resume* (F8 / F9): futás a következő töréspontig.
5. **Kifejezés figyelése (Watch / Expressions):** vegyük fel a `matrix[i][j]` és az `i * 3 + j + 1` kifejezést – utóbbi mindig megegyezik a `sorszam` értékével; ebből születik a C rész megoldása.
6. **Feltételes töréspont:** a töréspont tulajdonságainál adjuk meg az `i == 3` feltételt – a program csak a 4. sornál áll meg, így nem kell 15-ször léptetni.
7. A visszafelé listázásnál ellenőrizzük, hogy `i` 4-től, `j` 2-től indul, és a ciklus a `-1` értéknél áll le.

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

Kimenet:

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
