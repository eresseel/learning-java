# 01 – JUnit 5 alapok

## Cél
Ismerd meg a Maven projekt alapstruktúráját és a JUnit 5 legalapvetőbb annotációit és assertionjeit.

## Feladat
A `Calculator` osztály két egész számot adjon össze. A teszt ellenőrizze:
- pozitív számok összegét;
- negatív számok összegét;
- nullával történő összeadást.

## Megoldás
A megoldás a `src/main/java` alatt található. A teszt a `src/test/java` alatt van. A `@Test` annotáció jelzi a tesztmetódust, az `assertEquals` pedig összeveti az elvárt és a tényleges értéket.

## Futtatás
```bash
mvn clean test
```

## Továbbfejlesztési feladat
Adj hozzá `subtract(int left, int right)` metódust és legalább három tesztet hozzá.
