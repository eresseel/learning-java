# Java újraprogramozás – videós tananyag példái

A `src/vNN` csomagok a tananyag videóinak sorszámát követik (`v9` = 9. videó). Egyetlen, build eszköz nélküli forrásfa: a teljes `src/` egyben fordítható.

| Videók | Téma |
|---|---|
| v9–v20 | Változók, operátorok (unáris, bináris, összehasonlító, logikai), kifejezések, utasítások, blokkok |
| v21–v31 | Típuskonverzió, elágazások, ciklusok, `break` / `continue` / címke / `return` |
| v34–v58 | Osztályok, láthatóság, referenciák, paraméterátadás, öröklődés, `toString`, `equals` / `hashCode`, `null`, `final`, inicializáló blokkok, absztrakt osztályok, interfészek, enumok |
| v59–v66 | Tömbök, mátrixok, kivételek és saját kivételek, `record`, autoboxing |
| v67–v71 | Generikusok, korlátok, wildcardok, type erasure |
| v73–v83 | Kollekciók: `HashSet`, `TreeSet`, `ArrayList`, `LinkedList`, `HashMap`, `TreeMap`, iterátorok |
| v84–v98 | Névtelen osztályok, lambdák, funkcionális interfészek, `Optional`, Stream API |
| v99–v101 | Annotációk, saját annotációk, `package-info` |
| v106–v113 | I/O: karakter- és bináris streamek, fájl- és útvonalkezelés, NIO csatornák és bufferek |
| v115–v122 | `HttpClient`, `Properties`, rendszerváltozók, szálak, Time API, memóriaszivárgás |

## Futtatás

A `java-reprogramozas` mappából (az I/O példák a `data.dat`, `input.txt` és `resources/` fájlokat relatív útvonalon keresik, ezért innen kell indítani):

```bash
javac -encoding UTF-8 -d out $(find src -name '*.java')
```

```bash
java -cp out v9.Változók
```

> A fájl- és osztálynevek ékezetes karaktereket tartalmaznak, ezért UTF-8 locale és `-encoding UTF-8` szükséges.
