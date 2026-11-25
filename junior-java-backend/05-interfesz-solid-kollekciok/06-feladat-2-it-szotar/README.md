# Feladat II – Informatikai szótár (Map)

> Tananyag: Junior Java Backend fejlesztő V., Feladat II – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Alkalmazás, amely egy fájlban (`szotar.csv`) tárolt informatikai szótárban keres.
- A felhasználó megadja, hogy angol–magyar vagy magyar–angol fordítást kér, és a keresett kifejezést.
- A program a fájl tartalmát egy `Map` interfész-megvalósításban tárolja, végrehajtja a keresést, és kijelzi az eredményt.

A `szotar.csv` mintaadat saját készítésű, az eredeti fájl nem volt a tananyag része. Formátuma: fejlécsor, majd soronként `angol;magyar` (UTF-8).

Saját kiegészítések: a keresés kis- és nagybetűtől független; ha a kifejezés nincs meg, a program legfeljebb 5 azonos kezdőbetűjű szót javasol.

## Mit mutat be?

- fájl soronkénti olvasása `BufferedReader`-rel, try-with-resources;
- két `TreeMap` a két irányhoz, `Map.merge` ismétlődő kulcsokra;
- `keySet()` bejárása, `ArrayList` a találatokhoz.

## Fájlok

- `szotar.csv` – a minta szótár (39 szópár).
- `src/Szotar.java` – beolvasás és keresés.
- `src/App.java` – menü.

## Futtatás

A programot a feladat mappájából kell indítani, hogy megtalálja a `szotar.csv`-t.

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa:

```bash
printf '1\nInheritance\n2\ntűzfal\n1\nprocess\n3\n' | java -cp out App
```

```text
Inheritance = öröklődés
tűzfal = firewall
A(z) "process" kifejezés nem szerepel a szótárban.
Hasonló kezdetű szavak: password, printer, processor
```
