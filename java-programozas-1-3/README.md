# Java programozás 1.3 – gyakorló feladatok

A Java nyelvi alapjait (operátorok, vezérlési szerkezetek, tömbök, enum) bemutató számozott példák. Minden feladat önálló mappa saját `src/` könyvtárral és `README.md`-vel.

| Feladat | Téma |
|---|---|
| [helloWorld](01-hello-world/) | Az első program, a megjegyzések típusai (`//`, `/* */`, `/** */`). |
| [biciklik](02-biciklik/) | Osztály és objektumok: a `Bicycle` osztály példányainak állapotváltozása. |
| [osszeadas](03-osszeadas/) | Számok összegzése 1-től 10-ig `for` ciklussal. |
| [Max](04-max/) | A primitív típusok legnagyobb értékei (`MAX_VALUE`), `final` változó. |
| [aritmetika](05-aritmetika/) | Aritmetikai operátorok egész és lebegőpontos számokkal. |
| [sort](06-sort/) | Buborékrendezés egész számok tömbjén. |
| [relacio](07-relacio/) | Relációs operátorok (`>`, `>=`, `<`, `<=`, `==`, `!=`). |
| [bit](08-bit/) | Bitműveletek: jelzőbitek beállítása és vizsgálata (`|`, `&`). |
| [while](09-while/) | Elöltesztelő `while` ciklus: sztring másolása egy adott karakterig. |
| [doWhile](10-do-while/) | Hátultesztelő `do-while` ciklus: sztring másolása egy adott karakterig. |
| [for](11-for/) | Tömb bejárása számlálós `for` ciklussal. |
| [forEach](12-for-each/) | Tömb bejárása bővített (`for-each`) ciklussal. |
| [ifElse](13-if-else/) | Többágú `if-else if-else` szerkezet: pontszám átváltása osztályzatra. |
| [switch](14-switch/) | `switch` utasítás `break`-kel: hónap nevének kiírása. |
| [switch2](15-switch-2/) | `switch` összevont `case` ágakkal: hónap napjainak száma, szökőév. |
| [enum](16-enum/) | Felsorolt típus (`enum`) használata `switch` utasításban. |
| [break](17-break/) | Keresés tömbben, kilépés a ciklusból `break` utasítással. |

## Futtatás

Build eszköz nélküli, önálló JDK-feladatok. Bármelyik feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

A pontos parancs (főosztály, argumentumok) a feladat saját `README.md`-jében található.
