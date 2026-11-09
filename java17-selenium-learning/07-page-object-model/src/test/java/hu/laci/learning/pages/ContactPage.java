package hu.laci.learning.pages;

import java.nio.file.Path;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object encapsulating locators and actions for the contact form.
 * Assertions intentionally stay in test classes.
 */
public class ContactPage {

    private final WebDriver driver;

    // Locators belong to the page class so tests do not depend on DOM details.
    private final By nameInput = By.id("name");
    private final By emailInput = By.id("email");
    private final By submitButton = By.id("submit");
    private final By statusMessage = By.id("status");
    private final By nameError = By.id("name-error");

    public ContactPage(WebDriver driver) {
        this.driver = driver;
    }

    public ContactPage open() {
        String url = Path.of("src/test/resources/site/form.html")
                .toAbsolutePath()
                .toUri()
                .toString();
        driver.get(url);
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
        return driver.findElement(statusMessage).getText();
    }

    public String getNameError() {
        return driver.findElement(nameError).getText();
    }
}
