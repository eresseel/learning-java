# 5. lépés – adatok listázása (AdminPage)

## Cél
Vizsgakövetelmény **4. pont**: adatok listázása. Új page object: `AdminPage` (Admin / System Users), és az első táblázatos teszt.

## Mi változott
- **Új:** `AdminPage.java`: egyelőre csak a listázáshoz szükséges rész (`hasTableData`, `getFirstUsername`).
- **Új:** `Test04_ListEmployeesTest.java`.

## Új fogalmak
- **Közvetlen URL-navigáció**: a `@BeforeEach` belép, megvárja a dashboardot, majd `driver.get(USER_LIST_URL)`-lel a listaoldalra lép. Ez gyors és stabil.
- **Táblázat kiolvasása**: az adatsor `div[@class='oxd-table-card']`, a felhasználónév a sor 2. cellája (`div[@role='cell'][2]`).
- **Form-loader**: az OrangeHRM globális „töltés folyamatban” jelzője (`oxd-form-loader`). A `waitForLoaderToDisappear()` megvárja az eltűnését, de nem dob hibát, ha már eltűnt (a loader villanásszerű lehet).
- **Hosszabb várakozás**: a `hasTableData()` 30 másodpercet vár, mert a felhasználólista szerveroldali betöltése lassabb lehet.

## A teszt
1. Belépés, dashboard megvárása, ugrás a felhasználólistára.
2. `assertTrue(adminPage.hasTableData())`: van adatsor.
3. `getFirstUsername()`: kiolvassa az első sor felhasználónevét.
4. `assertNotNull` + `assertFalse(isEmpty())`: az érték nem üres.

## Futtatás

```bash
cd step05-listazas
mvn clean test
```

(JDK 17+, Google Chrome és internet kell. A Maven Wrapper csak a 10. lépéstől van a mappában; addig `mvn` kell, vagy másold át a 10. lépésből az `mvnw`, `mvnw.cmd` és `.mvn/` elemeket.)

## Kész, ha
A Test04 zöld, a konzolon látszik az első felhasználónév.

## A védéshez
„A lista tesztjénél nem konkrét nevet várok, mert a demo adatai változnak. Azt ellenőrzöm, hogy van adatsor, és az első érték értelmes.”
