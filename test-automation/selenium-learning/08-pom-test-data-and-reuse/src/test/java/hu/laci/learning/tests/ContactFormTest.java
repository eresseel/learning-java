package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.data.ContactTestData;
import hu.laci.learning.pages.ContactPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Az űrlap tesztjei újrafelhasználható Page Objecttel és külön tesztadatokkal.
 * A tesztek egymástól függetlenek: mindegyik új böngészőt kap a {@link BaseTest}-től.
 */
class ContactFormTest extends BaseTest {

    // Első érvényes adatsor.
    @Test
    void shouldSubmitFirstValidContact() {
        ContactPage page = new ContactPage(driver).open();

        page.enterName(ContactTestData.VALID_NAME)
                .enterEmail(ContactTestData.VALID_EMAIL)
                .submit();

        assertEquals("Form submitted for " + ContactTestData.VALID_NAME,
                page.getStatusMessage());
    }

    // Második érvényes adatsor: ugyanaz a viselkedés más adatokkal.
    @Test
    void shouldSubmitSecondValidContact() {
        ContactPage page = new ContactPage(driver).open();

        page.enterName(ContactTestData.SECOND_NAME)
                .enterEmail(ContactTestData.SECOND_EMAIL)
                .submit();

        assertEquals("Form submitted for " + ContactTestData.SECOND_NAME,
                page.getStatusMessage());
    }

    // Negatív eset: név nélkül hibaüzenetet várunk.
    @Test
    void shouldRejectMissingName() {
        ContactPage page = new ContactPage(driver).open();

        page.enterEmail(ContactTestData.VALID_EMAIL).submit();

        assertEquals("Name is required", page.getNameError());
    }
}
