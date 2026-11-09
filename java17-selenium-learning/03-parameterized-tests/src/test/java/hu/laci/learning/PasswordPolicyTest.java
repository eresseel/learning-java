package hu.laci.learning;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordPolicyTest {

    private final PasswordPolicy policy = new PasswordPolicy();

    @ParameterizedTest
    @ValueSource(strings = {"password", "12345678", "longer-password"})
    void shouldAcceptPasswordsWithAtLeastEightCharacters(String password) {
        assertEquals(true, policy.isValid(password));
    }

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
