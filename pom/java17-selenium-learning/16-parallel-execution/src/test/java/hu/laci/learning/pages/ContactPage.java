package hu.laci.learning.pages;

import hu.laci.learning.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.time.Duration;

/**
 * Page Object a helyi kapcsolatfelvételi űrlaphoz ({@code src/test/resources/site/form.html}).
 * <p>
 * A Page Object Model (POM) lényege: az oldal felépítését (lokátorok) és a felhasználói
 * műveleteket (kitöltés, küldés) egy osztály zárja magába. A teszt így csak azt írja le,
 * MIT csinál a felhasználó, azt nem, HOGYAN található meg egy elem. Ha az oldal HTML-je változik,
 * csak ezt az osztályt kell javítani, a teszteket nem.
 * <p>
 * Az osztály nem ad vissza nyers {@code WebElement}-eket, csak felhasználói műveleteket
 * és az oldalról kiolvasott értékeket.
 */
public class ContactPage {

    private final WebDriver driver;

    // Explicit várakozás: addig vár egy feltételre, de legfeljebb a megadott ideig.
    private final WebDriverWait wait;

    // Lokátorok egy helyen. Az id a legstabilabb lokátor, ha az elemnek van egyedi azonosítója.
    private final By nameInput = By.id("name");           // név beviteli mező
    private final By emailInput = By.id("email");         // e-mail beviteli mező
    private final By submitButton = By.id("submit");      // "Submit" gomb
    private final By statusMessage = By.id("status");     // sikeres küldés üzenete
    private final By nameError = By.id("name-error");     // hiányzó név hibaüzenete

    /**
     * @param driver a teszt által használt böngésző (a BaseTest hozza létre)
     */
    public ContactPage(WebDriver driver) {
        this.driver = driver;
        // Az időkorlát a Config-ból jön (alapérték 5 mp, felülírható: -Dwait.seconds=10),
        // így lassabb környezetben (pl. CI) a teszt kódjának módosítása nélkül megnövelhető.
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Config.explicitWaitSeconds()));
    }

    /**
     * Megnyitja a helyi HTML fájlt a böngészőben.
     *
     * @return maga az oldal-objektum, így a hívások láncolhatók (fluent interface)
     */
    public ContactPage open() {
        // A relatív fájlútvonalat abszolút file:// URL-lé alakítjuk, mert a böngésző csak így nyitja meg.
        // A Maven a projekt gyökeréből futtat, ezért működik a relatív "src/test/resources/..." útvonal.
        driver.get(Path.of("src/test/resources/site/form.html")
                .toAbsolutePath().toUri().toString());
        return this;
    }

    /**
     * Beírja a nevet a név mezőbe.
     *
     * @param name a beírandó név
     * @return az oldal-objektum (láncolható)
     */
    public ContactPage enterName(String name) {
        // clear(): ha a mezőben már van szöveg, a beírt érték ne annak a végére kerüljön.
        driver.findElement(nameInput).clear();
        driver.findElement(nameInput).sendKeys(name);
        return this;
    }

    /**
     * Beírja az e-mail címet az e-mail mezőbe.
     *
     * @param email a beírandó e-mail cím
     * @return az oldal-objektum (láncolható)
     */
    public ContactPage enterEmail(String email) {
        driver.findElement(emailInput).clear();
        driver.findElement(emailInput).sendKeys(email);
        return this;
    }

    /**
     * Rákattint a "Submit" gombra.
     *
     * @return az oldal-objektum (láncolható)
     */
    public ContactPage submit() {
        driver.findElement(submitButton).click();
        return this;
    }

    /**
     * Visszaadja a sikeres küldés után megjelenő állapotüzenetet.
     *
     * @return az üzenet szövege, pl. "Form submitted for Laci"
     */
    public String getStatusMessage() {
        // A JavaScript az üzenetet a küldés után tölti be, ezért megvárjuk, amíg látható lesz,
        // és nem használunk fix időtartamú alvást (Thread.sleep).
        return wait.until(ExpectedConditions.visibilityOfElementLocated(statusMessage)).getText();
    }

    /**
     * Visszaadja a hiányzó névre megjelenő hibaüzenetet.
     *
     * @return a hibaüzenet szövege, pl. "Name is required"
     */
    public String getNameError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(nameError)).getText();
    }
}
