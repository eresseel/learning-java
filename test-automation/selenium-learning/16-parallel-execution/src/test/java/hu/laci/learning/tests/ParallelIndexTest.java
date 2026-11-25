package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.pages.IndexPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Kezdőoldal tesztek, szintén párhuzamosan a többi osztállyal.
 */
class ParallelIndexTest extends BaseTest {

    @Test
    void shouldShowTitle() {
        IndexPage page = new IndexPage(driver).open();

        assertEquals("Learning Selenium", page.getTitle());
    }

    @Test
    void shouldShowHeading() {
        IndexPage page = new IndexPage(driver).open();

        assertEquals("Welcome to Selenium", page.getHeadingText());
    }
}
