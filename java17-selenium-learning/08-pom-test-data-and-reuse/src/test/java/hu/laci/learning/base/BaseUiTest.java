package hu.laci.learning.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Shared lifecycle for UI tests.
 * Keep browser setup in one place to avoid duplicated test infrastructure.
 */
public abstract class BaseUiTest {

    protected WebDriver driver;

    @BeforeEach
    protected void startBrowser() {
        driver = new ChromeDriver();
    }

    @AfterEach
    protected void stopBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
