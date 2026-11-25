# Java 24 óra alatt – gyakorló feladatok

A könyv fejezeteit követő kis példaprogramok. Minden feladat önálló mappa saját `src/` könyvtárral és `README.md`-vel.

## 1. fejezet – Első lépések

| Feladat | Téma |
|---|---|
| [Saluton](01-fejezet/01-saluton/) | Az első Java program: szöveg kiírása a konzolra. |
| [Root](01-fejezet/02-root/) | Négyzetgyök számítása a `Math.sqrt()` metódussal. |
| [Blanks](01-fejezet/03-blanks/) | Parancssori argumentumok (`args`) beillesztése egy mondatba. |

## 2. fejezet – Változók, sztringek, feltételek

| Feladat | Téma |
|---|---|
| [Variable](02-fejezet/01-variable/) | Primitív típusú változók deklarálása (`int`, `float`, `char`, `byte`, `short`, `long`, `boolean`) és értékkészletük. |
| [Equals](02-fejezet/02-equals/) | Sztringek összehasonlítása az `equals()` metódussal. |
| [indexOf](02-fejezet/03-index-of/) | Részsztring keresése az `indexOf()` metódussal. |
| [Credits](02-fejezet/04-credits/) | Sztringek összefűzése és formázott kiírás (film stáblista). |
| [Game](02-fejezet/05-game/) | Egyszerű `if` feltételek és összetett értékadás (`+=`). |

## 3. fejezet – Tömbök, osztályok, öröklődés

| Feladat | Téma |
|---|---|
| [Array](03-fejezet/01-array/) | Tömbök deklarálása, inicializálása, a `length` mező használata. |
| [NoSpace](03-fejezet/02-no-space/) | Sztring karaktertömbbé alakítása (`toCharArray()`), szóközök cseréje feltételes operátorral. |
| [Name](03-fejezet/03-name/) | Sztringtömb rendezése az `Arrays.sort()` metódussal. |
| [Kerek](03-fejezet/04-kerek/) | Betűgyakoriság számolása mondatokban egymásba ágyazott ciklusokkal. |
| [Tanulok](03-fejezet/05-tanulok/) | Kétdimenziós (szabálytalan) tömb bejárása, tanulói átlagok számítása. |
| [NewRoot](03-fejezet/06-new-root/) | Sztring számmá alakítása (`Integer.parseInt()`), majd négyzetgyök számítása. |
| [Modem](03-fejezet/07-modem/) | Öröklődés: közös `Modem` ősosztály és `CableModem`, `DslModem`, `AcousticModem` leszármazottak. |
| [Virus](03-fejezet/08-virus/) | Osztályváltozók (`static`), getter/setter metódusok, objektumtömb. |
| [Vector](03-fejezet/09-vector/) | Dinamikus lista (`Vector`) feltöltése és rendezése a `Collections.sort()` metódussal. |
| [Point3D](03-fejezet/10-point-3d/) | A `java.awt.Point` kiterjesztése háromdimenziós ponttá, metódusok felülírása. |

## 4. fejezet – Swing grafikus felület

| Feladat | Téma |
|---|---|
| [helloWorld (Swing)](04-fejezet/01-hello-world/) | Az első grafikus ablak `JFrame` segítségével. |
| [Button](04-fejezet/02-button/) | Gombok (`JButton`) elhelyezése `FlowLayout` elrendezéssel. |
| [Clock](04-fejezet/03-clock/) | Saját `JPanel` komponens: az aktuális dátum és idő megjelenítése. |

## Futtatás

Build eszköz nélküli, önálló JDK-feladatok. Bármelyik feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

A pontos parancs (főosztály, argumentumok) a feladat saját `README.md`-jében található.
