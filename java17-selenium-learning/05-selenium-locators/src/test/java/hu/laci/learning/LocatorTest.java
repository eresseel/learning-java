package hu.laci.learning;

import java.nio.file.Path;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocatorTest {

    private WebDriver driver;

    @Test
    void shouldFindElementsAndWaitForResult() {
        driver = new ChromeDriver();
        driver.get(Path.of("src/test/resources/site/index.html").toAbsolutePath().toUri().toString());

        // ID is usually the clearest locator when a stable ID is available.
        assertEquals("Welcome to Selenium",
                driver.findElement(By.id("page-title")).getText());

        // CSS selectors are useful for classes and data attributes.
        WebElement message = driver.findElement(By.cssSelector("[data-testid='welcome-message']"));
        assertTrue(message.isDisplayed());

        // XPath can locate elements by text or relationships in the DOM.
        WebElement button = driver.findElement(By.xpath("//button[normalize-space()='Show result']"));
        button.click();

        // Wait for the expected state instead of sleeping for a fixed duration.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement result = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("result")));
        assertEquals("Button clicked", result.getText());
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
