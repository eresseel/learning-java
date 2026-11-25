# Feladat II.B – a ruander.hu tanfolyamai (megnevezés, ár)

> Tananyag: Junior Java Backend fejlesztő VII., Feladat II.B – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjük le a ruander.hu oldal tartalmát.
- Tároljuk adatszerkezetben a tanfolyamok adatait (megnevezés, ár).

## Megoldás és korlátok

- A program a **főoldalt** (`https://www.ruander.hu/`) tölti le: a kurzuskártyákon szerepel a név és az ár. Paraméterként kategóriaoldal is megadható (pl. `programozo-tanfolyamok.html`), ott a „nettó Ft+áfa (bruttó Ft)” formátumból a bruttó árat vesszük.
- Az eredmény `List<Tanfolyam>` (`record Tanfolyam(String megnevezes, int ar)`); a több fülön is megjelenő tanfolyamok csak egyszer kerülnek be, az ár nélküli kártyák (pl. vizsgainformáció) kimaradnak. 2026 novemberében a főoldalról 72 tanfolyam jött le.
- **Ez „képernyőkaparás”** (web scraping) reguláris kifejezésekkel: a feldolgozó az oldal aktuális HTML szerkezetére (`<span class="course-name">`, `<span class="currency">`) épül. Ha az oldal szerkezete megváltozik, a program „Nem találtam tanfolyamot” üzenetet ad – ilyenkor a mintákat kell igazítani. Ezért a parser egységtesztje egy **mentett, saját készítésű mintaoldalon** fut (`src/test/resources/ruander-minta.html`), és nem függ az élő oldaltól.
- Valódi projektben HTML-elemző könyvtár (pl. jsoup) vagy hivatalos API való erre; a leckében szándékosan csak a JDK eszközeit használjuk.

## Mit mutat be?

- `HttpClient` (Java 21-től `AutoCloseable`, try-with-resources), átirányítás követése, időkorlát.
- `Pattern`/`Matcher`: `(?s)` mód, lusta illesztés (`.*?`), csoportok (`group(1)`), `find()` ciklus.
- `LinkedHashMap.putIfAbsent`: ismétlődések kiszűrése a sorrend megtartásával.
- Stream rendezés (`Comparator.comparingInt(...).reversed()`), magyar ezres tagolás (`Locale.of("hu", "HU")`).
- Tesztelés mentett mintán, élő hálózat nélkül.

## Fájlok

- `pom.xml` – csak JUnit (a program a JDK-n kívül nem használ könyvtárat).
- `src/main/java/hu/learning/junior/tanfolyamok/Tanfolyam.java` – egy tanfolyam (`record`).
- `src/main/java/hu/learning/junior/tanfolyamok/RuanderParser.java` – HTML → tanfolyamlista.
- `src/main/java/hu/learning/junior/tanfolyamok/App.java` – letöltés, feldolgozás, kiírás.
- `src/test/resources/ruander-minta.html` – saját készítésű mintaoldal a valódi szerkezet alapján (főoldali és kategóriaoldali kártyák, akciós ár, duplikátum, ár nélküli kártya).
- `src/test/java/hu/learning/junior/tanfolyamok/RuanderParserTest.java` – parser tesztek.

## Futtatás

```bash
mvn -B -q verify
```

```bash
mvn -B -q compile exec:java
mvn -B -q exec:java -Dexec.args="https://www.ruander.hu/programozo-tanfolyamok.html"
```

Példa kimenet (részlet; az árak az oldaltól függően változhatnak):

```text
72 tanfolyam (https://www.ruander.hu/):
     990 000 Ft  Junior Frontend fejlesztő + Webfejlesztő tanfolyam
     890 000 Ft  Információbiztonsági rendszerüzemeltető tanfolyam
     790 000 Ft  Junior szoftvertesztelő + automata tesztelő tanfolyam
     ...
     690 000 Ft  Junior Java Backend fejlesztő tanfolyam
     ...
```
