# 08 – Újrafelhasználható POM és tesztadatok

## Cél
Tedd karbantarthatóbbá a POM-megoldást: a közös beállítás kerüljön egy alap tesztosztályba, az adatok legyenek elkülönítve, és használj explicit várakozást.

## Feladat
- A `BaseUiTest` indítsa és zárja be a böngészőt.
- A `ContactPage` használjon `WebDriverWait`-et a státusz megjelenésének megvárására.
- A `ContactTestData` tartalmazzon néhány tesztadatot.
- A tesztek legyenek olvashatók és egymástól függetlenek.

## Mit tanulunk?
- közös tesztinfrastruktúra `@BeforeEach` / `@AfterEach` segítségével;
- POM és tesztadatok szétválasztása;
- explicit várakozás;
- több tesztadat ugyanazon viselkedés ellenőrzéséhez.

## Futtatás
```bash
mvn clean test
```

## Továbbfejlesztési feladat
1. Adj hozzá negatív e-mail-validációt.
2. Adj hozzá `headless` konfigurációt rendszerparaméterrel, például `-Dheadless=true`.
3. Készíts CI pipeline-ban Surefire riportok publikálására alkalmas lépést.
