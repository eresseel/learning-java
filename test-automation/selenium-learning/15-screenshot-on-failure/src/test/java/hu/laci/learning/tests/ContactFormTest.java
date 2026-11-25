package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.pages.ContactPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Normál (sikeres) tesztek. Ha bármelyik elbukna, a BaseTest-en keresztül képernyőkép készülne.
 */
class ContactFormTest extends BaseTest {

    @Test
    void shouldSubmitValidForm() {
        ContactPage page = new ContactPage(driver).open();

        page.enterName("Laci").enterEmail("laci@example.com").submit();

        assertEquals("Form submitted for Laci", page.getStatusMessage());
    }
}
