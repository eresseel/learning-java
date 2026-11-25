package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.pages.ContactPage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Részletesebb regressziós tesztek: a ritkább vagy hibás eseteket is lefedik, ezért lassabbak.
 * Futtatás: {@code mvn test -Dgroups=regression}, vagy a smoke nélkül: {@code mvn test -DexcludedGroups=smoke}.
 */
@Tag("regression")
class RegressionTest extends BaseTest {

    // @Nested: belső osztállyal logikailag csoportosítja a teszteket. A külső osztály @BeforeEach
    // metódusai (itt a BaseTest.setUp) a belső teszteknél is lefutnak.
    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("Missing name shows an error")
        void shouldShowErrorForMissingName() {
            ContactPage page = new ContactPage(driver).open();

            page.enterEmail("laci@example.com").submit();

            assertEquals("Name is required", page.getNameError());
        }
    }

    @Nested
    @DisplayName("Submission")
    class Submission {

        @Test
        @DisplayName("Name with accents is accepted")
        void shouldAcceptNameWithAccents() {
            ContactPage page = new ContactPage(driver).open();

            page.enterName("Árvíztűrő Tükörfúrógép").enterEmail("aa@example.com").submit();

            assertEquals("Form submitted for Árvíztűrő Tükörfúrógép", page.getStatusMessage());
        }

        // @Disabled: ismert hiba vagy még nem kész teszt átmeneti kikapcsolása. Mindig írd mellé az okát.
        @Test
        @Disabled("Example: this feature is not implemented yet")
        void shouldRejectInvalidEmail() {
            ContactPage page = new ContactPage(driver).open();

            page.enterName("Laci").enterEmail("not-an-email").submit();

            assertEquals("Email is invalid", page.getNameError());
        }
    }
}
