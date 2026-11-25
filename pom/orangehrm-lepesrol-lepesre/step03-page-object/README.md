# 3. lépés – Page Object Model (LoginPage, DashboardPage)

## Cél
Szétválasztani a **„mit ellenőrzünk”** és a **„hogyan kattintunk”** rétegét. Ez a projekt legfontosabb tervezési döntése.

## Mi változott
- **Új:** `src/main/java/.../pages/LoginPage.java`: lokátorok és műveletek a login oldalhoz (`navigateTo`, `login`, `getErrorMessageText`).
- **Új:** `src/main/java/.../pages/DashboardPage.java`: egyelőre egyetlen metódus: `isDashboardDisplayed()`.
- **Módosult:** `Test02_LoginTest.java`: már nincs benne lokátor és `findElement`, csak page object hívás és assertion.

## Új fogalmak
- **Page Object**: egy osztály, amely egy oldal (vagy oldalrész) lokátorait és műveleteit tartalmazza. A lokátorok `private final By` mezők, kívülről nem látszanak.
- **A tesztben nincs lokátor.** Ha az oldal megváltozik, csak a page osztályt kell javítani.
- **Lokátor-stratégia**: a legstabilabb a `By.name(...)` (HTML form-szemantika, nem generált class). Ha nincs name, a label szövegéből indulunk, és onnan navigálunk XPath-tal.
- **A page object nem tartalmaz assertiont**, csak értéket ad vissza (`boolean`, `String`). Az ellenőrzés a teszt dolga.

## Összehasonlítás (előtte → utána)
```java
// 2. lépés (a tesztben)
driver.findElement(By.name("username")).sendKeys(username);
...
// 3. lépés (a tesztben)
loginPage.login("Admin", "admin123");
Assertions.assertTrue(dashboardPage.isDashboardDisplayed(), "...");
```

## Fájlok rövid magyarázata
- `LoginPage.login(...)`: `clear()` + `sendKeys()` mindkét mezőn, majd a submit gomb. A `clear()` azért kell, mert a böngésző előre kitölthet mezőt.
- `DashboardPage.isDashboardDisplayed()`: megvárja, hogy az URL tartalmazza a `dashboard` szót. Ezt **minden későbbi teszt** használja, hogy a belépés befejezése után navigáljon tovább (különben a login átirányítása felülírhatná a következő navigációt).

Tipp: nézd meg a változást a 2. és 3. lépés között: `diff -ru step02-* step03-*`

## Futtatás

```bash
cd step03-page-object
mvn clean test
```

(JDK 17+, Google Chrome és internet kell. A Maven Wrapper csak a 10. lépéstől van a mappában; addig `mvn` kell, vagy másold át a 10. lépésből az `mvnw`, `mvnw.cmd` és `.mvn/` elemeket.)

## Kész, ha
Ugyanaz a 3 zöld teszt, mint a 2. lépésben, csak a kód szebb és átláthatóbb.

## A védéshez
„A Page Object Model elválasztja a *mit* a *hogyan*-tól. Ha a felület változik, egy helyen javítok, és a tesztek olvashatók maradnak.”
