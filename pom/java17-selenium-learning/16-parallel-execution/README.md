# 16 – Párhuzamos tesztfuttatás és szálbiztos WebDriver

**Nehézség:** ★★★★★

## Cél
A teszt-készlet felgyorsítása: több teszt fut egyszerre, mindegyik a saját böngészőjében.

## Feladat
- Kapcsold be a JUnit 5 párhuzamos futtatást a `junit-platform.properties` fájlban.
- Készíts `DriverFactory`-t, amely `ThreadLocal`-lal szálanként külön böngészőt kezel.
- Futtasd a teszteket, és a konzolkimenet szálneveiből ellenőrizd, hogy tényleg párhuzamosan futottak.

## Mit tanulunk?
- JUnit 5 párhuzamos végrehajtás: `enabled`, `mode.default`, `mode.classes.default`, fix szálszám;
- miért nem szálbiztos a WebDriver, és mit old meg a `ThreadLocal`;
- a `remove()` szerepe szálkészletnél;
- párhuzamosság buktatói: közös, módosítható állapot (statikus mezők, megosztott adat), tesztek közti függőség.

## Futtatás
```bash
cd 16-parallel-execution
mvn clean test -Dheadless=true
```
Ablakos módban egyszerre 3 Chrome-ablak nyílik meg, ezért ajánlott a `-Dheadless=true`. A konzolon a `START` / `END` sorokban a szál neve (pl. `ForkJoinPool-1-worker-2`) látszik.

## Fontos szabályok párhuzamos teszteknél
- A tesztek legyenek egymástól teljesen függetlenek, ne támaszkodjanak a futási sorrendre.
- Ne használj közös, módosítható statikus adatot szinkronizálás nélkül.
- Ha a tesztek ugyanazt a külső erőforrást módosítják (pl. ugyanaz a felhasználó egy éles rendszerben), a párhuzamosság hibát okoz. Ilyenkor `@ResourceLock` vagy szekvenciális futás kell.

## Továbbfejlesztési feladat
1. Állítsd a párhuzamosságot `dynamic` stratégiára (`config.strategy=dynamic`), és figyeld meg a különbséget.
2. Mérd meg a futási időt `parallelism=1` és `parallelism=3` mellett.
3. Jelölj meg egy osztályt `@Execution(ExecutionMode.SAME_THREAD)`-del, és nézd meg, mi változik.
