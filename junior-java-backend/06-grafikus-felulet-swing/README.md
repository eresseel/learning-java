# 06 – Grafikus felület (Swing)

A 6. lecke (Botlik Béla: *Junior Java Backend fejlesztő VI. – Grafikus interfész, eseményvezérelt működés*) feladata három egymásra épülő lépésben. A Swing fő elemei: konténerek (`JFrame`, `JPanel`), komponensek (`JButton`, `JLabel`, `JTextField`…) és események.

| Feladat | Téma |
| --- | --- |
| [Feladat I.A – Első GUI-s program](01-feladat-1a-elso-gui/) | `JFrame`, `JButton`, `JLabel`, `JTextField`, `ActionListener` |
| [Feladat I.B – Folyamatosan frissülő címke](02-feladat-1b-tukrozott-cimke/) | vezérlők másolata, élő tükrözés `DocumentListener`-rel |
| [Feladat I.C – További vezérlők](03-feladat-1c-tovabbi-vezerlok/) | `JSpinner`, `JCheckBox`, `JList`, `JComboBox` és eseményeik |

Mindhárom lépés teljes, önállóan futtatható program; a későbbi lépések megtartják az előzők funkcióit.

## Futtatás

A feladat mappájában (Java 21+):

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Grafikus környezet nélkül (szerver, CI, távoli terminál) ablak nem nyitható. Ilyenkor – vagy ha kifejezetten headless módban indítjuk – a program ablak helyett önellenőrzést futtat: programból állítja a vezérlők értékét, „megnyomja” a gombokat (`doClick()`), és kiírja, hogy a felület a várt módon reagált-e.

```bash
java -Djava.awt.headless=true -cp out App
```
