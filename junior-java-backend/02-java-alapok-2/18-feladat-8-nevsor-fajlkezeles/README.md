# Feladat VIII. – Osztálynévsor fájlkezeléssel

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Feladat VIII. – lásd a [lecke áttekintőjét](../README.md).

## Feladat

Egy osztály névsorának tárolását kell megoldani. A menüből a felhasználó választhat:

1. Adatok bekérése konzolról
2. Adatok beolvasása fájlból
3. Névsor kilistázása
4. Adatok kiírása fájlba
5. Kilépés

A választott művelet után a menü újra megjelenik, amíg a felhasználó a kilépést nem választja.

## Mit mutat be?

- Menüvezérelt program: `do-while` főciklus + `switch`.
- Alapszintű fájlkezelés a java.io csomaggal: adatfolyam megnyitása → olvasás/írás, amíg van adat → bezárás.
  - olvasás: `BufferedReader` + `FileReader`, `readLine()` amíg nem `null`;
  - írás: `BufferedWriter` + `FileWriter`, `write()` + `newLine()`;
  - a **try-with-resources** szerkezet hiba esetén is bezárja a fájlt (adatvesztés elkerülése);
  - `IOException` kezelése: hiányzó fájlnál üzenet, nem összeomlás.
- `ArrayList<String>`: a tömbbel szemben futás közben bővíthető – a névsor hossza előre nem ismert.
- Ábécérendes listázás magyar `Collator`-ral (lásd VII.B).

Saját döntések:

- Konzolos bekérés: soronként egy név, üres sorig; a nevek hozzáadódnak a meglévő listához.
- Fájlból olvasás: a `nevsor.txt` tartalmát a meglévő listához fűzi (üres sorokat kihagyja).
- Fájlba írás: a `nevsor-mentes.txt` fájlba ír (felülírja), hogy a mintaadat ne sérüljön. Ez a fájl futás közben keletkezik, nem része a repónak.
- A listázás ábécérendben, sorszámozva jelenik meg; a tárolt sorrend nem változik.
- A fájlok UTF-8 kódolásúak, és a program a feladat mappájához képest relatív útvonalon keresi őket – ezért a mappából kell futtatni.

## Fájlok

- `src/App.java` – a program.
- `nevsor.txt` – mintanévsor 10 névvel. A mintaadat saját készítésű (fiktív nevek), az eredeti fájl nem volt a tananyag része.

## Futtatás

A feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa (beolvasás fájlból, két név felvétele, listázás, mentés, kilépés):

```bash
printf '2\n1\nÉkes Panna\nBalogh Gergő\n\n3\n4\n5\n' | java -cp out App
```

```text
Választás: 10 név beolvasva a(z) nevsor.txt fájlból.
...
Választás: Adja meg a neveket soronként, a befejezéshez üres sort üssön:
2 név felvéve, a névsorban összesen 12 név van.
...
Választás: Névsor (12 fő):
  1. Balogh Gergő
  2. Ékes Panna
  3. Farkas Írisz
  ...
 12. Varga Zsófia
...
Választás: 12 név kiírva a(z) nevsor-mentes.txt fájlba.
...
Választás: Viszontlátásra!
```
