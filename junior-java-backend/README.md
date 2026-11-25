# Junior Java Backend fejlesztő – gyakorló feladatok

A „Junior Java Backend fejlesztő” tanfolyam 7 leckéjének összes kódpéldája, órai feladata és házi feladata, futtatható Java kóddal. Minden feladat saját mappában van, saját `README.md`-vel (feladatleírás, mit mutat be, futtatás), a forráskódban pedig magyar kommentek magyarázzák a lépéseket.

| Lecke | Téma | Feladatok | Build |
|---|---|---|---|
| [01 – Java alapok I.](01-java-alapok-1/) | Típusok, operátorok, bekérés/kiírás, elágazások, ciklusok | 15 | `javac` |
| [02 – Java alapok II.](02-java-alapok-2/) | Tömbök, mátrixok, String/Math/Random, metódusok, túltöltés, rendezés, fájlkezelés | 19 | `javac` |
| [03 – OOP alapok](03-oop-alapok/) | Osztály, objektum, konstruktor, getter/setter, `static`, polimorfizmus | 7 | `javac` |
| [04 – OOP II.](04-oop-2-oroklodes-kivetelek/) | Öröklődés, absztrakt osztály, kivételkezelés, állatmenhely projekt | 5 | `javac` |
| [05 – Interfész, SOLID, kollekciók](05-interfesz-solid-kollekciok/) | Interfészek, S.O.L.I.D., List/Set/Map, CSV | 9 | `javac` |
| [06 – Grafikus felület](06-grafikus-felulet-swing/) | Swing, eseménykezelés | 3 | `javac` |
| [07 – Adatbázis, hálózat, XML/JSON](07-adatbazis-halozat-xml-json/) | JDBC (MySQL Dockerben), HttpClient, web scraping, DOM XML, GSON, socket | 6 | Maven |

## Egymásra épülő láncok

- **01 / III.A:** egyjegyű szám vizsgálata 3 lépésben (`04` → `05` → `06`).
- **02 / I.A → I.B, II.A → II.C, IV → V:** tömbök, mátrix, majd metódusokra bontás.
- **Kerületszámítás:** 02 / VI.A → VI.B → VI.C → 03 / I.B (`Kor` osztály).
- **RepuloDolgok:** 03 / jármű-polimorfizmus → 04 / öröklődés → 04 / absztrakt osztály → 05 / `RepulInterface`.
- **06 / Swing:** I.A → I.B → I.C.
- **07 / JDBC:** Feladat I → Feladat III.B (ugyanaz az adatbázis, JSON-exporttal).

## Futtatás

- `javac` feladatok: a feladat mappájából `javac -d out src/*.java`, majd `java -cp out App`.
- Maven feladatok (07): lásd a lecke README-jét (`docker compose up -d` az adatbázishoz, majd `mvn -B -q compile exec:java`).

> A mintaadat-fájlok (pl. `szotar.csv`, `felhasznalok.txt`, dept/emp SQL) saját készítésűek, mert az eredeti fájlok nem voltak a tananyag részei.
