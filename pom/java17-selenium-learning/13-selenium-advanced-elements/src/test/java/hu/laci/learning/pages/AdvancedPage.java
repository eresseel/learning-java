package hu.laci.learning.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.time.Duration;

/**
 * Page Object a haladó elemeket tartalmazó helyi oldalhoz ({@code site/advanced.html}).
 * <p>
 * Olyan elemeket kezel, amelyekhez a sima {@code click()} és {@code sendKeys()} nem elég:
 * legördülő lista, jelölőnégyzet, JavaScript párbeszédablak, iframe, késve betöltődő elem
 * és új böngészőablak.
 */
public class AdvancedPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By countrySelect = By.id("country");
    private final By newsletterCheckbox = By.id("newsletter");
    private final By alertButton = By.id("alert-btn");
    private final By confirmButton = By.id("confirm-btn");
    private final By confirmResult = By.id("confirm-result");
    private final By infoFrame = By.id("info-frame");
    private final By frameText = By.id("frame-text");
    private final By loadButton = By.id("load-btn");
    private final By dynamicText = By.id("dynamic-text");
    private final By formLink = By.id("form-link");

    public AdvancedPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    /** Megnyitja a helyi HTML fájlt. */
    public AdvancedPage open() {
        driver.get(Path.of("src/test/resources/site/advanced.html")
                .toAbsolutePath().toUri().toString());
        return this;
    }

    // ---------- Legördülő lista ----------

    /** Kiválaszt egy országot a legördülő listából a látható szöveg alapján. */
    public AdvancedPage selectCountry(String visibleText) {
        // A Select segédosztály a <select> elemekhez való: opció kiválasztása szöveg, érték vagy index alapján.
        new Select(driver.findElement(countrySelect)).selectByVisibleText(visibleText);
        return this;
    }

    /** @return a legördülő listában jelenleg kiválasztott opció szövege */
    public String getSelectedCountry() {
        return new Select(driver.findElement(countrySelect)).getFirstSelectedOption().getText();
    }

    // ---------- Jelölőnégyzet ----------

    /** Átbillenti a jelölőnégyzetet (be, ha ki volt kapcsolva, és fordítva). */
    public AdvancedPage toggleNewsletter() {
        driver.findElement(newsletterCheckbox).click();
        return this;
    }

    /** @return {@code true}, ha a jelölőnégyzet be van pipálva */
    public boolean isNewsletterChecked() {
        // Az isSelected() jelölőnégyzetnél és rádiógombnál adja meg a kijelölt állapotot.
        return driver.findElement(newsletterCheckbox).isSelected();
    }

    // ---------- JavaScript alert és confirm ----------

    /** Rákattint az alert gombra, megvárja az ablakot, elfogadja, és visszaadja a szövegét. */
    public String clickAlertAndAccept() {
        driver.findElement(alertButton).click();
        // Megvárjuk, amíg a böngésző párbeszédablak megjelenik.
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String text = alert.getText();
        // Az accept() az "OK" gombot nyomja meg, a dismiss() a "Cancel"-t.
        alert.accept();
        return text;
    }

    /** Rákattint a confirm gombra, és elfogadja vagy elutasítja a kérdést. */
    public AdvancedPage clickConfirmAndAnswer(boolean accept) {
        driver.findElement(confirmButton).click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        if (accept) {
            alert.accept();
        } else {
            alert.dismiss();
        }
        return this;
    }

    /** @return a confirm döntése után az oldalra írt eredmény ("Confirmed" vagy "Cancelled") */
    public String getConfirmResult() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmResult)).getText();
    }

    // ---------- iframe ----------

    /** Átvált a beágyazott keretre, kiolvassa a benne lévő szöveget, majd visszavált a fő oldalra. */
    public String getTextInsideFrame() {
        // A keret elemei a fő oldalról nem érhetők el, előbb be kell lépni a keretbe.
        driver.switchTo().frame(driver.findElement(infoFrame));
        try {
            return driver.findElement(frameText).getText();
        } finally {
            // A finally miatt a hiba esetén is visszatérünk a fő oldalra, így a további lépések nem sérülnek.
            driver.switchTo().defaultContent();
        }
    }

    // ---------- Késve betöltődő elem ----------

    /** Rákattint a Load gombra, és megvárja, amíg a késve létrejövő szöveg megjelenik. */
    public String loadAndGetDynamicText() {
        driver.findElement(loadButton).click();
        // Az elem a kattintás után 1 másodperccel jön létre, ezért explicit várakozás kell.
        return wait.until(ExpectedConditions.visibilityOfElementLocated(dynamicText)).getText();
    }

    // ---------- Új ablak ----------

    /**
     * Megnyitja a linket új ablakban, átvált rá, kiolvassa a címét, bezárja, majd visszavált az eredeti ablakra.
     *
     * @return az új ablakban megnyílt oldal címe
     */
    public String openLinkInNewWindowAndGetTitle() {
        // A böngészőablakokat "handle" azonosítóval különböztetjük meg.
        String originalWindow = driver.getWindowHandle();

        driver.findElement(formLink).click();
        // Megvárjuk, amíg tényleg két ablak van nyitva.
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        // Megkeressük azt az ablakot, amelyik nem az eredeti, és átváltunk rá.
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        String title = driver.getTitle();

        // A close() csak az aktuális (új) ablakot zárja be, a quit() mindent.
        driver.close();
        driver.switchTo().window(originalWindow);
        return title;
    }
}
