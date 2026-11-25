package hu.laci.learning;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Az összes UI teszt közös ősosztálya.
 * <p>
 * A böngésző indítása a {@link Config} beállításai szerint történik: ha a {@code headless}
 * tulajdonság igaz, a Chrome ablak nélkül indul (CI környezetben ez szükséges).
 * Implicit várakozás nincs, a várakozást a Page Objectek explicit módon végzik.
 */
public class BaseTest {

    // Protected láthatóságú, hogy a leszármazott tesztosztályok közvetlenül elérjék a böngészőpéldányt.
    protected WebDriver driver;

    // Minden egyes @Test metódus előtt lefut.
    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en-US");

        if (Config.isHeadless()) {
            // Új headless mód: ugyanazt a böngészőmotort használja, mint a normál Chrome.
            options.addArguments("--headless=new");
            // Fix ablakméret: headless módban nincs maximalizálás, így a layout minden futásnál azonos.
            options.addArguments("--window-size=1920,1080");
            // CI/Docker környezetben a Chrome sandbox és a kicsi /dev/shm gyakran gondot okoz.
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        } else {
            // Grafikus módban teljes képernyőre nyitjuk az ablakot.
            options.addArguments("--start-maximized");
        }

        driver = new ChromeDriver(options);
    }

    // Minden @Test után lefut, akkor is, ha a teszt elbukott.
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
