# Feladat I.A – Első GUI-s program

> Tananyag: Junior Java Backend fejlesztő VI. (Grafikus interfész, eseményvezérelt működés), Feladat I.A – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Helyezzünk fel a frame-re `JButton`, `JLabel` és `JTextField` komponenst.
- A gombot megnyomva a címke vegye fel a szövegmező szövegét, a szövegmező pedig ezután törlődjön.

Saját kiegészítés: az Enter a szövegmezőben ugyanazt teszi, mint a gomb.

## Mit mutat be?

- Swing fő elemei: konténer (`JFrame`, `JPanel`), komponensek, események;
- elrendezéskezelő (`FlowLayout`);
- eseménykezelés: `addActionListener` lambdával;
- a GUI indítása az eseménykezelő szálon (`SwingUtilities.invokeLater`);
- headless környezetben (ablak nélkül) a panel logikája `doClick()`-kel programból ellenőrizhető.

## Fájlok

- `src/GuiPanel.java` – a komponensek és az eseménykezelés.
- `src/FoAblak.java` – a `JFrame` főablak.
- `src/App.java` – belépési pont, headless önellenőrzéssel.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Grafikus környezet nélkül (pl. szerveren) az ablak helyett önellenőrzés fut:

```bash
java -Djava.awt.headless=true -cp out App
```

```text
Nincs grafikus környezet (headless) – a panel önellenőrzése fut.
Címke: "Helló, Swing!", szövegmező: ""
OK – a gomb átmásolta és törölte a szöveget.
```
