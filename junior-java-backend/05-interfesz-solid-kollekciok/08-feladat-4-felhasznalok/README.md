# Feladat IV – Felhasználók (ArrayList)

> Tananyag: Junior Java Backend fejlesztő V., Feladat IV – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Program, amely felhasználói adatokat tárol: azonosító, jogosultság (Vendég = 1, Felhasználó = 2, Admin = 3), név.
- Az adatokat fájlból szerezzük be (`felhasznalok.txt`).
- Az adatok tárolásához `ArrayList` adatszerkezetet használunk.

A `felhasznalok.txt` mintaadat saját készítésű, az eredeti fájl nem volt a tananyag része (a nevek kitaláltak). Formátuma soronként `azonosito;jogosultsag;nev`; a `#`-tel kezdődő sor megjegyzés. Két szándékosan hibás sort is tartalmaz (ismeretlen jogosultság, nem szám azonosító), hogy látszódjon a hibakezelés.

Saját kiegészítések: menü az összes felhasználó listázására, jogosultság szerinti szűrésre, azonosító szerinti keresésre és jogosultságonkénti statisztikára.

## Mit mutat be?

- `List<Felhasznalo> lista = new ArrayList<>()`, `add`, `size`, bejárás, `forEach`;
- fájl beolvasása `Files.readAllLines`-szal, sorok feldolgozása `split`-tel;
- `enum` a jogosultságokhoz, kód → enum átalakítás;
- hibás sorok kihagyása kivételkezeléssel;
- programozási tételek listán: kiválogatás, keresés, megszámlálás.

## Fájlok

- `felhasznalok.txt` – minta adatfájl.
- `src/Jogosultsag.java` – a három jogosultság enumként.
- `src/Felhasznalo.java` – egy felhasználó, sorfeldolgozással.
- `src/App.java` – beolvasás és menü.

## Futtatás

A programot a feladat mappájából kell indítani.

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa (Admin szűrés, statisztika, kilépés):

```bash
printf '2\n3\n4\n5\n' | java -cp out App
```

```text
Kihagyott sor (10.): Ismeretlen jogosultságkód: 4
Kihagyott sor (11.): For input string: "abc"
Beolvasva 9 felhasználó.
...
Admin jogosultságú felhasználók (2 fő):
 1001  Admin        Admin Aladár
 1007  Admin        Kiss Erika
```
