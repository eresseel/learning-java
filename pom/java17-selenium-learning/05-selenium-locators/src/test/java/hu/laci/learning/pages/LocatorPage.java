package hu.laci.learning.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.time.Duration;

/**
 * Page Object a lokátor-gyakorló oldalhoz ({@code src/test/resources/site/index.html}).
 * <p>
 * Az egyes lokátor-típusok (id, CSS selector, XPath) itt vannak egy helyen,
 * a teszt pedig csak a viselkedést írja le.
 */
public class LocatorPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Az ID általában a legtisztább lokátor, ha van stabil azonosító.
    private final By heading = By.id("page-title");

    // A CSS selector jól használható osztályokra és data-* attribútumokra.
    // A data-testid kifejezetten teszteléshez szánt attribútum, ezért stabil.
    private final By welcomeMessage = By.cssSelector("[data-testid='welcome-message']");

    // Az XPath szöveg vagy DOM-kapcsolatok alapján is megtalál elemet.
    // A normalize-space() eltávolítja a felesleges szóközöket és sortöréseket a gomb szövege körül.
    private final By showResultButton = By.xpath("//button[normalize-space()='Show result']");

    // A kattintás után megjelenő eredmény szövege.
    private final By result = By.id("result");

    /**
     * @param driver a teszt által használt böngésző (a BaseTest hozza létre)
     */
    public LocatorPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    /**
     * Megnyitja a helyi HTML fájlt a böngészőben.
     *
     * @return az oldal-objektum (láncolható)
     */
    public LocatorPage open() {
        driver.get(Path.of("src/test/resources/site/index.html")
                .toAbsolutePath().toUri().toString());
        return this;
    }

    /**
     * @return a főcím (h1) szövege
     */
    public String getHeadingText() {
        return driver.findElement(heading).getText();
    }

    /**
     * @return {@code true}, ha az üdvözlő szöveg látható
     */
    public boolean isWelcomeMessageDisplayed() {
        return driver.findElement(welcomeMessage).isDisplayed();
    }

    /**
     * Rákattint a "Show result" gombra.
     *
     * @return az oldal-objektum (láncolható)
     */
    public LocatorPage clickShowResult() {
        driver.findElement(showResultButton).click();
        return this;
    }

    /**
     * Megvárja és visszaadja a kattintás után megjelenő eredmény szövegét.
     *
     * @return az eredmény szövege
     */
    public String getResultText() {
        // A várt állapotra várunk, nem fix időtartamot alszunk: ez gyorsabb és megbízhatóbb.
        return wait.until(ExpectedConditions.visibilityOfElementLocated(result)).getText();
    }
}
