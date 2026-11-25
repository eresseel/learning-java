[← Előző: 02-junit5-assertions.md](02-junit5-assertions.md)

---

# JUnit paraméterezett tesztek

**Dokumentáció:** <https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests>

---

## `@ParameterizedTest`

Ugyanazt a tesztmetódust több adatsorral futtatja le. Önmagában nem elég: valamelyik `@...Source` annotációval kell párosítani, amelyik a bemeneti adatokat adja.

---

## `@ValueSource`

Egyetlen paramétert ad meg egy egyszerű értéklistával. Szöveghez (`strings`), egész számhoz (`ints`), logikai értékhez (`booleans`) stb. használható.

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParameterizedTest
@ValueSource(strings = {"password", "12345678", "longer-pass"})
void shouldAcceptValidPassword(String password) {
    assertEquals(true, new PasswordPolicy().isValid(password));
}
```

---

## `@CsvSource`

Soronként több paramétert ad meg vesszővel elválasztva. Az `''` (két aposztróf) üres szöveget jelent. Az értékek a metódus paramétertípusára automatikusan konvertálódnak.

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParameterizedTest
@CsvSource({
    "1, 1",     // postId=1 -> userId=1
    "10, 1",
    "11, 2",
    "100, 10"
})
void shouldMapPostToUser(int postId, int expectedUserId) {
    assertEquals(expectedUserId, apiClient.getPost(postId).userId());
}
```

---

[ Következő: 04-junit5-extensions.md → ](04-junit5-extensions.md)
