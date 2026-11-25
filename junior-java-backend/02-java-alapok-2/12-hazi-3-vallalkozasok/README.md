# Házi feladat III. – Vállalkozások havi eredményei

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Önálló (házi) feladat III. – lásd a [lecke áttekintőjét](../README.md).

## Feladat

Alkalmazás, amely vállalkozások nyereségét / veszteségét tárolja havi bontásban, metódusok használatával:

- Kérjük be a vállalkozások számát, majd minden vállalkozás előző évi eredményét havi bontásban; az adatokat megfelelő adatszerkezetben tároljuk.
- Listázzuk ki a vállalkozások havi eredményeit és éves összesítésüket.
- Generáljunk egy véletlenszámot (1–12), és csak annak a hónapnak az eredményeit írjuk ki.
- Állapítsuk meg, melyik vállalkozás érte el a legjobb éves eredményt.
- Írjuk ki vállalkozásonként a veszteséges hónapok számát.

## Mit mutat be?

- Adatszerkezet-választás: `int[vallalkozas][honap]` kétdimenziós tömb – egy sor egy vállalkozás 12 hónapja.
- Egy mátrix sora önálló tömbként adható át metódusnak (`evesOsszeg(eredmenyek[v])`).
- Konstans tömb (`HONAPNEVEK`) a hónapnevekhez, sorszám ↔ index átváltás (`honap - 1`).
- Táblázatos kiírás `printf`-fel (`%7d`, `%-6s`).
- A `main` itt is csak metódushívásokból áll.

Feltételezések: az eredmények egész számok ezer forintban, a veszteség negatív szám; holtversenynél a kisebb sorszámú vállalkozás a „legjobb”.

## Fájlok

- `src/App.java` – a program.
- `minta-bemenet.txt` – mintabemenet 3 vállalkozással (saját készítésű adat), a kipróbáláshoz.

## Futtatás

A feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App < minta-bemenet.txt
```

Példa kimenet (a véletlen hónap futásonként más):

```text
Havi eredmények (ezer Ft):
          jan    feb    már    ápr    máj    jún    júl    aug    sze    okt    nov    dec     Éves
1.        120    -40     85     60    -15    200    310     -5     90     75     40    -20      900
2.        -30    -60     10     25     50     80    -10    -25     15     30     45     60      190
3.        300    250   -100    180    220    -50    400    380    120     90    -30    150     1910

Véletlenszerűen kiválasztott hónap: 3. (március)
  1. vállalkozás: 85 ezer Ft
  2. vállalkozás: 10 ezer Ft
  3. vállalkozás: -100 ezer Ft

A legjobb éves eredményt a(z) 3. vállalkozás érte el: 1910 ezer Ft

Veszteséges hónapok száma:
  1. vállalkozás: 4
  2. vállalkozás: 4
  3. vállalkozás: 3
```
