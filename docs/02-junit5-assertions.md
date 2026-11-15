[← Előző: 01-junit5-annotations.md](01-junit5-annotations.md)

---

# JUnit 5 assertions (ellenőrzések)

**Dokumentáció:** <https://junit.org/junit5/docs/current/api/org.junit.jupiter.api/org/junit/jupiter/api/Assertions.html>

Minden metódus a `org.junit.jupiter.api.Assertions` osztályból statikusan importálható.

---

## `assertEquals(expected, actual)`

Az elvárt és a tényleges értéket hasonlítja össze. Az első paraméter mindig az **elvárt**, a második a **tényleges** érték – ez a sorrend befolyásolja a hibaüzenet érthetőségét.

```java
import static org.junit.jupiter.api.Assertions.assertEquals;

@Test
void shouldCalculateSum() {
    int result = new Calculator().add(2, 3);
    assertEquals(5, result);           // assertEquals(elvárt, tényleges)
    assertEquals(5, result, "Az összeadás eredménye nem megfelelő");  // opcionális üzenet
}
```

---

## `assertTrue(condition)` / `assertFalse(condition)`

Logikai feltételt ellenőriz.

```java
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Test
void shouldCheckVisibility() {
    assertTrue(element.isDisplayed(), "Az elemnek láthatónak kell lennie");
    assertFalse(errorPanel.isDisplayed(), "A hibasávnak rejtettnek kell lennie");
}
```

---

## `assertNotNull(object)`

Ellenőrzi, hogy az érték nem null.

```java
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Test
void shouldReturnDriver() {
    WebDriver driver = DriverFactory.getDriver();
    assertNotNull(driver, "A WebDriver nem lehet null");
}
```

---

## `assertSame(expected, actual)`

Referencia-egyenlőséget ellenőriz (`==`), nem `equals()`-t. Akkor hasznos, ha azt akarjuk bizonyítani, hogy két változó **ugyanarra az objektumra** mutat.

```java
import static org.junit.jupiter.api.Assertions.assertSame;

@Test
void shouldReturnSameDriverInstance() {
    WebDriver a = DriverFactory.getDriver();
    WebDriver b = DriverFactory.getDriver();
    assertSame(a, b);  // ugyanaz a példány, nem csak egyenlő tartalom
}
```

---

## `assertThrows(exceptionType, executable)`

Ellenőrzi, hogy a lambdában lévő kód a várt típusú kivételt dobja. Visszaadja a kivételt, így annak üzenete is vizsgálható.

```java
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Test
void shouldRejectBlankValue() {
    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> TextUtils.requireNonBlank("")
    );
    assertTrue(ex.getMessage().contains("blank"));
}
```

---

[ Következő: 03-junit5-parameterized.md → ](03-junit5-parameterized.md)
