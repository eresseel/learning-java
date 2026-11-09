package hu.laci.learning;

import java.nio.file.Path;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormWorkflowTest {

    private WebDriver driver;

    @Test
    void shouldSubmitValidForm() {
        openForm();

        driver.findElement(By.id("name")).sendKeys("Laci");
        driver.findElement(By.id("email")).sendKeys("laci@example.com");
        driver.findElement(By.id("submit")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        String message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("status"))).getText();

        assertEquals("Form submitted for Laci", message);
    }

    @Test
    void shouldShowValidationForMissingName() {
        openForm();

        driver.findElement(By.id("email")).sendKeys("laci@example.com");
        driver.findElement(By.id("submit")).click();

        String message = driver.findElement(By.id("name-error")).getText();
        assertEquals("Name is required", message);
        assertTrue(driver.findElement(By.id("name")).isDisplayed());
    }

    private void openForm() {
        driver = new ChromeDriver();
        driver.get(Path.of("src/test/resources/site/form.html").toAbsolutePath().toUri().toString());
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
