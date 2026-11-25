package hu.laci.learning.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object az egyszerű, helyi bejegyzéslista-oldalhoz.
 * <p>
 * A kapituláris feladatban az API kliens és ez az oldal párhuzamosan dolgozik:
 * az API ellenőrzi az adatok helyességét a szerveren, ez az osztály a megjelenítést a böngészőben.
 */
public class PostListPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By postItems = By.cssSelector("[data-testid='post-item']");
    private final By postTitle = By.cssSelector("[data-testid='post-title']");
    private final By filterInput = By.id("filter");
    private final By filteredItems = By.cssSelector("[data-testid='post-item']:not([hidden])");

    public PostListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public PostListPage open() {
        driver.get(java.nio.file.Path.of("src/test/resources/site/posts.html")
                .toAbsolutePath().toUri().toString());
        return this;
    }

    /** @return a listában megjelenő bejegyzések darabszáma */
    public int getPostCount() {
        return driver.findElements(postItems).size();
    }

    /** @return az összes bejegyzés-cím szövege */
    public List<String> getPostTitles() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(postTitle));
        return driver.findElements(postTitle).stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Beírja a szűrőbe a megadott szöveget.
     *
     * @param text a szűrőfeltétel
     * @return az oldal-objektum (láncolható)
     */
    public PostListPage filterBy(String text) {
        driver.findElement(filterInput).clear();
        driver.findElement(filterInput).sendKeys(text);
        return this;
    }

    /** @return a szűrő után látható bejegyzések darabszáma */
    public int getVisiblePostCount() {
        return driver.findElements(filteredItems).size();
    }
}
