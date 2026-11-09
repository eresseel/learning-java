# 7. lépés – új adat bevitele és adatvezérelt (CSV-s) tesztelés

## Cél
Vizsgakövetelmények **6. és 7. pont**: új adat bevitele, valamint ismételt, sorozatos adatbevitel adatforrásból.

## Mi változott
- **Új:** `PimPage.java`: az Add Employee rész (`addEmployee`, `isEmployeeSavedSuccessfully`).
- **Módosult:** `DashboardPage.java`: új: `navigateToPim()`.
- **Új:** `Test06_07_DataDrivenAddEmployeeTest.java`.
- **Új:** `src/test/resources/testdata.csv`.

## Új fogalmak
- **PIM**: az OrangeHRM alkalmazottkezelő modulja.
- **`@ParameterizedTest` + `@CsvFileSource`**: egyetlen tesztkód, amely a CSV minden sorára újra lefut. A `numLinesToSkip = 1` kihagyja a fejlécsort.
  ```csv
  FirstName,LastName,EmpId
  John,Doe,9001
  Jane,Smith,9002
  Robert,Johnson,9003
  ```
  A három sor három külön futás, a jelentésben is három tesztként jelennek meg.
- **`EmpId` oszlop**: paraméterként átjön, de a teszt nem használja, mert az alkalmazás automatikusan generálja az Employee Id-t.
- **SPA-s navigáció tartalékkal**: a `navigateToPim()` a menüre kattint, és megvárja, hogy az URL tartalmazza a `/pim/` szöveget. Ha a kattintás „elveszik”, közvetlen URL-navigációra vált. Ez védelem a lassú demo szerver ellen.
- **Aszinkron mező**: az Employee Id a betöltés után töltődik ki. A kód megvárja (max. 5 mp), mert a késői újrarenderelés törölhetné a beírt nevet.
- **Kattintás JS-tartalékkal**: ha a natív `click()` nem éri el a gombot, JavaScript-tel váltjuk ki. Ez stabilizál, de elfedhet hibát, ezért tudatosan kell kezelni.
- **Siker-feltétel `or`-ral**: a mentés sikerét a toast **vagy** a „Personal Details” fejléc megjelenése igazolja (a toast gyorsan eltűnhet).

## Hogyan működik a teszt
Belépés → dashboard megvárása → PIM → `addEmployee(firstName, lastName)` → `assertTrue(isEmployeeSavedSuccessfully(), ...)`. A hibaüzenet tartalmazza az épp feldolgozott nevet.

## Futtatás

```bash
cd step07-adatvezerelt-felvitel
mvn clean test
```

(JDK 17+, Google Chrome és internet kell. A Maven Wrapper csak a 10. lépéstől van a mappában; addig `mvn` kell, vagy másold át a 10. lépésből az `mvnw`, `mvnw.cmd` és `.mvn/` elemeket.)

## Kész, ha
A jelentésben 3 teszt fut (John Doe, Jane Smith, Robert Johnson), mind zöld. Futtasd csak ezt: `mvn test -Dtest=Test06_07_DataDrivenAddEmployeeTest`

## A védéshez
„Egy tesztkód, három futás. Új adat hozzáadásához elég a CSV-t bővíteni, a kódhoz nem kell nyúlni.”

## Megjegyzés
A demo megosztott és nyilvános, ezért a felvett alkalmazottak mások számára is láthatók.
