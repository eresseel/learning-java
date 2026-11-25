package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.pages.ContactPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Űrlap tesztek. A junit-platform.properties miatt ezek a tesztek párhuzamosan futnak,
 * mindegyik a saját böngészőablakában.
 */
class ParallelFormTest extends BaseTest {

    @Test
    void shouldSubmitFirstContact() {
        ContactPage page = new ContactPage(driver).open();

        page.enterName("Laci").enterEmail("laci@example.com").submit();

        assertEquals("Form submitted for Laci", page.getStatusMessage());
    }

    @Test
    void shouldSubmitSecondContact() {
        ContactPage page = new ContactPage(driver).open();

        page.enterName("Alex").enterEmail("alex@example.com").submit();

        assertEquals("Form submitted for Alex", page.getStatusMessage());
    }

    @Test
    void shouldRejectMissingName() {
        ContactPage page = new ContactPage(driver).open();

        page.enterEmail("laci@example.com").submit();

        assertEquals("Name is required", page.getNameError());
    }
}
