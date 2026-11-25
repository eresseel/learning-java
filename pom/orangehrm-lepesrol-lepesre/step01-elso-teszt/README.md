# 1. lépés – az első Selenium teszt (minden egy helyen)

## Cél
Eljutni az első, működő, zöld teszthez a lehető legkevesebb kóddal. Még nincs közös ősosztály, nincs Page Object, csak egyetlen teszt, amely belép az OrangeHRM-be.

## Új fogalmak
- **Maven projekt** (`pom.xml`): a függőségek (Selenium, JUnit 5, WebDriverManager) és a Surefire teszt-pluginok verziói egy helyen. A későbbi lépések ugyanezt a pom-ot használják.
- **WebDriver**: a böngészőt vezérlő objektum (`ChromeDriver`).
- **WebDriverManager**: automatikusan letölti a Chrome-hoz illő chromedrivert.
- **Lokátor** (`By.name`, `By.xpath`): így azonosítunk egy elemet az oldalon.
- **Explicit várakozás** (`WebDriverWait` + `ExpectedConditions`): addig vár egy feltételre, ameddig szükséges (legfeljebb a megadott időkorlátig).
- **Assertion**: `Assertions.assertTrue(...)`: ettől lesz a kódból teszt, mert ha a feltétel nem igaz, a teszt elbukik.

## Fájlok
- `pom.xml`: új
- `src/test/java/com/orangehrm/test/tests/FirstLoginTest.java`: új

## Hogyan működik (a teszt lépései)
1. `WebDriverManager.chromedriver().setup()`: a driver beállítása.
2. `new ChromeDriver()`: elindul a Chrome.
3. `driver.get(...)`: megnyitja a login oldalt.
4. `wait.until(visibilityOfElementLocated(By.name("username")))`: megvárja a felhasználónév mezőt, majd beírja: `Admin`.
5. Beírja a jelszót (`admin123`), és megnyomja a submit gombot.
6. `assertTrue(wait.until(urlContains("dashboard")))`: az URL-ből ellenőrzi, hogy a belépés sikerült.
7. `finally { driver.quit(); }`: a böngésző akkor is bezárul, ha a teszt elbukik.

## Miért ilyen egyszerű, és mi a hátránya?
Ez a "működik" állapot. A hátránya: ha lesz tíz tesztünk, mindegyikbe be kell másolni a böngészőindítást és a try/finally blokkot, és minden lokátor a tesztben lesz. A következő két lépés ezt számolja fel.

## Futtatás

```bash
cd step01-elso-teszt
mvn clean test
```

(JDK 17+, Google Chrome és internet kell. A Maven Wrapper csak a 10. lépéstől van a mappában; addig `mvn` kell, vagy másold át a 10. lépésből az `mvnw`, `mvnw.cmd` és `.mvn/` elemeket.)

## Kész, ha
A Chrome megnyílik, belép, és a teszt zöld (JUnit: 1 sikeres teszt).

## A védéshez
„Az első lépésben egyetlen tesztet írtam, hogy bizonyítsam: a környezet (JDK, Maven, Chrome, driver) működik.”
