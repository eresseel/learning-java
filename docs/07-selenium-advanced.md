[← Előző: 06-selenium-waits.md](06-selenium-waits.md)

---

# Selenium – haladó elemek

**Dokumentáció:** <https://www.selenium.dev/documentation/webdriver/elements/>

---

## `Select`

A `<select>` legördülő listákat kezelő segédosztály. Opciót szöveg, érték (`value` attribútum) vagy index alapján választ ki.

```java
import org.openqa.selenium.support.ui.Select;

Select select = new Select(driver.findElement(By.id("country")));

select.selectByVisibleText("Germany");   // a látható szöveg alapján
select.selectByValue("de");              // a value attribútum alapján
select.selectByIndex(2);                 // lista pozíció alapján (0-tól)

// Az aktuálisan kiválasztott elem:
String selected = select.getFirstSelectedOption().getText();
```

---

## `iframe` kezelése (`switchTo().frame`)

Az `iframe` belsejébe lépés és visszalépés a fő oldalra. A keretbe lépés előtt az abban lévő elemek a fő oldalról nem érhetők el.

```java
// Átváltás az iframe-re (WebElement alapján):
driver.switchTo().frame(driver.findElement(By.id("info-frame")));

// Most már az iframe belsejében keresünk:
String text = driver.findElement(By.id("frame-text")).getText();

// Visszalépés a fő oldalra (mindig meg kell hívni):
driver.switchTo().defaultContent();
```

---

## Új böngészőablak kezelése

Minden nyitott ablaknak van egy egyedi `handle` azonosítója. Az ablakváltás és bezárás sorrendje számít.

```java
// Az aktuális ablak azonosítója:
String originalWindow = driver.getWindowHandle();

// Kattintás egy új ablakot megnyitó linkre:
driver.findElement(By.id("new-window-link")).click();

// Megvárjuk, amíg a második ablak megjelenik:
new WebDriverWait(driver, Duration.ofSeconds(5))
        .until(ExpectedConditions.numberOfWindowsToBe(2));

// Átváltás az új ablakra:
for (String handle : driver.getWindowHandles()) {
    if (!handle.equals(originalWindow)) {
        driver.switchTo().window(handle);
        break;
    }
}

String newTitle = driver.getTitle();

// Az aktuális ablak (az új) bezárása:
driver.close();

// Visszaváltás az eredeti ablakra (a close() után kötelező):
driver.switchTo().window(originalWindow);
```

---

[ Következő: 08-restassured.md → ](08-restassured.md)
