[← Előző: 05-selenium-core.md](05-selenium-core.md)

---

# Selenium – várakozások

**Dokumentáció:** <https://www.selenium.dev/documentation/webdriver/waits/>

---

## `WebDriverWait` és `ExpectedConditions`

Explicit várakozás: addig vár egy feltételre, de legfeljebb a megadott ideig. Ez gyorsabb és megbízhatóbb, mint a `Thread.sleep()`, és nem keveredik az implicit várakozással.

```java
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

// Vár, amíg az elem látható lesz, majd visszaadja:
WebElement element = wait.until(
    ExpectedConditions.visibilityOfElementLocated(By.id("status"))
);

// Vár, amíg az elem kattintható lesz:
wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));

// Vár, amíg az URL tartalmazza a megadott szöveget:
wait.until(ExpectedConditions.urlContains("dashboard"));

// Vár, amíg pontosan 2 ablak van nyitva:
wait.until(ExpectedConditions.numberOfWindowsToBe(2));

// Vár, amíg egy JavaScript alert megjelenik:
Alert alert = wait.until(ExpectedConditions.alertIsPresent());
```

---

## Miért ne használjunk implicit várakozást?

Az implicit várakozás (`driver.manage().timeouts().implicitlyWait(...)`) globálisan, minden `findElement()` hívásra érvényes, és keveredik az explicit várakozással. Ez nehezen kiszámítható várakozási időket okoz – különösen, ha azt ellenőrzöd, hogy egy elem **nincs** jelen, mert ilyenkor az összes időt kivárja. Ehelyett mindig explicit várakozást érdemes használni a Page Object-ekben.

---

[ Következő: 07-selenium-advanced.md → ](07-selenium-advanced.md)
