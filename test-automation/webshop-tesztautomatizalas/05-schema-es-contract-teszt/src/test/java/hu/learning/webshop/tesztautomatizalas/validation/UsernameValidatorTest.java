package hu.learning.webshop.tesztautomatizalas.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * A {@link UsernameValidator} komponenstesztjei.
 *
 * <p>A tesztek sorrendje a TDD-ciklusokat követi (lásd README): minden {@code @Nested}
 * csoport egy-egy „piros → zöld → refaktor” kör eredménye. Minden teszt az
 * Arrange / Act / Assert (előkészítés / végrehajtás / ellenőrzés) szerkezetet követi.</p>
 */
@DisplayName("UsernameValidator – aktuális szabály: min. 6 karakter, betű vagy szám")
class UsernameValidatorTest {

    /** A tesztelt objektum (SUT ezen a szinten: egyetlen osztály). */
    private UsernameValidator validator;

    /** Minden teszt előtt új példány: a tesztek egymástól függetlenek maradnak. */
    @BeforeEach
    void setUp() {
        validator = new UsernameValidator();
    }

    /** 1. TDD-kör: a tananyag X. feladatának konkrét bemenete. */
    @Nested
    @DisplayName("1. kör – a X. feladat példája")
    class FeladatXPelda {

        /** Az „admin12” (7 karakter, betű+szám) az AKTUÁLIS szabály szerint érvényes. */
        @Test
        @DisplayName("admin12 érvényes (a SUT is elfogadja: {\"valid\": true})")
        void admin12IsValid() {
            // Arrange – a bemenet a tananyag naplóiból származik
            String input = "admin12";

            // Act
            ValidationResult result = validator.validate(input);

            // Assert – érvényes, és nincs elutasítási ok
            assertTrue(result.valid());
            assertNull(result.reason());
        }
    }

    /** 2. TDD-kör: hosszkorlát, határérték-elemzéssel. */
    @Nested
    @DisplayName("2. kör – hossz határértékei")
    class HosszHatarertekek {

        /**
         * Határérték-elemzés a 6-os minimum körül: 5 (alatta), 6 (pontosan), 7 (felette).
         * A 8 azért szerepel, mert az ELAVULT szabály 8-tól fogadott el – így látszik,
         * hogy a 6 és 7 hosszú nevek most már érvényesek.
         */
        @ParameterizedTest(name = "[{index}] \"{0}\" (hossz {1}) -> érvényes: {2}")
        @CsvSource({
                "abcd5,    5, false",
                "abcde6,   6, true",
                "abcdef7,  7, true",
                "abcdefg8, 8, true"
        })
        void lengthBoundaries(String input, int length, boolean expected) {
            // Arrange – önellenőrzés: a tesztadat tényleg a megadott hosszú
            assertEquals(length, input.length(), "hibás tesztadat");

            // Act
            boolean actual = validator.isValid(input);

            // Assert
            assertEquals(expected, actual);
        }

        /** A rövid név elutasításakor érthető okot kapunk. */
        @Test
        void tooShortHasReason() {
            ValidationResult result = validator.validate("abc12");
            assertFalse(result.valid());
            assertEquals("A felhasználónév legalább 6 karakter hosszú legyen.", result.reason());
        }
    }

    /** 3. TDD-kör: megengedett karakterek. */
    @Nested
    @DisplayName("3. kör – karakterkészlet")
    class Karakterkeszlet {

        /** Érvényes példák: csak betű, csak szám, vegyes, nagybetűs is (az új szabály engedi!). */
        @ParameterizedTest(name = "\"{0}\" érvényes")
        @ValueSource(strings = {"adminxy", "123456", "Admin12", "ADMIN12", "a1b2c3d4e5"})
        void validNames(String input) {
            assertTrue(validator.isValid(input));
        }

        /** Érvénytelen példák: szóköz, írásjel, aláhúzás, ékezetes betű, emoji. */
        @ParameterizedTest(name = "\"{0}\" érvénytelen")
        @ValueSource(strings = {"admin 12", "admin-12", "admin_12", "admin.12", "ádmin12", "admin12!", "admin😀1"})
        void invalidCharacters(String input) {
            // Act
            ValidationResult result = validator.validate(input);

            // Assert – érvénytelen, és az ok a karakterkészletre utal
            assertFalse(result.valid());
            assertTrue(result.reason().startsWith("A felhasználónév csak betűt vagy számot"),
                    () -> "váratlan ok: " + result.reason());
        }
    }

    /** 4. TDD-kör: hiányzó bemenet (robusztusság). */
    @Nested
    @DisplayName("4. kör – null és üres bemenet")
    class HianyzoBemenet {

        /** A {@code @NullAndEmptySource} egy {@code null} és egy üres ("") bemenetet ad. */
        @ParameterizedTest
        @NullAndEmptySource
        void nullOrEmptyIsInvalid(String input) {
            ValidationResult result = validator.validate(input);
            assertFalse(result.valid());
            assertEquals("A felhasználónév megadása kötelező.", result.reason());
        }
    }
}
