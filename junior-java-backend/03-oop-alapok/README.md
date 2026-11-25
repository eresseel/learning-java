# 03 – OOP alapok

A 3. lecke (Botlik Béla: *Junior Java Backend fejlesztő III. – OOP alapok*) példái és feladatai: osztályok, objektumok, konstruktor, getter/setter, láthatósági szintek, statikusság, valamint az öröklés és a polimorfizmus első bemutatása.

| Feladat | Téma |
| --- | --- |
| [Példa – Laptop osztály](01-pelda-laptop-osztaly/) | osztály, objektum, konstruktor, getter/setter, láthatóság |
| [Példa – statikusság](02-pelda-statikussag/) | statikus mező, metódus, blokk, beágyazott osztály |
| [Példa – Jármű polimorfizmus](03-pelda-jarmu-polimorfizmus/) | öröklés, felülírás, polimorfizmus („repülő dolgok” lánc 1. lépése) |
| [Feladat I.A – Laptop kereskedés](04-feladat-1a-laptop-kereskedes/) | a diasor laptopkereskedés-modellje |
| [Feladat I.B – Kerület `Kor` osztállyal](05-feladat-1b-kerulet-kor-osztaly/) | saját típus paraméterként, túltöltés (a 2. lecke Feladat VI folytatása) |
| [Feladat II – Családi kassza](06-feladat-2-csaladi-kassza/) | `Kassa` osztály, menü |
| [Önálló feladat – Kalóriaszámláló](07-hazi-kaloriaszamlalo/) | osztály állapottal és műveletekkel |

## Futtatás

Minden feladat build eszköz nélkül, a JDK-val futtatható (Java 21+). A feladat mappájában:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Az interaktív programok pipe-olt bemenettel is futtathatók, pl. `printf '3\n4\n' | java -cp out App`; ha elfogy a bemenet, a program üzenettel kilép.
