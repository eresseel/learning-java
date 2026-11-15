# Selenium WebDriver – alapok

**Dokumentáció:** <https://www.selenium.dev/documentation/webdriver/>

---

## `WebDriver`

A böngészőt vezérlő interfész. A konkrét implementáció a böngészőtől függ (pl. `ChromeDriver`). A tesztek mindig az interfészen keresztül dolgoznak, így a konkrét böngésző cserélhető.

```java
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

WebDriver driver = new ChromeDriver();
driver.get("https://example.com");      // oldal betöltése
String title = driver.getTitle();       // böngészőfül címe
String url = driver.getCurrentUrl();    // aktuális URL
driver.navigate().back();               // vissza gomb
driver.quit();                          // minden ablak bezárása + driver leállítása
```

---

## `ChromeDriver` / `ChromeOptions`

A `ChromeDriver` a Chrome böngészőt vezérli. A `ChromeOptions` az indítási kapcsolókat és a böngésző beállításait tárolja.

```java
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

ChromeOptions options = new ChromeOptions();
options.addArguments("--start-maximized");
options.addArguments("--headless=new");        // ablak nélküli mód
options.addArguments("--no-sandbox");
options.addArguments("--disable-dev-shm-usage");
options.addArguments("--window-size=1920,1080");
options.addArguments("--lang=en-US");

WebDriver driver = new ChromeDriver(options);
// A Selenium Manager automatikusan letölti a megfelelő chromedrivert.
```

---

## `By`

Lokátor-stratégiákat leíró osztály. Az elemek megkeresésének módját adja meg.

```java
import org.openqa.selenium.By;

By.id("username")                               // id attribútum alapján (legstabilabb)
By.name("password")                             // name attribútum alapján
By.cssSelector(".oxd-input")                    // CSS selector
By.cssSelector("[data-testid='submit-btn']")    // data-* attribútum (teszteléshez ajánlott)
By.xpath("//button[normalize-space()='Login']") // XPath kifejezés
By.className("error-message")                   // CSS osztálynév
By.linkText("Forgot Password?")                 // link teljes szövege alapján
```

---

## `WebElement`

Egyetlen oldal-elemet jelöl. A `findElement()` visszatérési típusa.

```java
import org.openqa.selenium.WebElement;

WebElement input = driver.findElement(By.id("username"));
input.clear();                      // beviteli mező kiürítése
input.sendKeys("Admin");            // szöveg beírása
input.click();                      // kattintás
input.getText();                    // az elem látható szövege
input.getAttribute("placeholder"); // egy HTML attribútum értéke
input.isDisplayed();                // látható-e az elem
input.isEnabled();                  // aktív-e (nem disabled)
input.isSelected();                 // checkbox/radio esetén be van-e jelölve
```

---

## `Alert`

A böngésző JavaScript párbeszédablakát jelöli (`alert`, `confirm`, `prompt`).

```java
import org.openqa.selenium.Alert;

// Átváltás a párbeszédablakra:
Alert alert = driver.switchTo().alert();

alert.getText();     // az ablak szövege
alert.accept();      // OK / megerősítés
alert.dismiss();     // Cancel / elutasítás
alert.sendKeys("x"); // szöveg beírása (csak prompt esetén)
```

---

## `OutputType` / `TakesScreenshot`

Képernyőképet készít a böngészőről. A `ChromeDriver` implementálja a `TakesScreenshot` interfészt.

```java
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;

TakesScreenshot screenshotDriver = (TakesScreenshot) driver;

// FILE: ideiglenes fájlba írja a képet.
File png = screenshotDriver.getScreenshotAs(OutputType.FILE);

// BASE64: Base64-kódolt szöveget ad vissza (pl. HTML riportba ágyazáshoz).
String base64 = screenshotDriver.getScreenshotAs(OutputType.BASE64);
```

