package hu.laci.learning;

import hu.laci.learning.base.BaseUiTest;
import hu.laci.learning.data.ContactTestData;
import hu.laci.learning.pages.ContactPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContactFormTest extends BaseUiTest {

    @Test
    void shouldSubmitFirstValidContact() {
        ContactPage page = new ContactPage(driver).open();

        page.enterName(ContactTestData.VALID_NAME)
                .enterEmail(ContactTestData.VALID_EMAIL)
                .submit();

        assertEquals("Form submitted for " + ContactTestData.VALID_NAME,
                page.getStatusMessage());
    }

    @Test
    void shouldSubmitSecondValidContact() {
        ContactPage page = new ContactPage(driver).open();

        page.enterName(ContactTestData.SECOND_NAME)
                .enterEmail(ContactTestData.SECOND_EMAIL)
                .submit();

        assertEquals("Form submitted for " + ContactTestData.SECOND_NAME,
                page.getStatusMessage());
    }

    @Test
    void shouldRejectMissingName() {
        ContactPage page = new ContactPage(driver).open();

        page.enterEmail(ContactTestData.VALID_EMAIL).submit();

        assertEquals("Name is required", page.getNameError());
    }
}
