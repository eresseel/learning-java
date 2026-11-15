package hu.laci.learning;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Böngészőkezelő, amely SZÁLANKÉNT külön WebDriver példányt tárol.
 * <p>
 * Párhuzamos futtatásnál minden teszt saját szálon fut, és saját böngészőre van szüksége.
 * A WebDriver nem szálbiztos, ezért két szál nem használhatja ugyanazt a példányt.
 * A {@link ThreadLocal} olyan változó, amelynek minden szál számára külön értéke van:
 * az egyik szál nem látja a másik szál böngészőjét.
 */
public final class DriverFactory {

    // Szálanként külön tárolt WebDriver. A "static final" mező egy közös "kulcs", az érték azonban szálanként más.
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
        // Segédosztály, nem példányosítjuk.
    }

    /**
     * Létrehoz egy új böngészőt az aktuális szál számára, és eltárolja.
     *
     * @return az újonnan indított böngésző
     */
    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en-US");

        if (Config.isHeadless()) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        } else {
            options.addArguments("--start-maximized");
        }

        WebDriver driver = new ChromeDriver(options);
        DRIVER.set(driver);
        return driver;
    }

    /**
     * @return az aktuális szálhoz tartozó böngésző, vagy {@code null}, ha ehhez a szálhoz nincs böngésző
     */
    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    /**
     * Bezárja az aktuális szál böngészőjét, és törli a ThreadLocal bejegyzést.
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            // A remove() fontos: a szálak újrahasznosulnak (szálkészlet), így a régi érték nem maradhat bent.
            DRIVER.remove();
        }
    }
}
