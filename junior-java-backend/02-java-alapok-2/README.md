# 02 – Java alapok II.

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (tömbök, alprogramok), JuniorJavaBackend_02.pdf.

A lecke a tömbökkel (vektor, mátrix), a beépített karakter-, sztring-, matematikai és dátumfüggvényekkel,
a véletlenszám-generátorral, a metódusokkal és a metódus-túltöltéssel, a rendező és kereső algoritmusokkal,
valamint az alapszintű fájlkezeléssel foglalkozik.

## Témák

- Egy- és kétdimenziós tömbök, eltérő hosszú sorok, bővített for ciklus (for-each)
- Debugger használata: töréspont, léptetés, változók figyelése
- `Character`, `String`, `Math` metódusok, dátumkezelés (`java.time`)
- `Random` – véletlenszámok adott tartományban
- Metódusok visszatérési értékkel és anélkül, paraméterátadás, tiszta kód
- Metódus-túltöltés (overloading), metódus szignatúra
- Rendezések (kiválasztásos, beszúrásos), bináris keresés
- Fájlkezelés (`BufferedReader`, `BufferedWriter`, try-with-resources), menüvezérelt program

## Feladatok

A mappák a tananyag sorrendjét követik (a házi feladatok ott szerepelnek, ahol a PDF-ben).

| Feladat | Téma |
|---|---|
| [00 – Bemutató példák](00-peldak-tombok-fuggvenyek/) | A diák kódrészletei: tömbök, `Character` / `String` / `Math` / dátum, `Random`, metódusok, túltöltés |
| [Feladat I.A – Osztályzatok](01-feladat-1a-osztalyzatok/) | Tömb feltöltése, listázása, átlag – indexes `for` |
| [Feladat I.B – Osztályzatok for-each-csel](02-feladat-1b-osztalyzatok-foreach/) | Ugyanez bővített for ciklussal |
| [Feladat II.A (+II.B) – 5x3-as mátrix](03-feladat-2a-matrix-sorszamozas/) | Mátrix feltöltése segédváltozóval, előre és visszafelé listázás; gyakorlás a debuggerrel |
| [Feladat II.C (+II.B) – Segédváltozó nélkül](04-feladat-2c-matrix-segedvaltozo-nelkul/) | Sorszám az indexekből kiszámolva; gyakorlás a debuggerrel |
| [Házi I. – Tíz egész szám](05-hazi-1-tiz-egesz-szam/) | A–G részfeladatok, 1D → 2D másolás |
| [Feladat III. – Felhasználónév](06-feladat-3-felhasznalonev/) | Sztringfüggvények, karakterek számlálása |
| [Feladat IV. – Két véletlen tömb](07-feladat-4-veletlen-tombok/) | `Random`, összegzés, megszámlálás, maximum, keresés |
| [Házi II.A – Keresztnevek](08-hazi-2a-nevek/) | `String[]`, leghosszabb név, legtöbb „s” |
| [Házi II.B – Transzponálás](09-hazi-2b-transzponalas/) | Mátrix transzponálása |
| [Házi II.C – Főátló](10-hazi-2c-foatlo/) | Főátló, fordított bejárás |
| [Feladat V. – Metódusokra bontás](11-feladat-5-metodusokra-bontas/) | A Feladat IV. metódusokkal, a `main` csak hívásokat tartalmaz |
| [Házi III. – Vállalkozások](12-hazi-3-vallalkozasok/) | Mátrix vállalkozás × hónap, metódusok |
| [Feladat VI.A – Kerület túltöltéssel](13-feladat-6a-kerulet-tultoltes/) | Metódus-túltöltés paraméterszám alapján |
| [Feladat VI.B – Kör kerülete](14-feladat-6b-kor-kerulete/) | Túltöltés paramétertípus alapján, szignatúra-ütközés |
| [Feladat VI.C – Terület túltöltéssel](15-feladat-6c-terulet-tultoltes/) | Túltöltött területszámítás |
| [Feladat VII.A – Rendezés, bináris keresés](16-feladat-7a-rendezes-binaris-kereses/) | Egyedi véletlenszámok, kiválasztásos rendezés, bináris keresés |
| [Feladat VII.B – Városok ábécérendben](17-feladat-7b-varosok-rendezese/) | Beszúrásos rendezés, magyar `Collator` |
| [Feladat VIII. – Névsor fájlkezeléssel](18-feladat-8-nevsor-fajlkezeles/) | Menü, fájlból olvasás és fájlba írás |

Egymásra épülő lépések: I.A → I.B, II.A → II.C, IV. → V., VI.A → VI.B → VI.C. Minden lépés teljes, önállóan futtatható program.
A II.B feladat (debugger használata) nem igényel külön kódot: a II.A és II.C README-jében
„Gyakorlás a debuggerrel” címmel konkrét töréspont-javaslatok szerepelnek.

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
printf 'Straub\nIngrid\n' | java -cp out App
```

A véletlenszámokat használó programok kimenete minden futáskor más.
