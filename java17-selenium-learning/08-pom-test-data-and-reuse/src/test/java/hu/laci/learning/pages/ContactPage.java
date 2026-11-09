package hu.laci.learning.pages;

import java.nio.file.Path;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the local contact form.
 * It exposes user-facing actions and observations, not raw WebElements.
 */
public class ContactPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By nameInput = By.id("name");
    private final By emailInput = By.id("email");
    private final By submitButton = By.id("submit");
    private final By statusMessage = By.id("status");
    private final By nameError = By.id("name-error");

    public ContactPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public ContactPage open() {
        driver.get(Path.of("src/test/resources/site/form.html")
                .toAbsolutePath().toUri().toString());
        return this;
    }

    public ContactPage enterName(String name) {
        driver.findElement(nameInput).clear();
        driver.findElement(nameInput).sendKeys(name);
        return this;
    }

    public ContactPage enterEmail(String email) {
        driver.findElement(emailInput).clear();
        driver.findElement(emailInput).sendKeys(email);
        return this;
    }

    public ContactPage submit() {
        driver.findElement(submitButton).click();
        return this;
    }

    public String getStatusMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(statusMessage)).getText();
    }

    public String getNameError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(nameError)).getText();
    }
}
