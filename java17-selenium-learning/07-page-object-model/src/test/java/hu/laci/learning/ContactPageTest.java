package hu.laci.learning;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import hu.laci.learning.pages.ContactPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContactPageTest {

    private WebDriver driver;
    private ContactPage contactPage;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        contactPage = new ContactPage(driver);
        contactPage.open();
    }

    @Test
    void shouldSubmitContactFormThroughPageObject() {
        // The test describes user intent without knowing the page's CSS/DOM.
        contactPage.enterName("Laci")
                .enterEmail("laci@example.com")
                .submit();

        assertEquals("Form submitted for Laci", contactPage.getStatusMessage());
    }

    @Test
    void shouldReportMissingNameThroughPageObject() {
        contactPage.enterEmail("laci@example.com").submit();

        assertEquals("Name is required", contactPage.getNameError());
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
