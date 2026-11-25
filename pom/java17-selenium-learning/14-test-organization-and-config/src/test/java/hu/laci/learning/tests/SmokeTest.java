package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.pages.ContactPage;
import hu.laci.learning.pages.IndexPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Gyors "füst" (smoke) tesztek: a legfontosabb funkciók alapvető működését ellenőrzik.
 * <p>
 * A {@code @Tag("smoke")} címkével jelöljük, így külön is futtathatók: {@code mvn test -Dgroups=smoke}.
 * Az osztály szintű címke az összes tesztmetódusra érvényes.
 */
@Tag("smoke")
class SmokeTest extends BaseTest {

    @Test
    void shouldOpenIndexPage() {
        IndexPage page = new IndexPage(driver).open();

        assertEquals("Learning Selenium", page.getTitle());
        assertEquals("Welcome to Selenium", page.getHeadingText());
    }

    @Test
    void shouldSubmitValidContactForm() {
        ContactPage page = new ContactPage(driver).open();

        page.enterName("Laci").enterEmail("laci@example.com").submit();

        assertEquals("Form submitted for Laci", page.getStatusMessage());
    }
}
