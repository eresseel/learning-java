# `org.junit.jupiter.api.Assertions` – osztályszintű használat

**Dokumentáció:** <https://junit.org/junit5/docs/current/api/org.junit.jupiter.api/org/junit/jupiter/api/Assertions.html>

---

## Statikus import vs. osztályszintű hívás

A JUnit 5 `Assertions` osztályának metódusai kétféleképpen hívhatók. A statikus import olvashatóbb és rövidebb, az osztályszintű hívás viszont explicit: a kódból egyértelműen látszik, hogy ez egy JUnit assertion.

```java
// 1. Statikus importtal (a learning zip-ben használt módszer):
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

assertEquals(5, result);
assertTrue(page.isDisplayed());

// 2. Az Assertions osztályon keresztül (az orangehrm zip-ben használt módszer):
import org.junit.jupiter.api.Assertions;

Assertions.assertEquals(5, result);
Assertions.assertTrue(page.isDisplayed());
```

A két módszer **teljesen egyenértékű**, csak a hívás stílusa különbözik. Egy projekten belül érdemes egységesen az egyiket használni.

---

## Leggyakoribb metódusok

```java
import org.junit.jupiter.api.Assertions;

// Értékegyenlőség:
Assertions.assertEquals("Form submitted for Laci", page.getStatusMessage());

// Feltétel igaz:
Assertions.assertTrue(dashboardPage.isDashboardDisplayed(),
        "Az irányítópultnak láthatónak kell lennie");

// Feltétel hamis:
Assertions.assertFalse(firstUser.trim().isEmpty(),
        "A felhasználónév nem lehet üres");

// Nem null:
Assertions.assertNotNull(firstUser, "A felhasználónév nem lehet null");

// Kivétel elvárt:
Assertions.assertThrows(IllegalArgumentException.class,
        () -> service.process(null));
```

---

## `@CsvFileSource`

Paraméterezett teszt bemeneti adatait CSV fájlból olvassa, nem kódból. Az adatok és a teszt logikája így elválik egymástól: az adatok módosításához nem kell a Java kódot szerkeszteni.

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.api.Assertions;

@ParameterizedTest
// resources: a fájl elérési útja a classpath gyökeréhez képest (src/test/resources/).
// numLinesToSkip: az első sor fejléc (pl. "firstName,lastName"), ezért kihagyjuk.
@CsvFileSource(resources = "/testdata.csv", numLinesToSkip = 1)
void shouldAddEmployee(String firstName, String lastName) {
    boolean isSaved = pimPage.addEmployee(firstName, lastName);
    Assertions.assertTrue(isSaved,
            "Az új adat (" + firstName + " " + lastName + ") bevitele nem volt sikeres!");
}
```

A `testdata.csv` fájl (`src/test/resources/testdata.csv`):
```
firstName,lastName
John,Doe
Jane,Smith
Teszt,Elek
```

### Összehasonlítás a `@CsvSource`-szal

| | `@CsvSource` | `@CsvFileSource` |
|---|---|---|
| Adatok helye | Java kódban, az annotációban | Külön CSV fájlban |
| Módosítás | Kód szerkesztése kell | Csak a fájlt kell módosítani |
| Sok adatsor | Nehézkes | Jól skálázódik |
| Megosztás | A kóddal együtt | Fájlként önállóan is átadható |

