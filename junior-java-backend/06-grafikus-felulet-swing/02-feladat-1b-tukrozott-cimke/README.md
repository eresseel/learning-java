# Feladat I.B – Folyamatosan frissülő címke

> Tananyag: Junior Java Backend fejlesztő VI. (Grafikus interfész, eseményvezérelt működés), Feladat I.B – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Készítsünk másolatot a frame-en lévő vezérlőkből (szövegmező, címke).
- Érjük el, hogy az új címke folyamatosan az új szövegmező értékét mutassa.

## Mit mutat be?

- `DocumentListener`: a szövegmező tartalmának minden változására (beszúrás, törlés) reagál, nem csak gombnyomásra;
- interfész megvalósítása névtelen belső osztállyal (három kötelező metódus);
- összetett elrendezés: `BoxLayout` (sorok egymás alatt) + soronként `FlowLayout`, keretes címmel (`TitledBorder`).

## Mi változott az előző lépéshez képest?

- Az [előző lépés](../01-feladat-1a-elso-gui/) gombnyomásos sora változatlanul megmaradt, egy keretes alpanelre került.
- Új második sor: szövegmező + „tükör” címke, amelyet egy `DocumentListener` frissít gépelés közben.
- A panel elrendezése `FlowLayout`-ról `BoxLayout`-ra változott.
- Az önellenőrzés a tükrözést is vizsgálja (beírás és törlés után).

## Fájlok

- `src/GuiPanel.java` – a két sor és az eseménykezelők.
- `src/FoAblak.java` – a `JFrame` főablak.
- `src/App.java` – belépési pont, headless önellenőrzéssel.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Grafikus környezet nélkül:

```bash
java -Djava.awt.headless=true -cp out App
```
