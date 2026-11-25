# Feladat I – Kapcsolatok és szolgáltatások CSV mentése

> Tananyag: Junior Java Backend fejlesztő V., Feladat I – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Alkalmazás egy cég kapcsolatainak és szolgáltatásainak adminisztrálására.
- Kapcsolatról tárolandó: név, cím, e-mail, telefon.
- Szolgáltatásról tárolandó: megnevezés, ár.
- Mind a kapcsolatok, mind a szolgáltatások **egységesen** legyenek menthetők CSV formátumban.

A kezdő adatok fiktívek. A mentés a futtatási mappában létrehozza a `kapcsolatok.csv` és `szolgaltatasok.csv` fájlt (pontosvessző elválasztóval, UTF-8 kódolással); ezek a program kimenetei, nem részei a forrásnak.

## Mit mutat be?

- közös interfész (`CsvMentheto`) két, egymással rokonságban nem álló osztályhoz;
- interfész konstans és `default` metódus;
- a mentő (`CsvMento`) csak az absztrakciótól függ – bármilyen új `CsvMentheto` osztályt módosítás nélkül ment (a S.O.L.I.D. nyitva-zárt és függőség-invertálás elve);
- fájlírás try-with-resources szerkezettel, `IOException` kezelése;
- `ArrayList` (a lecke kollekciós része).

## Fájlok

- `src/CsvMentheto.java` – a közös interfész.
- `src/Kapcsolat.java`, `src/Szolgaltatas.java` – az implementáló osztályok.
- `src/CsvMento.java` – egységes CSV mentés.
- `src/App.java` – menü.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa (új szolgáltatás, mentés, kilépés):

```bash
printf '2\nAdatmentés; havi díj\n8000\n4\n5\n' | java -cp out App
```

```text
--- szolgaltatasok.csv ---
Megnevezés;Ár
Weboldal-karbantartás (havi);25000
Rendszergazdai óradíj;12000
"Adatmentés; havi díj";8000
```
