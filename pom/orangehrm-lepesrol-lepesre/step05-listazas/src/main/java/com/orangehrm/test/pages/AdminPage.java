package com.orangehrm.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object az OrangeHRM Admin / System Users moduljához.
 * Felelős új felhasználó létrehozásáért, a felhasználólista lekérdezéséért és a keresésért.
 */
public class AdminPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // A form-loader az OrangeHRM globális "töltés folyamatban" indikátora – amíg látszik,
    // az űrlap elemei még nem feltétlenül interakcióképesek, ezért sok metódus explicit
    // megvárja az eltűnését.
    private final By formLoader = By.className("oxd-form-loader");
    private final By tableCard = By.xpath("//div[contains(@class, 'oxd-table-card')]");

    // A második cella (index [2]) tartalmazza a felhasználónevet az első táblázatsorban.
    private final By firstUsernameLocator = By.xpath("//div[@class='oxd-table-body']/div[1]//div[@role='cell'][2]");

    public AdminPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * A form-loader eltűnésére vár, de nem dob hibát, ha ez időtúllépéssel jár –
     * néhány oldalon a loader villanásszerűen jelenik meg, és előfordulhat, hogy
     * mire a wait elindulna, már el is tűnt, ami félrevezető TimeoutException-t okozna.
     */
    private void waitForLoaderToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
        } catch (Exception ignored) {
        }
    }

    /**
     * Visszaadja a felhasználólista első sorában szereplő felhasználónevet.
     *
     * @return az első sor felhasználóneve
     */
    public String getFirstUsername() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(tableCard));
        WebElement firstUserElement = wait.until(ExpectedConditions.visibilityOfElementLocated(firstUsernameLocator));
        return firstUserElement.getText();
    }

    /**
     * Ellenőrzi, hogy a felhasználólista táblázata megjelent-e (van-e legalább egy adatsor).
     *
     * @return {@code true}, ha a táblázat látható
     */
    public boolean hasTableData() {
        waitForLoaderToDisappear();
        // Ennél a metódusnál hosszabb (30 mp) várakozást használunk az alapértelmezett 15 mp
        // helyett, mert a felhasználólista betöltése (szerveroldali lapozás/szűrés) néha
        // lassabb, mint a többi, egyszerűbb oldalművelet.
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            return longWait.until(ExpectedConditions.visibilityOfElementLocated(tableCard)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("DIAGNOSZTIKA URL: " + driver.getCurrentUrl() + " | cím: " + driver.getTitle());
            throw e;
        }
    }
}
