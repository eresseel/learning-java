package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.pages.LocatorPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Elemkeresés (lokátorok) és explicit várakozás gyakorlása.
 * A lokátorok a {@link LocatorPage} osztályban vannak, a teszt csak a lépéseket és az ellenőrzéseket tartalmazza.
 */
class LocatorTest extends BaseTest {

    @Test
    void shouldFindElementsAndWaitForResult() {
        LocatorPage page = new LocatorPage(driver).open();

        // Ellenőrzés id lokátorral megtalált elemre.
        assertEquals("Welcome to Selenium", page.getHeadingText());

        // Ellenőrzés CSS selectorral megtalált elemre.
        assertTrue(page.isWelcomeMessageDisplayed());

        // Kattintás az XPath-szal megtalált gombra.
        page.clickShowResult();

        // Az eredmény a kattintás után jelenik meg, a Page Object megvárja.
        assertEquals("Button clicked", page.getResultText());
    }
}
