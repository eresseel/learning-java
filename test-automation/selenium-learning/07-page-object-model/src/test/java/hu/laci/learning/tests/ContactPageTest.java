package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.pages.ContactPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A Page Object Model használatát bemutató teszt.
 * A teszt a felhasználó szándékát írja le, az oldal DOM-szerkezetét nem ismeri.
 */
class ContactPageTest extends BaseTest {

    private ContactPage contactPage;

    // A szülőosztály @BeforeEach metódusa (BaseTest.setUp, itt indul a böngésző) ELŐBB lefut,
    // csak utána ez a metódus, ezért a `driver` itt már használható.
    @BeforeEach
    void openPage() {
        contactPage = new ContactPage(driver).open();
    }

    @Test
    void shouldSubmitContactFormThroughPageObject() {
        // A teszt a felhasználó szándékát írja le, az oldal CSS/DOM szerkezetét nem ismeri.
        contactPage.enterName("Laci")
                .enterEmail("laci@example.com")
                .submit();

        assertEquals("Form submitted for Laci", contactPage.getStatusMessage());
    }

    @Test
    void shouldReportMissingNameThroughPageObject() {
        // Név nélkül küldjük el az űrlapot, hibaüzenetet várunk.
        contactPage.enterEmail("laci@example.com").submit();

        assertEquals("Name is required", contactPage.getNameError());
    }
}
