package com.orangehrm.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object a bejelentkezés utáni Dashboard (kezdőképernyő) oldalhoz.
 * Innen érhető el a felső menün keresztül a PIM és az Admin modul, valamint a kijelentkezés.
 */
public class DashboardPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By userDropdown = By.className("oxd-userdropdown-tab");
    private final By logoutLink = By.xpath("//a[contains(@href, 'logout')]");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Kijelentkezteti a felhasználót a jobb felső felhasználói menün keresztül.
     * Két lépés: előbb megnyitjuk a felhasználói dropdown menüt, majd a benne
     * megjelenő Logout linkre kattintunk.
     */
    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(userDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

    /**
     * Megvárja és ellenőrzi, hogy az aktuális URL a Dashboard oldalra mutat-e.
     * Ez a bejelentkezés utáni "landing page" – ha ide eljutunk, a login sikeres volt.
     *
     * @return {@code true}, ha a bejelentkezés után a Dashboard töltött be
     */
    public boolean isDashboardDisplayed() {
        return wait.until(ExpectedConditions.urlContains("dashboard"));
    }
}
