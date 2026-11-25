# 01 – Felhasználónév-validátor TDD-vel

> Tananyag: *Tesztautomatizálás* (ISTQB CTAL-TAE), III. fejezet – tesztautomatizálási megközelítések (TDD); X. feladat (felhasználónév-szabály) – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- A webáruház regisztrációjánál a felhasználónevet ellenőrizni kell.
- A tananyag X. feladatában a SUT naplói szerint az **aktuális szabály**: *minimum 6 karakter, betű vagy szám*. Az `admin12` bemenetet a rendszer elfogadja (`{"valid": true}`).
- A teszteset metaadataiban még egy **régi szabály** szerepel: *csak kisbetű és szám, min. 8 karakter* – ez elavult, a 09. lépésben elemezzük.
- Készítsünk egy `UsernameValidator` osztályt az aktuális szabállyal, **tesztvezérelt fejlesztéssel (TDD)**, határérték-elemzéssel (5/6/7/8 hosszú nevek) és paraméterezett tesztekkel.

## Mit mutat be?

- **TDD** (tesztvezérelt fejlesztés) a „piros → zöld → refaktor” ciklussal. A tananyag szerint előnye az egyszerű tesztesetfejlesztés, jobb kódminőség és lefedettség, a hibák korai megfogása; hátránya, hogy az áttérés időigényes.
- **Komponensteszt** (CTFL ismétlés): egyetlen osztály, kódközeli teszt, fejlesztő végzi, a lokális fejlesztői környezetben fut (II. fejezet: végrehajtási környezetek).
- **Határérték-elemzés**: a 6-os minimum körül 5 / 6 / 7, és a régi szabály határa (8).
- JUnit 6 paraméterezett tesztek: `@ParameterizedTest`, `@CsvSource`, `@ValueSource`, `@NullAndEmptySource`; csoportosítás `@Nested`-del; `@DisplayName`.
- Tesztelhetőség: a `validate()` nem csak igen/nem választ ad, hanem **okot** is (`ValidationResult`) – ez a megfigyelhetőséget javítja.

### A TDD-ciklusok, ahogy a kód készült

| Kör | Piros (előbb a teszt, ami elbukik) | Zöld (minimális kód) | Refaktor |
|---|---|---|---|
| 1 | `admin12IsValid` – még nincs `UsernameValidator` osztály, nem fordul | `validate()` mindig `ok()`-t ad | – |
| 2 | `lengthBoundaries` – az 5 hosszú név is érvényes lenne | hosszellenőrzés `< 6` | a 6-os értéket `MIN_LENGTH` konstansba tesszük |
| 3 | `invalidCharacters` – az `admin_12` átmenne | karakterenkénti ellenőrzés | saját `isAsciiLetterOrDigit()`, mert a `Character.isLetterOrDigit` az ékezetes betűt is engedné |
| 4 | `nullOrEmptyIsInvalid` – `NullPointerException` | külön `null`/üres ág elöl | az okokat `ValidationResult`-ba szervezzük, `isValid()` rövidítés |

A tesztosztály `@Nested` csoportjai ugyanezt a sorrendet követik, így a fájl olvasva is „elmeséli” a fejlesztést.

### Saját döntések

- „Betű” alatt az angol ábécé kis- és nagybetűit értjük (`[a-zA-Z]`); ékezetes betű, szóköz, írásjel nem megengedett.
- Felső hosszkorlát nincs, mert a tananyag sem említ ilyet.

## Fájlok

- `pom.xml` – sima Maven projekt, JUnit 6.1.3 (BOM), Java 21.
- `src/main/java/.../validation/UsernameValidator.java` – a szabály megvalósítása.
- `src/main/java/.../validation/ValidationResult.java` – eredmény + elutasítási ok (record).
- `src/test/java/.../validation/UsernameValidatorTest.java` – 20 teszteset, TDD-körönként csoportosítva.

(A `...` rövidítés: `hu/learning/webshop/tesztautomatizalas`.)

## Futtatás

```bash
mvn -B -q verify
```

Csak a tesztek, részletes kimenettel:

```bash
mvn test
```

Várt eredmény: `Tests run: 20, Failures: 0, Errors: 0, Skipped: 0`.

**TDD gyakorlás:** kommentezd ki a `validate()` 3. lépését (karakterkészlet), futtasd a teszteket – a `invalidCharacters` tesztek pirosak lesznek. Állítsd vissza – zöld.

## Továbblépés

A [02-webshop-domain](../02-webshop-domain/) lépés ugyanerre a projektre építi a termék- és kosár-domaint.
