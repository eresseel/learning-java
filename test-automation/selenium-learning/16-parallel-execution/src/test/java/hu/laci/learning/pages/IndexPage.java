package hu.laci.learning.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;

/**
 * Page Object a helyi kezdőoldalhoz ({@code site/index.html}).
 */
public class IndexPage {

    private final WebDriver driver;
    private final By heading = By.id("page-title");

    public IndexPage(WebDriver driver) {
        this.driver = driver;
    }

    public IndexPage open() {
        driver.get(Path.of("src/test/resources/site/index.html")
                .toAbsolutePath().toUri().toString());
        return this;
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getHeadingText() {
        return driver.findElement(heading).getText();
    }
}
