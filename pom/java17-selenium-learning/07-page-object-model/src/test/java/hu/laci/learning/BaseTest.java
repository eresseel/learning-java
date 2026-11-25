package hu.laci.learning;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Az összes UI (böngészős) teszt közös ősosztálya.
 * <p>
 * A böngésző indítását és bezárását egy helyen végzi, így a tesztosztályokban nem kell
 * ezt megismételni. A tesztek ebből az osztályból származnak ({@code extends BaseTest}),
 * és a {@link #driver} mezőn keresztül érik el a böngészőt.
 * <p>
 * Minden teszt előtt új böngésző indul, minden teszt után bezárul, ezért a tesztek
 * egymástól függetlenek (nincs megosztott süti, munkamenet vagy nyitott ablak).
 * <p>
 * Implicit várakozás szándékosan nincs beállítva: a várakozást a Page Object osztályok
 * explicit módon ({@code WebDriverWait}) végzik, így a két mechanizmus nem keveredik.
 */
public class BaseTest {

    // Protected láthatóságú, hogy a leszármazott tesztosztályok közvetlenül elérjék a böngészőpéldányt.
    protected WebDriver driver;

    // A @BeforeEach miatt minden egyes @Test metódus előtt automatikusan lefut.
    // A JUnit az ősosztály @BeforeEach metódusát a leszármazott saját @BeforeEach metódusa ELŐTT hívja meg.
    @BeforeEach
    public void setUp() {
        // A böngésző indítási kapcsolóit és konfigurációját tároló objektum.
        ChromeOptions options = new ChromeOptions();

        // Teljes képernyős ablak: így a reszponzív oldalak nem rejtenek el elemeket.
        options.addArguments("--start-maximized");

        // Normál böngésző User-Agent szimulálása, hogy a szerver ne ismerje fel az automatizált böngészőt
        // (headless módban a Chrome alapból "HeadlessChrome"-ként azonosítja magát).
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");

        // Angol nyelvű felület: a tesztek az angol szövegekre (pl. "Name is required") számítanak,
        // így a gép nyelvi beállítása nem befolyásolja az eredményt.
        options.addArguments("--lang=en-US");

        // Elindítja a Chrome böngészőt; a Selenium Manager automatikusan letölti
        // és beállítja a hozzá illő chromedrivert, külön driver-kezelés nem kell.
        driver = new ChromeDriver(options);
    }

    // A @AfterEach miatt minden egyes @Test metódus után lefut, akkor is, ha a teszt elbukott.
    @AfterEach
    public void tearDown() {
        // A null-ellenőrzés azért kell, mert ha a setUp() hibával megállt, a driver még null lehet.
        if (driver != null) {
            // A quit() bezárja az összes böngészőablakot és leállítja a háttérben futó WebDriver folyamatot.
            // (A close() csak az aktuális ablakot zárná be, a driver folyamat futva maradna.)
            driver.quit();
        }
    }
}
