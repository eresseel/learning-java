# Dolgozat II. – Újságok nyilvántartása

> Tananyag: Dolgozat II. (újságokat nyilvántartó alkalmazás, I–X. feladat) – lásd a [feladatlapok áttekintőjét](../README.md).

## Feladat

Újságokat nyilvántartó alkalmazás, amely általános újságokat, napilapokat és hetilapokat tárol.
Minden újságnak van megnevezése, kiadója és ára; a napilapnak heti megjelenésszáma, a hetilapnak
a megjelenés napja (a hét hányadik napja).

- **I.** Az osztályhierarchia elkészítése (`Ujsag` → `Napilap`, `Hetilap`), valamint két metódus:
  - `afaTartalomSzamit()` – az áfa az ár 5%-a;
  - `hetiArkalkulacio()` – a beszerzés heti költsége; leszármazott **nem** írhatja felül.
- **II.** Az `ujsagok.csv` beolvasása **tömbbe**. A sorokon belül kötőjel az elválasztó:
  - `Ujsag - megnevezés - kiadó - ár`
  - `Napilap - megnevezés - kiadó - ár - heti megjelenés`
  - `Hetilap - megnevezés - kiadó - ár - hét hányadik napja`
- **III.** Setterek hibadobással, az alábbi üzleti szabályok szerint:
  - a megnevezés és a kiadó nem lehet üres;
  - az ár nem lehet negatív;
  - napilap az, ami hetente legalább négyszer megjelenik;
  - a hét napja 1..7.
- **IV.** Az összes hetilap kiírása minden adatával.
- **V.** A hetente legalább hatszor megjelenő napilapok kiírása.
- **VI.** Az általános újságok darabszáma.
- **VII.** A legdrágább sajtótermék megnevezése és áfatartalma.
- **VIII.** A fájlban szereplő első napilap megnevezése és heti költsége.
- **Gyakorlás (a dolgozaton kívül):**
  - **IX.** Osztályszinten ellenőrzött adatbekérés.
  - **X.** A bekért példány hozzáfűzése a CSV fájlhoz.

> A mintaadat (`ujsagok.csv`) saját készítésű, az eredeti fájl nem volt a tananyag része.
> A lapok és a kiadók nevei kitaláltak.

## Mit mutat be?

- **Öröklődés és polimorfizmus.** A `Napilap` és a `Hetilap` kiegészíti az `Ujsag`-ot. Az elemek egy
  `Ujsag[]` tömbben vannak, és a `toString()` mindig a valódi típus adatait írja ki.
- **`final` metódus + sablonmetódus (Template Method).** A `hetiArkalkulacio()` `final`, ezért nem lehet
  felülírni. A heti költség mégis típusonként más: általános újságnál és hetilapnál az ár, napilapnál
  `ár × heti megjelenés`. Ezt a felülírható `protected int hetiPeldanyszam()` segédmetódus oldja meg:
  a számítás menetét a `final` metódus rögzíti, a változó részt a leszármazott adja meg.
- **Ellenőrzött setterek, kivételek.** Érvénytelen adatnál a setterek `IllegalArgumentException`-t
  dobnak. A konstruktor is a settereket hívja, így hibás objektum nem jöhet létre.
- **Osztályszintű ellenőrzés.** A szabályok statikus `ellenoriz...` metódusokban vannak. Ezeket a
  setterek és a konzolos bekérő (`UjsagBekero`) is meghívja, így a szabály egyetlen helyen van leírva.
- **`instanceof` és `getClass()` különbsége.** A VI. feladatnál `getClass() == Ujsag.class` kell,
  mert egy napilap is `instanceof Ujsag`.
- Mintaillesztéses `instanceof` (`u instanceof Napilap n`), `switch` kifejezés, maximumkiválasztás,
  lineáris keresés.
- **Fájlkezelés.** Beolvasás a `Files.readAllLines` metódussal, hozzáfűzés `StandardOpenOption.APPEND`
  móddal.
- **Robusztus konzolos bekérés.** Hibás adatnál a program újrakérdez. Ha a bemenet elfogy (EOF),
  kivétel nélkül kilép.
- **JUnit 6 tesztek.** Paraméterezett tesztek, ideiglenes mappa (`@TempDir`) a fájlos teszteknél, és
  egy reflexiós ellenőrzés arra, hogy a `hetiArkalkulacio()` valóban `final`.

## Fájlok

- `pom.xml` – Maven projekt (Java 21, JUnit 6.1.3).
- `ujsagok.csv` – minta bemeneti fájl (11 sor, mindhárom típusból).
- `src/main/java/hu/learning/feladatlapok/ujsagok/Ujsag.java` – az ősosztály: ellenőrzött setterek,
  `afaTartalomSzamit()`, `final hetiArkalkulacio()`.
