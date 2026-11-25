# 05 – Interfész, S.O.L.I.D., kollekciók

Az 5. lecke (Botlik Béla: *Junior Java Backend fejlesztő V. – Interface, S.O.L.I.D, kollekciók*) példái és feladatai: interfészek, a S.O.L.I.D. tervezési elvek, csomagoló osztályok és a Java Collections Framework (List, Set, Map).

| Feladat | Téma |
| --- | --- |
| [Példa – RepulInterface](01-pelda-repul-interfesz/) | interfész a `Minden`-hierarchián keresztben („repülő dolgok” lánc 4. lépése) |
| [Példa – beépített interfészek](02-pelda-beepitett-interfeszek/) | `Comparable`, `Cloneable`, `Closeable`, `Iterable` |
| [Feladat I – Kapcsolatok és szolgáltatások CSV-ben](03-feladat-1-kapcsolatok-csv/) | közös `CsvMentheto` interfész, fájlírás |
| [Példa – S.O.L.I.D.](04-pelda-solid/) | az öt alapelv a diasor példái alapján |
| [Példa – kollekciók](05-pelda-kollekciok/) | csomagoló osztályok, `List`, `Set`, `Map` megvalósítások |
| [Feladat II – Informatikai szótár](06-feladat-2-it-szotar/) | `szotar.csv` → `Map`, kétirányú keresés |
| [Házi feladat – Zöldséges árlista](07-hazi-1-zoldseges-arlista/) | `Map` műveletek menüből |
| [Feladat IV – Felhasználók](08-feladat-4-felhasznalok/) | `felhasznalok.txt` → `ArrayList`, szűrés jogosultság szerint |
| [Házi feladat II – Véletlenszámok listában](09-hazi-2-veletlenszam-lista/) | `List` műveletek, `subList`, törlés bejárás közben |

A diasor számozásában nincs „Feladat III.”, ezért az itt sem szerepel.

A „repülő dolgok” példalánc korábbi lépései: [03 – Jármű polimorfizmus](../03-oop-alapok/03-pelda-jarmu-polimorfizmus/) → [04 – RepuloDolgok öröklődés](../04-oop-2-oroklodes-kivetelek/01-pelda-repulodolgok-oroklodes/) → [04 – RepuloDolgok absztrakt](../04-oop-2-oroklodes-kivetelek/02-pelda-repulodolgok-absztrakt/) → [05 – RepulInterface](01-pelda-repul-interfesz/).

## Futtatás

Minden feladat build eszköz nélkül, a JDK-val futtatható (Java 21+). A feladat mappájában (az adatfájlos feladatoknál kötelezően onnan):

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Az interaktív programok pipe-olt bemenettel is futtathatók; ha elfogy a bemenet, a program üzenettel kilép.
