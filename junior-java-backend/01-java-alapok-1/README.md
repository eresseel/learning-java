# 01 – Java alapok I.

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (adattípusok, operátorok, változók, vezérlőszerkezetek), JuniorJavaBackend_01.pdf.

A lecke a Java nyelv alapjait mutatja be: a primitív és összetett adattípusokat, a típuskonverziót,
az operátorokat és precedenciájukat, a változók deklarációját, hatókörét és a konstansokat,
az adatbekérést (`Scanner`, `BufferedReader`) és a kiírást (`print`, `println`, `printf`),
valamint a vezérlőszerkezeteket (elágazás, `switch`, `for`, `while`, `do-while`).

## Témák

- Java szintaktika, megjegyzések, a `main` metódus mint belépési pont
- Primitív típusok (`boolean`, `char`, `byte`, `short`, `int`, `long`, `float`, `double`) és összetett típusok
- Implicit és explicit típuskonverzió (kasztolás), `Integer.parseInt()`
- Aritmetikai, relációs, logikai, értékadó és egyéb operátorok, precedencia
- Változók, hatókör, `final` konstans
- Adatbekérés és -kiírás, vezérlőkarakterek, formázott kiírás
- Elágazások, esetszétválasztás, ciklusok
- Tiszta kód: beszédes elnevezések

## Feladatok

| Feladat | Téma |
|---|---|
| [00 – Bemutató példák](00-peldak-tipusok-operatorok-kiiras/) | A diák kódrészletei: típusok, konverzió, operátorok, hatókör, `printf`, `Scanner` vs. `BufferedReader` |
| [Feladat I. – Hello World!](01-feladat-1-hello-world/) | Az első program, `main` metódus, `println` |
| [Feladat II.A – Név bekérése](02-feladat-2a-nev-bekerese/) | `Scanner.nextLine()`, vezérlőkarakterek |
| [Feladat II.B – Téglalap](03-feladat-2b-teglalap/) | Valós számok bekérése, `printf` formázás |
| [Feladat III.A, 1. lépés – Egyjegyű?](04-feladat-3a-lepes-1-egyjegyu/) | Egyszerű elágazás (`if`) |
| [Feladat III.A, 2. lépés – Nem egyjegyű](05-feladat-3a-lepes-2-nem-egyjegyu/) | Kétirányú elágazás (`if` – `else`), `Math.abs` |
| [Feladat III.A, 3. lépés – Jegyek száma](06-feladat-3a-lepes-3-jegyek-szama/) | Többágú elágazás (`else if`) |
| [Feladat III.B – Osztályzat szövegesen](07-feladat-3b-osztalyzat-szovegesen/) | `switch` – `case` – `break` – `default` |
| [Feladat IV.A – Egytől tízig](08-feladat-4a-egytol-tizig/) | Számlálós ciklus (`for`) |
| [Feladat IV.B – Száztól hármasával](09-feladat-4b-szaztol-harmasaval/) | `do-while` és `while`, maradékos osztás |
| [Feladat IV.C – Páratlan számig](10-feladat-4c-paratlan-szamig/) | Hátultesztelős ciklus bekéréssel, `continue` |
| [Házi A – Magasságok](11-hazi-a-magassagok/) | Háromágú elágazás |
| [Házi B – Hatványozás](12-hazi-b-hatvanyozas/) | Gyűjtőváltozó ciklusban |
| [Házi C – Számvizsgálat elágazással](13-hazi-c-szamvizsgalat-elagazassal/) | Tartományok vizsgálata `if`-fel |
| [Házi C – Számvizsgálat switch-csel](14-hazi-c-szamvizsgalat-switch/) | Ugyanez `switch`-csel, szándékos átesés |

A III.A feladat a tananyag szerint fokozatosan bővül, ezért három önálló lépésmappára bontottuk.
A házi C feladatot a tananyag kétféle megoldással kéri; a két megoldás két külön mappában van,
így mindkettő önállóan futtatható és könnyen összehasonlítható.

## Futtatás

Minden feladat egy önálló, build eszköz nélküli program (JDK 21 vagy újabb kell).
A feladat mappájába lépve:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Az interaktív programok bemenete pipe-pal is megadható, például:

```bash
printf 'Kiss\nAnna\n' | java -cp out App
```
