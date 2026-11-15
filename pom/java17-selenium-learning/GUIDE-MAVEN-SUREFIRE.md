# Maven és Surefire – gyors referencia

## Gyakori parancsok

```bash
mvn clean test
mvn -Dtest=CalculatorTest test
mvn -Dtest=ContactFormTest#shouldRejectMissingName test
mvn clean verify
```

- `clean`: eltávolítja az előző build `target/` könyvtárát.
- `test`: lefordítja a kódot, majd lefuttatja a teszteket.
- `verify`: a Maven életciklus későbbi fázisaiig fut, és további ellenőrzések beillesztésére alkalmas.
- `-Dtest=...`: kiválasztott tesztosztályt vagy tesztmetódust futtat.

## Surefire riportok

A Maven Surefire a teszteredményeket a `target/surefire-reports/` könyvtárban tárolja, többek között XML és szöveges riportokként. CI-rendszerben ezeket a fájlokat lehet tesztriportként publikálni.

## Miért `*Test.java`?

A Surefire alapértelmezett mintái felismerik a `*Test` végű osztályneveket. Ha ettől eltérő nevet használsz, előfordulhat, hogy a teszt nem fut le automatikusan. A projekt ezért következetesen `*Test` neveket használ.

## Gyakori hibák

- **`JAVA_HOME` nem Java 17-re mutat:** állítsd át a környezetet, majd ellenőrizd `mvn -version` paranccsal.
- **Chrome nem található:** telepítsd a Chrome-ot, és ellenőrizd, hogy a felhasználó el tudja indítani.
- **Driver letöltési hiba:** ellenőrizd a hálózatot/proxyt; a Selenium Manager első futáskor letölthet komponenseket.
- **`No tests were executed`:** ellenőrizd a tesztosztály nevét, a `src/test/java` útvonalat és a Surefire konfigurációt.
- **A teszt túl korán olvas ki egy elemet:** használj explicit waitet, ne rögzített `Thread.sleep` hívást.
