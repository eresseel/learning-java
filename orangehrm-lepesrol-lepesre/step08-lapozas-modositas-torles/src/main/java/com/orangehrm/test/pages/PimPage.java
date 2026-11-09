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

    // ---- Employee List ----
    private final By employeeListTab = By.xpath("//a[text()='Employee List']");
    private final By tableRows = By.xpath("//div[@class='oxd-table-card']");
    private final By paginationNextButton = By.xpath("//button[contains(@class, 'oxd-pagination-page-item--previous-next')]/i[contains(@class, 'bi-chevron-right')]");
    private final By editButton = By.xpath("(//button[i[contains(@class, 'bi-pencil-fill')]])[1]");
    private final By deleteButton = By.xpath("(//button[i[contains(@class, 'bi-trash')]])[1]");
    private final By confirmDeleteButton = By.xpath("//button[contains(., 'Yes, Delete')]");
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

    /**
     * A PIM modulon belül az Employee List fülre navigál, és megvárja a táblázat betöltését.
     */
    public void navigateToEmployeeList() {
        wait.until(ExpectedConditions.elementToBeClickable(employeeListTab)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
        wait.until(ExpectedConditions.visibilityOfElementLocated(tableRows));
    }

    /**
     * Végigjárja a teljes, többoldalas alkalmazott listát az első oldaltól az utolsóig:
     * minden oldalon megvárja a táblázat betöltését, majd a Next gombra kattint, amíg
     * az elérhető és aktív (nem a legutolsó oldalon áll).
     *
     * @return a bejárt oldalak száma (legalább 1, ha legalább egy oldal létezik)
     */
    public int traverseAllPagesAndCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(tableRows));
        int pageCount = 1;

        while (true) {
            List<WebElement> nextButtons = driver.findElements(paginationNextButton);
            if (nextButtons.isEmpty()) {
                // Egyoldalas lista: nincs lapozó gomb, tehát nincs mit bejárni tovább
                break;
            }

            WebElement nextBtn = nextButtons.get(0);
            // A gomb DOM-ban jelen lehet akkor is, ha inaktív (utolsó oldalon állunk) –
            // ezért a "disabled" attribútumot és a szülő elem CSS osztályát is ellenőrizzük,
            // mert az OrangeHRM UI ezt kétféleképpen is jelezheti.
            String disabledAttr = nextBtn.getAttribute("disabled");
            String parentClass = nextBtn.findElement(By.xpath("./..")).getAttribute("class");
            boolean isDisabled = disabledAttr != null
                    || (parentClass != null && parentClass.contains("disabled"));
            if (isDisabled) {
                break;
            }

            try {
                wait.until(ExpectedConditions.elementToBeClickable(paginationNextButton)).click();
            } catch (Exception e) {
                // Ha a kattintás mégsem sikerül (pl. időközben mégis inaktívvá vált), leállunk
                // ahelyett, hogy a tesztet hibával elszakítanánk – a bejárt oldalszám így is hiteles.
                break;
            }

            wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
            wait.until(ExpectedConditions.visibilityOfElementLocated(tableRows));
            pageCount++;

            // Biztonsági korlát, hogy egy hibás lokátor (pl. a Next gomb mindig "aktívnak" tűnik)
            // miatt a ciklus ne fusson végtelenítve.
            if (pageCount > 100) {
                break;
            }
        }

        return pageCount;
    }

    /**
     * Megnyitja az alkalmazott lista első sorának szerkesztését, kicseréli a keresztnevet,
     * és menti a módosítást.
     *
     * @param newFirstName az új keresztnév
     * @return a módosítás előtti (eredeti) keresztnév
     */
    public String editFirstEmployee(String newFirstName) {
        WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(editButton));
        editBtn.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));

        // Megvárjuk, hogy a mező tényleg megkapja a szerkesztésre nyíló alkalmazott nevét,
        // különben "" -t olvasnánk ki eredeti névként (race condition az aszinkron betöltéssel).
        wait.until(driver -> !nameInput.getAttribute("value").isEmpty());
        String oldFirstName = nameInput.getAttribute("value");

        // Mezőtartalom cseréje: teljes kijelölés + törlés + új szöveg beírása.
        // Macen Cmd+A, más rendszeren (pl. Linux CI) Ctrl+A jelöli ki a teljes szöveget
        Keys selectAllModifier = System.getProperty("os.name").toLowerCase().contains("mac") ? Keys.COMMAND : Keys.CONTROL;
        nameInput.sendKeys(Keys.chord(selectAllModifier, "a"));
        nameInput.sendKeys(Keys.BACK_SPACE);
        nameInput.sendKeys(newFirstName);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));

        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        try {
            saveBtn.click();
        } catch (Exception e) {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", saveBtn);
        }

        return oldFirstName;
    }

    /**
     * Törli az alkalmazott lista első sorát, a megerősítő ablakban a Yes, Delete gombbal.
     *
     * @return a törölt alkalmazott neve (amennyire a táblázat celláiból kiolvasható)
     */
    public String deleteFirstEmployee() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));

        WebElement firstRow = wait.until(ExpectedConditions.visibilityOfElementLocated(tableRows));

        // A törlés gomb megnyomása előtt kiolvassuk a nevet, mert utána a sor eltűnik a DOM-ból
        List<WebElement> cells = firstRow.findElements(By.xpath(".//div[@role='cell']"));
        String deletedEmployeeName = "";

        if (cells.size() >= 4) {
            deletedEmployeeName = cells.get(2).getText() + " " + cells.get(3).getText();
        } else if (cells.size() >= 3) {
            deletedEmployeeName = cells.get(2).getText();
        }

        WebElement delBtn = wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        delBtn.click();

        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton));
        confirmBtn.click();

        return deletedEmployeeName.trim();
    }

    /**
     * Ellenőrzi, hogy a törlés után megjelenik-e a sikeres törlést jelző toast üzenet.
     *
     * @return {@code true}, ha a sikeres törlést jelző toast látható
     */
    public boolean isDeleteSuccessful() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successToast)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
