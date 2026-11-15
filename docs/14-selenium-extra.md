# Selenium – `Keys`, `JavascriptExecutor`, `TimeoutException`

**Dokumentáció:** <https://www.selenium.dev/documentation/webdriver/>

---

## `Keys`

Billentyűkódokat tartalmazó enum. A `sendKeys()` metódusba átadva speciális billentyűleütéseket szimulál (Enter, Tab, Backspace, Ctrl+A stb.).

```java
import org.openqa.selenium.Keys;

WebElement input = driver.findElement(By.id("search"));

// Egy különleges billentyű elküldése:
input.sendKeys(Keys.ENTER);
input.sendKeys(Keys.TAB);
input.sendKeys(Keys.BACK_SPACE);
input.sendKeys(Keys.ESCAPE);

// Billentyűkombináció: Keys.chord() összefűzi a leütéseket.
// Ctrl+A (Windows/Linux) vagy Cmd+A (macOS) – az összes szöveg kijelölése:
Keys modifier = System.getProperty("os.name").toLowerCase().contains("mac")
        ? Keys.COMMAND
        : Keys.CONTROL;
input.sendKeys(Keys.chord(modifier, "a"));

// Kijelölés után törlés:
input.sendKeys(Keys.chord(modifier, "a"));
input.sendKeys(Keys.BACK_SPACE);
```

---

## `JavascriptExecutor`

A böngésző JavaScript motorját közvetlenül hívja. Akkor kell, ha a Selenium normál kattintása (`click()`) nem működik, mert az elem le van fedve, animáció fut, vagy a CSS `pointer-events: none` van rajta.

```java
import org.openqa.selenium.JavascriptExecutor;

JavascriptExecutor js = (JavascriptExecutor) driver;

// JavaScript kattintás – átugorja a normál Selenium eseménykezelést:
WebElement button = driver.findElement(By.id("save-btn"));
js.executeScript("arguments[0].click();", button);

// Görgetés, hogy az elem látható legyen a képernyőn:
WebElement link = driver.findElement(By.id("footer-link"));
js.executeScript("arguments[0].scrollIntoView(true);", link);

// Görgetés a lap aljára:
js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

// Egy mező értékének beállítása JavaScripttel (ha a sendKeys nem működik):
js.executeScript("arguments[0].value = arguments[1];", input, "új érték");
```

> **Mikor érdemes használni?** Csak végső esetként, ha a normál Selenium műveletek nem működnek. A JS kattintás átugorja a böngésző eseménykezelőjét, ezért rejthet el hibákat. Ha egy normál felhasználó sem tud kattintani az elemre, a JS kattintás sem a megfelelő megoldás.

---

## `TimeoutException`

A `WebDriverWait.until()` dobja ezt a kivételt, ha a feltétel a megadott időn belül nem teljesül. Elkapásával elkerülhető, hogy a teszt leálljon, ha egy opcionális elem nem jelenik meg.

```java
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

try {
    // Ha a toast üzenet 5 másodpercen belül megjelenik, a szövegét adjuk vissza.
    return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")))
               .getText();
} catch (TimeoutException e) {
    // Az elem nem jelent meg: visszatérünk egy alapértelmezett értékkel.
    return "";
}
```

> **Mikor érdemes elkapni?** Csak akkor, ha az elem megjelenése tényleg opcionális (pl. egy értesítési ablak, amely gyorsan el is tűnhet). Ha az elem megjelenése a teszt feltétele, ne kapd el a kivételt – hadd bukjon el a teszt.

