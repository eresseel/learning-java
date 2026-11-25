# Példa – S.O.L.I.D. alapelvek

> Tananyag: Junior Java Backend fejlesztő V., „S.O.L.I.D. I–IX.” diák – lásd a [lecke áttekintőjét](../README.md).

## Feladat

A diák szöveges példáinak kódváltozata:

| Elv | Diasor példája | Itt |
| --- | --- | --- |
| **S** – egy felelősség | a számoló osztály ne feleljen a megjelenítésért is | `TeruletOsszegzo` számol, `EredmenyKiiro` megjelenít |
| **O** – nyitott/zárt | ha „A” más osztályok (D, E…) objektumaival is számol, ne kelljen „A”-t módosítani | a `Haromszog` utólag került be, az összegző változatlan |
| **L** – Liskov | az altípus bárhol helyettesítheti az őst | bármely `Sikidom` megvalósítás működik az összegzővel |
| **I** – interfész elkülönítés | a HTML+XML interfészt bontsuk ketté | `HtmlMentheto`, `XmlMentheto`; a `Riport` mindkettőt, a `Szamla` csak az XML-t implementálja |
| **D** – függőség invertálás | a BL osztály ne egy konkrét adatbáziskezelőhöz kötődjön | a `RendelesKezelo` az `AdatTarolo` interfészt kapja; `MemoriaTarolo` és `KonzolTarolo` cserélhető |

## Mit mutat be?

- interfészek mint absztrakciók, amelyektől a kód függ;
- konstruktoron át átadott függőség (dependency injection alapja);
- `List.of(...)` változtathatatlan lista.

## Fájlok

- `src/Sikidom.java`, `src/Teglalap.java`, `src/Kor.java`, `src/Haromszog.java` – síkidomok (O, L).
- `src/TeruletOsszegzo.java`, `src/EredmenyKiiro.java` – számolás és megjelenítés (S).
- `src/HtmlMentheto.java`, `src/XmlMentheto.java`, `src/Riport.java`, `src/Szamla.java` – interfész elkülönítés (I).
- `src/AdatTarolo.java`, `src/MemoriaTarolo.java`, `src/KonzolTarolo.java`, `src/RendelesKezelo.java` – függőség invertálás (D).
- `src/App.java` – belépési pont.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```
