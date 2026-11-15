package com.orangehrm.test.tests;

import com.orangehrm.test.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * 2. lépés: bejelentkezés, pozitív és két negatív eset. A böngészőt a BaseTest kezeli.
 * A lokátorok és a login művelet még a tesztben vannak; a 3. lépésben kerülnek át
 * egy Page Objectbe.
 */
public class Test02_LoginTest extends BaseTest {

    private WebDriverWait wait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Segédmetódus: a három teszt ugyanazt a login-lépéssort használja,
    // ezért nem másoljuk be háromszor.
    private void login(String username, String password) {
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        // clear(): ha a böngésző előre kitölti a mezőt, a beírt szöveg ne a meglévő mögé kerüljön.
        wait().until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).clear();
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    private String errorMessage() {
        // Két alternatív minta ("|" = vagy), mert a hibaüzenet DOM-ja verziónként kissé eltérhet.
        By alert = By.xpath("//p[contains(@class, 'oxd-alert-content-text')] | //div[contains(@class, 'oxd-alert')]");
        return wait().until(ExpectedConditions.visibilityOfElementLocated(alert)).getText();
    }

    @Test
    @DisplayName("Sikeres bejelentkezés helyes adatokkal")
    public void testSikeresBejelentkezes() {
        login("Admin", "admin123");
        Assertions.assertTrue(wait().until(ExpectedConditions.urlContains("dashboard")),
                "A bejelentkezés nem volt sikeres.");
    }

    @Test
    @DisplayName("Sikertelen bejelentkezés hibás jelszóval")
    public void testSikertelenBejelentkezesHibasJelszoval() {
        login("Admin", "hibasJelszo123");
        Assertions.assertTrue(errorMessage().contains("Invalid credentials"),
                "A várt 'Invalid credentials' hibaüzenet nem jelent meg.");
    }

    @Test
    @DisplayName("Sikertelen bejelentkezés hibás felhasználónévvel")
    public void testSikertelenBejelentkezesHibasFelhasznalonevvel() {
        login("RosszAdmin", "admin123");
        Assertions.assertTrue(errorMessage().contains("Invalid credentials"),
                "A várt 'Invalid credentials' hibaüzenet nem jelent meg.");
    }
}