- `.../Napilap.java` – heti megjelenés (4..7), felülírja a `hetiPeldanyszam()` metódust.
- `.../Hetilap.java` – a megjelenés napja (1..7).
- `.../UjsagFajlKezelo.java` – a CSV sor ↔ objektum átalakítás, beolvasás tömbbe, hozzáfűzés.
- `.../UjsagBekero.java` – ellenőrzött konzolos bekérés (IX.).
- `.../App.java` – belépési pont: a II. és a IV–VIII. feladat, majd az opcionális IX–X.
- `src/test/java/.../UjsagTest.java` – az áfa, a heti költség, a `final` módosító és a setterek
  kivételeinek tesztjei.
- `src/test/java/.../UjsagFajlKezeloTest.java` – a CSV feldolgozás, a beolvasás és a hozzáfűzés
  tesztjei (`@TempDir`).

## Futtatás

Fordítás és tesztek (a projekt mappájából):

```bash
mvn -B -q clean verify
```

Futtatás (a projekt mappájából, hogy a relatív `ujsagok.csv` útvonal működjön):

```bash
java -cp target/classes hu.learning.feladatlapok.ujsagok.App
```

Más CSV fájlt is megadhatsz argumentumként. Ez akkor hasznos, ha a hozzáfűzést egy másolaton
szeretnéd kipróbálni:

```bash
cp ujsagok.csv /tmp/ujsagok-proba.csv
java -cp target/classes hu.learning.feladatlapok.ujsagok.App /tmp/ujsagok-proba.csv
```

> Figyelem: ha a IX–X. résznél `i`-t válaszolsz, a program **módosítja** a megadott CSV fájlt.
> Az eredeti mintafájlt a `git checkout ujsagok.csv` paranccsal állíthatod vissza.

Példa kimenet (a IV–VIII. feladat):

```text
Beolvasott sajtótermékek száma: 11

IV. Hetilapok:
  Hetilap [megnevezés: Kertbarat Het, kiadó: Zold Lap Kft., ár: 690 Ft, megjelenés: a hét 4. napja (csütörtök)]
  Hetilap [megnevezés: Sport Hetvege, kiadó: Palyaszeli Kiado Kft., ár: 590 Ft, megjelenés: a hét 6. napja (szombat)]
  Hetilap [megnevezés: Gazdasagi Figyelo Plusz, kiadó: Pengo Lapkiado Zrt., ár: 1250 Ft, megjelenés: a hét 3. napja (szerda)]
  Hetilap [megnevezés: Csaladi Het, kiadó: Otthon Media Kft., ár: 450 Ft, megjelenés: a hét 5. napja (péntek)]

V. Legalább hetente hatszor megjelenő napilapok:
  Napilap [megnevezés: Reggeli Hirmondo, kiadó: Fiktiv Kiado Kft., ár: 350 Ft, heti megjelenés: 6]
  Napilap [megnevezés: Orszagos Napi, kiadó: Fiktiv Kiado Kft., ár: 420 Ft, heti megjelenés: 7]

VI. Általános újságok száma: 3 db

VII. A legdrágább sajtótermék: Utazo Magazin, áfatartalma: 99.50 Ft

VIII. Az első napilap: Reggeli Hirmondo, heti költsége: 2100 Ft
```

Példa a gyakorló részre, pipe-olt bemenettel (először hibás, majd helyes adatokkal):

```bash
printf 'i\nFolyoirat\nNapilap\n\nProba Napi\nProba Kiado Kft.\nabc\n-5\n320\n3\n5\nn\n' \
  | java -cp target/classes hu.learning.feladatlapok.ujsagok.App /tmp/ujsagok-proba.csv
```

```text
IX–X. Szeretne új sajtóterméket felvenni? (i/n): Típus (Ujsag / Napilap / Hetilap):   Hiba: Ismeretlen típus: Folyoirat
Típus (Ujsag / Napilap / Hetilap): Megnevezés:   Hiba: A(z) megnevezés nem lehet üres!
Megnevezés: Kiadó: Ár (Ft):   Hiba: egész számot adjon meg!
Ár (Ft):   Hiba: Az ár nem lehet negatív! (megadott: -5)
Ár (Ft): Heti megjelenés (4..7):   Hiba: Napilap legalább 4-szer, legfeljebb 7-szer jelenik meg egy héten! (megadott: 3)
Heti megjelenés (4..7): Hozzáfűzve a(z) /tmp/ujsagok-proba.csv fájlhoz: Napilap - Proba Napi - Proba Kiado Kft. - 320 - 5
IX–X. Szeretne új sajtóterméket felvenni? (i/n): Viszlát!
```

## Saját döntések

- Az ár egész forint (`int`), az áfa `double` (pl. 99,50 Ft).
- Heti költség: általános újságnál és hetilapnál 1 példány/hét, napilapnál annyi példány, ahányszor
  hetente megjelenik.
- A napilap heti megjelenése felülről 7-tel korlátos, mert egy hétben ennyi nap van.
- A VII. feladatnál, ha több termék ára azonos, az elsőt választjuk.
- A kötőjel az elválasztó karakter, ezért a bekérő nem enged kötőjelet a megnevezésben és a kiadó
  nevében.
- Ha a bemenet üres vagy elfogyott, a program a gyakorló részt kihagyja, és kivétel nélkül befejeződik.
