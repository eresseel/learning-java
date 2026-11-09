# 9. lépés – riport mentése CSV-be és adatkezelési nyilatkozat (minden követelmény kész)

## Cél
Vizsgakövetelmények **10. pont** (adatok lementése felületről) és **3. pont** (adatkezelési nyilatkozat). Ezzel mind a 11 követelmény le van fedve.

## Mi változott
- **Módosult:** `PimPage.java`: új: `isReportSectionAvailable`, `downloadFirstReport`, `getFirstReportName`, `exportReportDataToCsv`.
- **Új:** `PrivacyPolicyPage.java`.
- **Új:** `Test10_DownloadReportsTest.java`, `Test03_PrivacyPolicyTest.java`.

## Riport CSV-be (Test10)
Az OrangeHRM riportoldalán **nincs letöltés gomb**, ezért a megjelenített riporttáblát olvassa ki a kód, és Java `BufferedWriter`-rel írja CSV-be a `target/downloads/employee_report.csv` fájlba. Ez a követelmény egy értelmezése, nem böngészős letöltés.

A fejlesztés tanulságai (a Javadoc is rögzíti):
1. A riportot az Actions oszlop **3. (utolsó) gombja** nyitja meg. Sem a sorra, sem a Name cellára kattintás nem működik.
2. Megnyitás után csak egy szűrőűrlap látszik. A tényleges táblázat **a „Show” gomb megnyomása után** épül fel. Ennek kihagyásakor a CSV üres vagy hibás volt.
3. A kód ellenőrzi, hogy a kattintás után az URL tényleg megváltozott-e.

A teszt az `exists() && length() > 0` feltételt ellenőrzi.

**Ismert gyengeség:** a „nem üres fájl” gyenge feltétel, mert egy hibás futásban a CSV egy „No Data Found” sort is tartalmazhat, és mégis zöld lenne. Javítás: az írt sorok számának visszaadása és ellenőrzése. Emellett a Javadoc szűkített keresést ír, a kód viszont az egész oldalon keres (`driver.findElements`).

## Adatkezelési nyilatkozat (Test03)
1. A login oldal láblécében az `orangehrm.com` linkre kattint (görgetéssel, JS-tartalékkal), és az **új ablakban** nyílik meg.
2. `switchToNewWindow()`: megvárja, hogy **két ablak** legyen nyitva (`numberOfWindowsToBe(2)`), és csak utána vált. Ezzel elkerüli a race condition-t.
3. A süti-bannert rövid (4 mp) várakozással kezeli, mert nem mindig jelenik meg.
4. A Privacy Policy link `href`-jére navigál, és az URL-ben a `privacy-policy` szöveget ellenőrzi.

**Hibakereső maradvány:** a `PrivacyPolicyPage` 5 másodperces `Thread.sleep`-et tartalmaz (demonstrációs cél). A projekt explicit várakozást ígér, ezért ezt érdemes törölni.

## Futtatás

```bash
cd step09-riport-adatkezelesi-nyilatkozat
mvn clean test
```

(JDK 17+, Google Chrome és internet kell. A Maven Wrapper csak a 10. lépéstől van a mappában; addig `mvn` kell, vagy másold át a 10. lépésből az `mvnw`, `mvnw.cmd` és `.mvn/` elemeket.)

## Kész, ha
Mind a 14 tesztfuttatás zöld (Test01, 02×3, 03, 04, 05, 06_07×3, 08, 09, 10, 11).

## Megjegyzés a zip-hez képest
Az eredeti zip a lépésekben használtnál 5 metódust és 2 mezőt tartalmaz, amelyeket **sehol nem hív semmi** (`goToNextPageAndVerify`, `getEmployeeRows`, `AdminPage.clickAddUser`, `DashboardPage.navigateToAdmin`, `LoginPage.clickPrivacyPolicy`, `reportTableHeaderCells`, `reportTableRows`). Ezeket a lépések szándékosan kihagyják. Az eredeti zipben is törölhetők.

## A védéshez
„A riportnál tanultam a legtöbbet: az oldal csak a Show gomb után mutatja a táblát, ezért kellett a kód. Ezt a Javadoc és a docs/11 is rögzíti.”
