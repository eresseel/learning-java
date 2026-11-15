package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.pages.IndexPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Az első böngészős teszt: megnyit egy helyi oldalt, és ellenőrzi a címét és a főcímét.
 * <p>
 * A böngésző indítását és bezárását a {@link BaseTest} végzi, az oldal elemeit
 * az {@link IndexPage} Page Object kezeli, ezért a teszt csak az ellenőrzéseket tartalmazza.
 */
class FirstSeleniumTest extends BaseTest {

    @Test
    void shouldOpenLocalPageAndReadHeading() {
        // A `driver` mezőt a BaseTest.setUp() hozta létre a teszt előtt.
        IndexPage indexPage = new IndexPage(driver).open();

        // A böngészőfül címe egyezzen az elvárttal.
        assertEquals("Learning Selenium", indexPage.getTitle());

        // A főcím szövege és láthatósága.
        assertEquals("Welcome to Selenium", indexPage.getHeadingText());
        assertTrue(indexPage.isHeadingDisplayed());
    }
}
