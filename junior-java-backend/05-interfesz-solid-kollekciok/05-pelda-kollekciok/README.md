# Példa – Java kollekciók

> Tananyag: Junior Java Backend fejlesztő V., „Java kollekciók I–VIII.” és „Csomagoló osztályok” diák – lásd a [lecke áttekintőjét](../README.md).

## Feladat

A diákon szereplő fogalmak kipróbálása:

- csomagoló osztályok (`Integer`, `Double`, `Character`…), autoboxing (`Integer szam = 10;`) és unboxing (`int szam2 = szam;`);
- **List** – rendezett sorozat, duplikáció és `null` is lehet: `ArrayList`, `LinkedList`;
- **Set** – halmaz, egy elem csak egyszer: `HashSet`, `LinkedHashSet`, `TreeSet`;
- **Map** – kulcs–érték párok, egyedi kulccsal: `Hashtable`, `HashMap`, `TreeMap`.

## Mit mutat be?

- változótípusként az interfész (`List`, `Set`, `Map`), a megvalósítás csak a `new` után;
- a megvalósítások közti különbség (pl. a halmazok bejárási sorrendje);
- `Collections.sort`, halmazműveletek (`retainAll`, `addAll`), `entrySet` bejárás;
- csapda: `Integer` objektumok összehasonlítása `==` helyett `equals()`-szal.

A `Comparable` interfész a [beépített interfészek példájában](../02-pelda-beepitett-interfeszek/) szerepel.

## Fájlok

- `src/App.java` – a négy bemutató rész.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```
