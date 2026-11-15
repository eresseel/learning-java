[← Előző: 12-parallel-threadlocal.md](12-parallel-threadlocal.md)

---

# WebDriverManager

**Dokumentáció:** <https://bonigarcia.dev/webdrivermanager/>  
**GitHub:** <https://github.com/bonigarcia/webdrivermanager>

> **Megjegyzés:** A Selenium 4.6-tól beépített **Selenium Manager** automatikusan elvégzi ugyanezt a feladatot, ezért új projektekben a WebDriverManager már nem szükséges. A leírás azért szerepel itt, mert a beküldött projektben még ez volt használatban.

---

## Mire való?

A böngészővezérlőhöz (`chromedriver`, `geckodriver` stb.) illő bináris letöltése és beállítása. A böngésző verziójával automatikusan egyező drivert tölt le és teszi elérhetővé a Selenium számára.

---

## `WebDriverManager.chromedriver().setup()`

Letölti és beállítja a Chrome böngészőhöz illő `chromedriver` binárist. A `setUp()` előtt kell meghívni, még a `new ChromeDriver()` előtt.

```java
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

// @BeforeEach-ben, a ChromeDriver() példányosítása előtt:
WebDriverManager.chromedriver().setup();
WebDriver driver = new ChromeDriver();
```

### Összehasonlítás a Selenium Manager megközelítéssel

```java
// Régi módszer (WebDriverManager):
WebDriverManager.chromedriver().setup();
driver = new ChromeDriver(options);

// Új módszer (Selenium 4.6+, Selenium Manager – külön setup() nem kell):
driver = new ChromeDriver(options);
```

A két megközelítés eredménye azonos: mindkét esetben a Selenium megtalálja a megfelelő `chromedriver`-t. A különbség csak az, hogy a Selenium Manager esetén nem kell függőséget felvenni és a `setup()` hívást megírni.

---

[ Következő: 14-selenium-extra.md → ](14-selenium-extra.md)
