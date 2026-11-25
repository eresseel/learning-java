package hu.laci.learning.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.nio.file.Path;

/**
 * Page Object a helyi kezdőoldalhoz ({@code src/test/resources/site/index.html}).
 * <p>
 * Az oldal lokátorait és a hozzá tartozó lekérdezéseket tartalmazza, így a teszt
 * nem tud az oldal HTML-szerkezetéről.
 */
public class IndexPage {

    private final WebDriver driver;

    // A főcím lokátora: az id a legstabilabb választás, ha egyedi azonosító van.
    private final By heading = By.id("page-title");

    /**
     * @param driver a teszt által használt böngésző (a BaseTest hozza létre)
     */
    public IndexPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Megnyitja a helyi HTML fájlt a böngészőben.
     *
     * @return az oldal-objektum (láncolható)
     */
    public IndexPage open() {
        // A relatív fájlútvonalat file:// URL-lé alakítjuk, mert a böngésző csak így tudja megnyitni.
        driver.get(Path.of("src/test/resources/site/index.html")
                .toAbsolutePath().toUri().toString());
        return this;
    }

    /**
     * @return a böngészőfül címe (a HTML {@code <title>} eleme)
     */
    public String getTitle() {
        return driver.getTitle();
    }

    /**
     * @return a főcím (h1) szövege
     */
    public String getHeadingText() {
        return driver.findElement(heading).getText();
    }

    /**
     * @return {@code true}, ha a főcím látható az oldalon
     */
    public boolean isHeadingDisplayed() {
        WebElement element = driver.findElement(heading);
        // Az isDisplayed() azt ellenőrzi, hogy az elem ténylegesen látszik-e (nem rejtett).
        return element.isDisplayed();
    }
}
