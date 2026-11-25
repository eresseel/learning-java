# Feladat VII.A – Rendezés és bináris keresés

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Feladat VII.A – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Töltsünk fel egy negyven elemű tömböt 1–100 közötti véletlenszámokkal úgy, hogy egy szám csak egyszer szerepelhet.
- Listázzuk ki az elemeket 5x8-as formában.
- Rendezzük a tömböt csökkenő sorrendbe.
- Listázzuk ki a rendezett tömböt 4x10-es formában.
- Kérjünk be egy számot, és bináris kereséssel állapítsuk meg, megtalálható-e a tömbben.
- A VII.A és VII.B feladathoz különböző rendezési algoritmust kell használni.

## Mit mutat be?

- Egyedi véletlenszámok generálása: `while` ciklus + eldöntés tétele (`tartalmazza`) – a már szereplő számot eldobjuk.
- **(Maximum)kiválasztásos rendezés**, saját megvalósításban: minden lépésben a rendezetlen rész legnagyobb elemét cseréljük a helyére. (A VII.B beszúrásos rendezést használ.)
- **Bináris keresés** csökkenő sorrendű tömbben: a tartomány felezése; mivel a tömb csökkenő, az irányok fordítottak a megszokott (növekvő) változathoz képest.
- Tömb átadása metódusnak referencia szerint: a rendező metódus a hívó tömbjét módosítja.
- Táblázatos kiírás: sortörés minden `oszlopokSzama`-adik elem után (`(i + 1) % oszlopokSzama == 0`).

## Fájlok

- `src/App.java` – a program.

## Futtatás

A feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa (a számok véletlenszerűek):

```text
$ printf '50\n' | java -cp out App
Az eredeti tömb (5 sor x 8 oszlop):
  97  92  18  83  28  34  53  65
  12  81  80  63  70   3  90  62
  55  78  38  11  88  89  20  21
   6  66  10  85  16  68  47  96
  50  44  91  69   5  25  61  41

A csökkenő sorrendbe rendezett tömb (4 sor x 10 oszlop):
  97  96  92  91  90  89  88  85  83  81
  80  78  70  69  68  66  65  63  62  61
  55  53  50  47  44  41  38  34  28  25
  21  20  18  16  12  11  10   6   5   3

Keresett szám: A(z) 50 megtalálható a tömbben, indexe: 22
```
