package com.orangehrm.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * 2. lépés: közös ősosztály. A böngésző életciklusa (indítás, bezárás) innen kerül ki
 * a tesztekből, így minden tesztosztály ugyanazt az alapot kapja, és nem kell
 * minden tesztben try/finally-t írni.
 * <p>
 * Minden teszt előtt új böngésző indul, minden teszt után bezárul. Ettől a tesztek
 * egymástól függetlenek: nincs megosztott süti, session vagy nyitott ablak.
 * (A végleges, bővített változat a 4. lépésben készül el.)
 */
public class BaseTest {
    protected WebDriver driver;

    @BeforeEach
    public void setupDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
