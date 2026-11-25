package hu.laci.learning;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Paraméterezett tesztek: ugyanaz a tesztlogika több bemeneti adattal fut le,
 * így nem kell minden adathoz külön tesztmetódust írni.
 */
class PasswordPolicyTest {

    private final PasswordPolicy policy = new PasswordPolicy();

    // @ParameterizedTest: a metódus annyiszor fut le, ahány érték van megadva.
    // @ValueSource: egyetlen paramétert adó, egyszerű értéklista (itt csak érvényes jelszavak).
    @ParameterizedTest
    @ValueSource(strings = {"password", "12345678", "longer-password"})
    void shouldAcceptPasswordsWithAtLeastEightCharacters(String password) {
        assertEquals(true, policy.isValid(password));
    }

    // @CsvSource: soronként két érték (jelszó, elvárt eredmény). Az érték JUnit-ban automatikusan
    // a metódus paramétertípusára (String, boolean) konvertálódik.
    // Az '' (két aposztróf) üres szöveget jelent. A 7 és 8 karakteres jelszó a határérték-vizsgálat.
    @ParameterizedTest
    @CsvSource({
            "short, false",
            "'', false",
            "1234567, false",
            "12345678, true"
    })
    void shouldValidatePasswordLength(String password, boolean expected) {
        assertEquals(expected, policy.isValid(password));
    }
}
