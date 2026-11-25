# Feladat II.A – Mit üzen nekünk Charles Dickens és George Bernard Shaw?

> Tananyag: Junior Java Backend fejlesztő VII., Feladat II.A – lásd a [lecke áttekintőjét](../README.md).

## Feladat

„Hallgatózzunk a világhálón! Mit üzen nekünk Charles Dickens és George Bernard Shaw?!”

### Értelmezés

A feladat szövege tág, ezért így értelmeztük: a program **HTTP-n kérjen le
idézeteket** a két szerzőtől egy nyilvános webes forrásból, és írja ki őket.

- **Elsődleges forrás:** a Quotable idézet-API egy kulcs nélkül használható nyilvános példánya
  (`https://api.quotable.kurokeita.dev/api/quotes?author=charles-dickens&limit=10`), JSON választ ad.
- **Tartalék forrás:** ha az API nem elérhető vagy nem ad találatot, a szerző
  angol Wikiquote oldala a MediaWiki API-n keresztül
  (`https://en.wikiquote.org/w/api.php?action=parse&prop=wikitext&page=Charles_Dickens`);
  a wikiszövegből a felső szintű felsorolás-elemeket (az idézeteket) vesszük ki.

A Quotable-tükör egy magánszemély által üzemeltetett szolgáltatás, ezért nem
garantált, hogy sokáig elérhető marad – ezért van tartalék forrás. A
program 2026 novemberében mindkét forrással működött (Shaw-tól a Quotable
csak 2 idézetet ad).

## Mit mutat be?

- A JDK beépített HTTP kliense (`java.net.http.HttpClient`, Java 11+): `HttpRequest` builder, fejlécek (User-Agent), időkorlát, `send` + `BodyHandlers.ofString()`, státuszkód ellenőrzése.
- URL paraméterek kódolása: `URLEncoder.encode`.
- JSON feldolgozás a GSON **fa-modelljével** (`JsonParser` → `JsonObject`/`JsonArray`), osztálytérkép nélkül.
- Szövegtisztítás reguláris kifejezésekkel (wiki-jelölés eltávolítása).
- Hibakezelés tartalék forrással (`try`/`catch` több kivételtípusra, `InterruptedException` helyes kezelése).
- Tesztelés internet nélkül: a parserek mentett mintaválaszokon (`src/test/resources`) futnak.

## Fájlok

- `pom.xml` – függőség: `gson`.
- `src/main/java/hu/learning/junior/idezetek/Idezet.java` – egy idézet (`record`).
- `src/main/java/hu/learning/junior/idezetek/IdezetKliens.java` – HTTP lekérések a két forrásból.
- `src/main/java/hu/learning/junior/idezetek/QuotableParser.java` – a Quotable JSON feldolgozása.
- `src/main/java/hu/learning/junior/idezetek/WikiquoteParser.java` – a Wikiquote wikiszöveg feldolgozása.
- `src/main/java/hu/learning/junior/idezetek/App.java` – belépési pont, tartalék logika.
- `src/test/resources/quotable-minta.json`, `wikiquote-minta.json` – saját készítésű, lerövidített mintaválaszok.
- `src/test/java/hu/learning/junior/idezetek/ParserTest.java` – parser tesztek.

## Futtatás

```bash
mvn -B -q verify
```

```bash
mvn -B -q compile exec:java                          # Quotable, szükség esetén Wikiquote
mvn -B -q exec:java -Dexec.args="--wikiquote"        # csak a tartalék forrás
```

Példa kimenet (az idézetek futásonként változhatnak):

```text
=== Mit üzen nekünk Charles Dickens? ===
 - „There is a wisdom of the head, and a wisdom of the heart.” – Charles Dickens [quotable]
 ...

=== Mit üzen nekünk George Bernard Shaw? ===
 - „A life spent making mistakes is not only more honorable, but more useful than a life spent doing nothing.” – George Bernard Shaw [quotable]
 ...
```
