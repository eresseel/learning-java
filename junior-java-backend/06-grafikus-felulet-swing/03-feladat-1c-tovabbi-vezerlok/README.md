# Feladat I.C – További vezérlők

> Tananyag: Junior Java Backend fejlesztő VI. (Grafikus interfész, eseményvezérelt működés), Feladat I.C – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Ismerkedjünk további vezérlőkkel: `JSpinner`, `JCheckBox` (a diasoron „Checkbox”), `JList`, `JComboBox` (a diasoron „ComboBox”).

Saját megvalósítás: egy kis „profil” űrlap – életkor (számléptető, 0–120), hírlevél (jelölőnégyzet), város (legördülő lista), programozási nyelvek (többes kijelölésű lista). Minden vezérlő változása megjelenik egy állapotsorban, az „Összegzés” gomb pedig egy mondatba foglalja az aktuális értékeket.

## Mit mutat be?

- vezérlőnként eltérő eseménytípus: `ChangeListener` (`JSpinner`), `ItemListener` (`JCheckBox`), `ListSelectionListener` (`JList`), `ActionListener` (`JComboBox`, `JButton`);
- modellek: `SpinnerNumberModel`, a `JList` kijelölési módja;
- görgethető lista `JScrollPane`-ben;
- a vezérlők szigorú típuskezelése (pl. a `JSpinner` értéke `Object`, át kell alakítani).

## Mi változott az előző lépéshez képest?

- Az [előző lépés](../02-feladat-1b-tukrozott-cimke/) két sora (I.A és I.B) változatlan.
- Új, harmadik keretes rész a négy új vezérlővel, egy „Összegzés” gombbal, állapotsorral és eredménycímkével.
- Az önellenőrzés az új vezérlők eseményeit és az összegzést is vizsgálja.

## Fájlok

- `src/GuiPanel.java` – mindhárom rész komponensei és eseménykezelői.
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

```text
OK   – JList esemény (Utolsó esemény: nyelvek = [Java, SQL])
OK   – Összegzés (30 éves, Szeged, nyelvek: Java, SQL, hírlevél: igen)
```
