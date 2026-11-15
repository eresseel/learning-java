# 13 – Haladó Selenium elemek

**Nehézség:** ★★★☆☆

## Cél
Azokkal az oldalelemekkel való munka, amelyekhez a sima `click()` és `sendKeys()` nem elég.

## Feladat
Az `advanced.html` oldalon kezeld a következőket Page Objecten keresztül:
- legördülő lista (`Select`);
- jelölőnégyzet (`isSelected`);
- JavaScript `alert` és `confirm` ablak;
- beágyazott oldal (`iframe`);
- késve megjelenő elem (explicit várakozás);
- új böngészőablak (ablak-azonosítók, `switchTo().window`).

## Mit tanulunk?
- `Select` segédosztály;
- `driver.switchTo().alert()` és `ExpectedConditions.alertIsPresent()`;
- `driver.switchTo().frame(...)` és `defaultContent()`, a `finally` szerepe a visszalépésben;
- `getWindowHandle()` / `getWindowHandles()`, `close()` és `quit()` különbsége;
- miért kell a dinamikus tartalomhoz explicit várakozás.

## Futtatás
```bash
cd 13-selenium-advanced-elements
mvn clean test
```
Chrome szükséges, internet nem (helyi HTML fájlok).

## Továbbfejlesztési feladat
1. Add hozzá az oldalhoz egy `prompt()` ablakot, és írd meg a kezelését (`alert.sendKeys(...)`).
2. Válts a `Select`-ben érték (`selectByValue`) és index (`selectByIndex`) szerinti kiválasztásra.
3. Adj hozzá egy egérmozgásra (hover) megjelenő menüt, és kezeld a `org.openqa.selenium.interactions.Actions` osztállyal.
