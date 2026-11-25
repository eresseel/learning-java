package com.mycompany.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class BaseTest {

    // Protected láthatóságú, hogy a leszármazott tesztosztályok közvetlenül elérjék a böngészőpéldányt
    protected WebDriver driver;

    // Minden egyes @Test metódus előtt automatikusan lefut
    @BeforeEach
    public void setUp() {
        // A böngésző indítási kapcsolóit és konfigurációját tároló objektum
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // Normál böngésző User-Agent szimulálása a headless megkülönböztetés elkerülésére
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
        options.addArguments("--lang=en-US");

        // Elindítja a Chrome/Chromium böngészőt; a Selenium Manager automatikusan letölti és beállítja a megfelelő drivert
        driver = new ChromeDriver(options);

        // Alapértelmezett implicit várakozás beállítása, hogy a Selenium röviden várjon az elemek megjelenésére hiba előtt
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // Minden egyes @Test metódus után automatikusan lefut, akkor is, ha a teszt elbukik
    @AfterEach
    public void tearDown() {
        // Bezárja az összes böngészőablakot, és tisztán leállítja a háttérben futó WebDriver folyamatot
        if (driver != null) {
            driver.quit();
        }
    }
}
