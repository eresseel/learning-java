package hu.laci.learning;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FirstSeleniumTest {

    private WebDriver driver;

    @Test
    void shouldOpenLocalPageAndReadHeading() {
        // Selenium Manager attempts to resolve a compatible ChromeDriver.
        driver = new ChromeDriver();

        // Convert the test HTML file into a file:// URL.
        String pageUrl = Path.of("src/test/resources/site/index.html")
                .toAbsolutePath()
                .toUri()
                .toString();

        driver.get(pageUrl);

        assertEquals("Learning Selenium", driver.getTitle());

        WebElement heading = driver.findElement(By.id("page-title"));
        assertEquals("Welcome to Selenium", heading.getText());
        assertTrue(heading.isDisplayed());
    }

    @AfterEach
    void closeBrowser() {
        // Quit closes the browser and ends the WebDriver session.
        if (driver != null) {
            driver.quit();
        }
    }
}
