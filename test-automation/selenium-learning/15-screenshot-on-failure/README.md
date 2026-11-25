# 15 – Képernyőkép készítése elbukott tesztnél (JUnit extension)

**Nehézség:** ★★★★☆

## Cél
Hibakeresés megkönnyítése: ha egy UI teszt elbukik, automatikusan képernyőkép és az oldal HTML-forrása mentődjön. CI környezetben, ahol a böngészőt nem látjuk, ez gyakran az egyetlen nyom.

## Feladat
- Írj JUnit kiegészítőt (`ScreenshotOnFailureExtension`), amely elbukott tesztnél képernyőképet készít.
- Kösd rá a `BaseTest`-re, hogy minden leszármazott teszt megkapja.
- Próbáld ki a szándékosan elbukó demóteszttel, és nézd meg a `target/screenshots` mappát.

## Mit tanulunk?
- a JUnit extension modell (`AfterTestExecutionCallback`, `ExtensionContext`);
- az életciklus sorrendje: a tesztmetódus után, de az `@AfterEach` előtt fut a kiegészítő, ezért a böngésző még él. (A `TestWatcher` túl későn értesítene: a böngésző addigra be van zárva.)
- `TakesScreenshot`, `getScreenshotAs`, `getPageSource`;
- az `@ExtendWith` öröklődése;
- feltételes futtatás: `@EnabledIfSystemProperty`.

## Futtatás
```bash
cd 15-screenshot-on-failure
mvn clean test                          # normál futás, minden zöld
mvn clean test -DdemoFailure=true       # a demóteszt elbukik, képernyőkép készül
mvn clean test -DdemoFailure=true -Dheadless=true
```
Hiba után: `target/screenshots/DemoFailureTest_..._<időbélyeg>.png` és `.html`.

## Továbbfejlesztési feladat
1. Mentsd el a böngésző konzol-naplóját is (`driver.manage().logs().get("browser")`).
2. Készíts képernyőképet sikeres tesztnél is, ha egy rendszer-tulajdonság ezt kéri.
3. Csatold a képernyőképet CI-ban artifactként (lásd a gyökér `.github/workflows/ci.yml`).
