[← Előző: 00-index.md](00-index.md)

---

# JUnit 5 annotációk

**Dokumentáció:** <https://junit.org/junit5/docs/current/user-guide/>

---

## `@Test`

Jelöli, hogy a metódus egy futtatandó tesztet jelent. A JUnit a neve szerint keresi meg és hajtja végre.

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    void shouldAddTwoNumbers() {
        int result = 2 + 3;
        assertEquals(5, result);
    }
}
```

---

## `@BeforeEach` / `@AfterEach`

`@BeforeEach`: minden `@Test` metódus **előtt** lefut (pl. böngésző indítása).  
`@AfterEach`: minden `@Test` metódus **után** lefut, akkor is, ha a teszt elbukott (pl. böngésző bezárása).

```java
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseTest {

    private Connection connection;

    @BeforeEach
    void openConnection() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test");
    }

    @AfterEach
    void closeConnection() throws Exception {
        if (connection != null) connection.close();
    }

    @Test
    void shouldInsertRow() { /* ... */ }
}
```

---

## `@BeforeAll`

Az osztály összes tesztje előtt **egyszer** fut le. A metódusnak statikusnak kell lennie (kivéve, ha `@TestInstance(PER_CLASS)` van megadva).

```java
import org.junit.jupiter.api.BeforeAll;
import io.restassured.RestAssured;

class ApiTest {

    @BeforeAll
    static void configureBaseUri() {
        RestAssured.baseURI = "https://api.example.com";
    }

    @Test
    void shouldReturnOk() { /* ... */ }
}
```

---

## `@Disabled`

Átmenetileg kikapcsol egy tesztet. Mindig adj meg okot, különben senki sem tudja, mikor kerülhet vissza.

```java
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    @Disabled("Ismert hiba: a fizetési kapu sandbox módban hibásan válaszol – GH-1234")
    void shouldProcessPayment() { /* ... */ }
}
```

---

## `@DisplayName`

Emberi olvasásra szánt teszt- vagy osztálynevet ad meg. A tesztriportban és az IDE-ben ez jelenik meg.

```java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Bejelentkezési oldal")
class LoginTest {

    @Test
    @DisplayName("Érvényes jelszóval bejelentkezés sikeres")
    void shouldLoginWithValidCredentials() { /* ... */ }
}
```

---

## `@Tag`

Logikai címke, amellyel csoportokra oszthatók a tesztek. A Maven Surefire `-Dgroups=` és `-DexcludedGroups=` kapcsolókkal szűr rájuk.

```java
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("smoke")
class SmokeTest {

    @Test
    void shouldOpenHomePage() { /* ... */ }
}
```

Futtatás csak a `smoke` tesztekre:
```bash
mvn test -Dgroups=smoke
```

---

## `@Nested`

Belső osztállyal logikailag csoportosítja a teszteket. A külső osztály `@BeforeEach` metódusai a belső teszteknél is lefutnak.

```java
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UserTest {

    @Nested
    class WhenLoggedIn {

        @Test
        void shouldSeeProfile() { /* ... */ }
    }

    @Nested
    class WhenLoggedOut {

        @Test
        void shouldBeRedirectedToLogin() { /* ... */ }
    }
}
```

---

## `@EnabledIfSystemProperty`

A teszt csak akkor fut le, ha egy adott rendszer-tulajdonság (system property) az elvárt értékre van állítva.

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

class DemoFailureTest {

    @Test
    @EnabledIfSystemProperty(named = "demoFailure", matches = "true")
    void shouldFailOnPurpose() {
        // Csak a -DdemoFailure=true esetén fut le.
    }
}
```

---

[ Következő: 02-junit5-assertions.md → ](02-junit5-assertions.md)
