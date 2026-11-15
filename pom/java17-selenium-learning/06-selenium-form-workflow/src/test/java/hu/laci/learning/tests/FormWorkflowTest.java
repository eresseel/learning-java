package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.pages.ContactPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Űrlap-folyamat tesztelése: sikeres és hibás kitöltés.
 * Az oldal elemeit a {@link ContactPage} kezeli, a böngésző életciklusát a {@link BaseTest}.
 */
class FormWorkflowTest extends BaseTest {

    // Pozitív eset: érvényes adatokkal az űrlap elküldhető.
    @Test
    void shouldSubmitValidForm() {
        ContactPage page = new ContactPage(driver).open();

        // A láncolt hívások a felhasználó lépéseit írják le sorban.
        page.enterName("Laci")
                .enterEmail("laci@example.com")
                .submit();

        assertEquals("Form submitted for Laci", page.getStatusMessage());
    }

    // Negatív eset: név nélkül az űrlap hibaüzenetet ad.
    @Test
    void shouldShowValidationForMissingName() {
        ContactPage page = new ContactPage(driver).open();

        // A név mezőt szándékosan üresen hagyjuk.
        page.enterEmail("laci@example.com").submit();

        assertEquals("Name is required", page.getNameError());
    }
}
