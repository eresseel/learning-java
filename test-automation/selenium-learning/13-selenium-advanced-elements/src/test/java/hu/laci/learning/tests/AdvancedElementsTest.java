package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.pages.AdvancedPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A "haladó" Selenium elemek tesztjei: legördülő lista, jelölőnégyzet, párbeszédablak,
 * iframe, késve betöltődő tartalom és új ablak.
 */
class AdvancedElementsTest extends BaseTest {

    private AdvancedPage page;

    // A BaseTest.setUp() (böngészőindítás) előbb lefut, ezért a driver itt már használható.
    @BeforeEach
    void openPage() {
        page = new AdvancedPage(driver).open();
    }

    @Test
    void shouldSelectCountryFromDropdown() {
        page.selectCountry("Germany");

        assertEquals("Germany", page.getSelectedCountry());
    }

    @Test
    void shouldToggleCheckbox() {
        // Kezdetben nincs bepipálva.
        assertFalse(page.isNewsletterChecked());

        page.toggleNewsletter();
        assertTrue(page.isNewsletterChecked());

        page.toggleNewsletter();
        assertFalse(page.isNewsletterChecked());
    }

    @Test
    void shouldReadAndAcceptAlert() {
        assertEquals("Hello from alert", page.clickAlertAndAccept());
    }

    @Test
    void shouldAcceptConfirmDialog() {
        page.clickConfirmAndAnswer(true);

        assertEquals("Confirmed", page.getConfirmResult());
    }

    @Test
    void shouldDismissConfirmDialog() {
        page.clickConfirmAndAnswer(false);

        assertEquals("Cancelled", page.getConfirmResult());
    }

    @Test
    void shouldReadTextInsideIframe() {
        assertEquals("Text inside frame", page.getTextInsideFrame());
    }

    @Test
    void shouldWaitForDynamicElement() {
        assertEquals("Loaded!", page.loadAndGetDynamicText());
    }

    @Test
    void shouldOpenLinkInNewWindow() {
        assertEquals("Contact Form", page.openLinkInNewWindowAndGetTitle());
    }
}
