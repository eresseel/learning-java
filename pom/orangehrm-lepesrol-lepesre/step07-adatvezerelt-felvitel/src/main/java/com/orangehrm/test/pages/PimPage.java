package com.orangehrm.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object az OrangeHRM PIM (alkalmazottak) moduljához.
 * Felelős az alkalmazottak felvételéért, listázásáért, lapozásáért, szerkesztéséért,
 * törléséért, valamint a riportok megnyitásáért és CSV-be exportálásáért.
 */
public class PimPage {
    private static final String ADD_EMPLOYEE_URL =
            "https://opensource-demo.orangehrmlive.com/web/index.php/pim/addEmployee";
    private final WebDriver driver;
    private final WebDriverWait wait;

    // ---- Add Employee ----
    private final By addEmployeeTab = By.xpath("//a[text()='Add Employee']");
    private final By firstNameInput = By.name("firstName");
    private final By lastNameInput = By.name("lastName");

    // Az Employee Id mező (ugyanaz a szerkezet, mint az Admin oldal Username mezőjénél)
    private final By employeeIdInput = By.xpath("//label[normalize-space()='Employee Id']/parent::div/following-sibling::div//input");
    private final By saveButton = By.xpath("//button[@type='submit']");
    private final By formLoader = By.className("oxd-form-loader");
    private final By personalDetailsHeader = By.xpath("//h6[text()='Personal Details']");
    private final By successToast = By.xpath("//div[contains(@class, 'oxd-toast-content--success')]");

    public PimPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Felvesz egy új alkalmazottat az Add Employee űrlapon: kitölti a kereszt- és
     * vezetéknevet, megvárja az automatikusan generált Employee Id betöltését, majd ment.
     *
     * @param firstName az alkalmazott keresztneve
     * @param lastName  az alkalmazott vezetékneve
     */
    public void addEmployee(String firstName, String lastName) {
        // Fülre kattintunk, de nem bízunk vakon benne: ha az SPA routing "elveszíti" a kattintást
        // (lassú render, korábbi művelet még folyamatban), közvetlen URL-navigációval pótoljuk.
        wait.until(ExpectedConditions.elementToBeClickable(addEmployeeTab)).click();
        try {
            // Ellenőrizzük, hogy a kattintás tényleg átvitt az Add Employee oldalra
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.urlContains("addEmployee"));
        } catch (Exception e) {
            // Ha a kattintás elveszett (pl. lassú oldal, korábbi művelet miatt), közvetlenül navigálunk
            System.out.println("Figyelmeztetés: az Add Employee fülre kattintás nem navigált át, közvetlen URL-lel próbálkozunk.");
            driver.get(ADD_EMPLOYEE_URL);
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));

        // Előbb megvárjuk, hogy az űrlap teljesen betöltsön (az Employee Id aszinkron töltődik ki),
        // és csak utána gépelünk, hogy a késői újrarenderelés ne törölje a beírt adatokat
        waitForEmployeeIdToLoad();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));

        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput)).sendKeys(firstName);
        driver.findElement(lastNameInput).sendKeys(lastName);

        // A submit gombra néha JS-eseménykezelő van kötve, amit a natív .click() nem mindig vált ki
        // megbízhatóan (pl. overlay miatt) – ezért JS-kattintással próbálkozunk tartalékként.
        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        try {
            saveBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
        }
    }

    /**
     * Legfeljebb 5 másodpercig vár, hogy az Employee Id mező értéket kapjon.
     * Ha a mező nem található vagy üres marad, nem dob hibát, a mentés ettől még megpróbálódik.
     */
    private void waitForEmployeeIdToLoad() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> {
                String value = d.findElement(employeeIdInput).getAttribute("value");
                return value != null && !value.isEmpty();
            });
        } catch (Exception e) {
            // Nem kritikus hiba: az Employee Id kitöltése "csak" visszaigazolás, a mentés
            // enélkül is sikerülhet, ezért itt csak naplózunk, nem dobunk tovább kivételt.
            System.out.println("Figyelmeztetés: az Employee Id nem töltődött ki 5 mp alatt (vagy a lokátor nem illik az oldalra).");
        }
    }

    /**
     * Ellenőrzi, hogy a mentés sikeres volt-e: megjelent-e a siker toast, vagy
     * (alkalmazott felvételekor) a mentés után betöltő Personal Details fejléc.
     *
     * @return {@code true}, ha a mentés sikeressége igazolható
     */
    public boolean isEmployeeSavedSuccessfully() {
        try {
            // Két lehetséges "sikerjel" közül bármelyik elfogadható: a toast gyorsan eltűnhet,
            // ilyenkor a Personal Details fejléc megjelenése is elég bizonyíték a sikeres mentésre.
            return wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(successToast),
                    ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader)
            ));
        } catch (Exception e) {
            logSaveFailureDiagnostics();
            return false;
        }
    }

    /**
     * Csak hibakereséshez: kiírja, mit látott a Selenium, amikor a mentés nem igazolódott vissza.
     */
    private void logSaveFailureDiagnostics() {
        try {
            System.out.println("---- DIAGNOSZTIKA (a mentés nem igazolódott vissza) ----");
            System.out.println("URL: " + driver.getCurrentUrl());
            for (WebElement err : driver.findElements(By.xpath("//span[contains(@class, 'oxd-input-field-error-message')]"))) {
                System.out.println("Validációs hiba: " + err.getText());
            }
            for (WebElement toast : driver.findElements(By.xpath("//div[contains(@class, 'oxd-toast')]"))) {
                System.out.println("Toast: " + toast.getText());
            }
            System.out.println("---------------------------------------------------------");
        } catch (Exception ignored) {
        }
    }
}
