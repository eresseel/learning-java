# 07 – Page Object Model (POM)

## Cél
Válaszd szét a teszt üzleti szándékát az oldal technikai részleteitől.

## Feladat
Hozz létre `ContactPage` osztályt, amely:
- megnyitja a helyi űrlapot;
- kezeli a név és e-mail mezőt;
- elküldi az űrlapot;
- visszaadja a státusz- és validációs üzeneteket.

A teszt ne tartalmazzon közvetlen `By` locatorokat. Ezek a Page Object osztály felelősségei.

## POM alapelvek
- A Page Object tárolja az oldalhoz tartozó locatorokat és műveleteket.
- A teszt az üzleti folyamatot írja le.
- Az ellenőrzések (assertionök) maradjanak a tesztben, ne a Page Objectben.
- A driver életciklusát a teszt kezelje.

## Futtatás
```bash
mvn clean test
```

## Továbbfejlesztési feladat
Készíts külön `HomePage` osztályt, és a teszt a kezdőoldalról navigáljon a kapcsolatfelvételi oldalra.
