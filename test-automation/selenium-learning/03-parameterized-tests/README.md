# 03 – Paraméterezett tesztek

## Cél
Ismétlődő tesztlogika helyett egy tesztet futtass több bemeneti adatra.

## Feladat
Készíts `PasswordPolicy` osztályt, amely legalább 8 karakter hosszú jelszót fogad el. A teszt több elfogadott és elutasított bemenetet ellenőrizzen.

## Mit tanulunk?
- `@ParameterizedTest`;
- `@ValueSource` és `@CsvSource`;
- olvasható tesztadatok.

## Futtatás
```bash
mvn clean test
```

## Továbbfejlesztési feladat
Egészítsd ki a szabályt legalább egy számjegy kötelező jelenlétével, és írj tesztet az új szabályra.
