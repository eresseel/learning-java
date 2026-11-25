package com.orangehrm.test.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * 1. lépés: a legegyszerűbb lehetséges Selenium + JUnit 5 teszt.
 * Minden egy helyen van: böngésző indítása, lokátorok, műveletek, assertion, takarítás.
 * Az osztály neve "Test"-re végződik, mert a pom.xml-ben a Surefire csak a
 * "Test"-re végződő nevű osztályokat futtatja (lásd a pom.xml-t).
 */
public class FirstLoginTest {

    @Test
    public void adminCanLogIn() {
        // A WebDriverManager letölti és beállítja a Chrome-hoz illő chromedrivert,
        // így nem kell kézzel driver-t keresni és az elérési útját megadni.
        WebDriverManager.chromedriver().setup();

        // Ez indítja el a valódi Chrome böngészőt.
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

            // Az oldal egy SPA, a mezők kicsit később jelennek meg, mint ahogy a driver.get()
            // visszatér. Ezért NEM findElement-tel kezdünk, hanem explicit várakozással:
            // addig vár (max. 10 mp-ig), amíg a mező látható nem lesz.
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")))
                    .sendKeys("Admin");

            driver.findElement(By.name("password")).sendKeys("admin123");
            driver.findElement(By.xpath("//button[@type='submit']")).click();

            // Assertion: a sikeres belépés után az URL tartalmazza a "dashboard" szót.
            // Az urlContains egy ExpectedCondition, a wait.until pedig boolean-t ad vissza.
            Assertions.assertTrue(wait.until(ExpectedConditions.urlContains("dashboard")),
                    "A bejelentkezés után nem a dashboard töltött be!");
        } finally {
            // A finally miatt a böngésző akkor is bezárul, ha az assertion elbukik.
            driver.quit();
        }
    }
}
