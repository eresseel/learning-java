# 14 – Tesztszervezés és konfiguráció: címkék, profilok, beállítások

**Nehézség:** ★★★☆☆

## Cél
A tesztek csoportosítása és a futtatás beállításainak kivezetése a kódból, hogy ugyanaz a teszt különböző környezetben (fejlesztői gép, CI) módosítás nélkül fusson.

## Feladat
- Jelöld a teszteket `@Tag`-gel (`smoke`, `regression`), és futtasd őket külön.
- Olvasd a beállításokat (`headless`, `wait.seconds`) a `Config` osztályból, rendszer-tulajdonságokból.
- Add át a beállításokat a Maven parancssorból és Maven profillal.
- Csoportosítsd a teszteket `@Nested` osztályokkal, és használd a `@DisplayName`-et és a `@Disabled`-et.

## Mit tanulunk?
- JUnit 5 címkék és a Surefire szűrés: `-Dgroups=...`, `-DexcludedGroups=...`;
- konfiguráció rendszer-tulajdonságokból, alapértékekkel;
- a Surefire `systemPropertyVariables` beállítása: a Maven tulajdonság átkerül a teszt JVM-be;
- Maven profil (`-Pheadless`);
- a tesztek szervezése: smoke és regresszió.

## Futtatás
```bash
cd 14-test-organization-and-config
mvn clean test                          # minden teszt, ablakos Chrome
mvn clean test -Dgroups=smoke           # csak a smoke tesztek
mvn clean test -DexcludedGroups=smoke   # minden, kivéve a smoke
mvn clean test -Pheadless               # ablak nélkül (profil)
mvn clean test -Dheadless=true -Dwait.seconds=10   # beállítások parancssorból
```

## Továbbfejlesztési feladat
1. Adj a `Config`-hoz `baseUrl()` beállítást, és használd egy tesztben.
2. Hozz létre `slow` címkét, és zárd ki a gyors futtatásból.
3. Készíts Maven profilt `smoke` néven, amely a `groups` tulajdonságot `smoke`-ra állítja.
