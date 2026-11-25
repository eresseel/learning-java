# Feladat II – Állatmenhely (összefoglaló projektmunka)

> Tananyag: Junior Java Backend fejlesztő IV. (OOP II.), Feladat II – az „Összefoglaló projektmunka” specifikációja alapján; lásd a [lecke áttekintőjét](../README.md).

## Feladat

Állatmenhely nyilvántartó program:

- a menhely kutyát és macskát fogad; minden állatról tudjuk a nevét (nem lehet üres) és a súlyát (minimum 2 kg);
- kutya: marmagasság (minimum 10 cm); macska: rövidszőrű vagy hosszúszőrű;
- általános állat nem jöhet létre, csak kutya vagy macska (absztrakt ős);
- az ős előírja a `napiEtelAdag()` metódust: kutya: súly / 50, macska: súly / 70;
- sem a kutya, sem a macska osztály nem származtatható tovább (`final`);
- menü:
  1. **Felvétel** – adatbekérés konzolról, tárolás max. 20 elemű tömbben; ha betelt, további állat nem vehető fel;
  2. **Listázás** – minden állat összes adata listaszerűen, a sor végén a napi ételadaggal; csak ha van állat;
  3. **Felszabadítás** – a legrégebben felvett állat kikerül, a tároló frissül; csak ha van állat;
  4. **Kilépés**.

Saját döntések: a súlyt kilogrammban, a napi ételadagot is kilogrammban (3 tizedesre) adjuk meg; a hibás adatokat (üres név, 2 kg alatti súly, 10 cm alatti marmagasság, hibás számformátum) az osztályok kivétellel jelzik, a program kiírja a hibát és visszatér a menübe.

## Mit mutat be?

- absztrakt osztály és absztrakt metódus, `final` osztály;
- öröklődés, `super(...)`, polimorf `toString()`;
- adatellenőrzés kivételdobással, kivételek elkapása a menüben;
- tömbös tárolás, elemek eltolása törléskor.

## Fájlok

- `src/Allat.java` – absztrakt ős, közös validáció, `napiEtelAdag()` előírása.
- `src/Kutya.java`, `src/Macska.java` – a két `final` leszármazott.
- `src/Menhely.java` – a 20 elemű tároló és műveletei.
- `src/App.java` – menü és bekérés.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa pipe-olt bemenettel (egy kutya, egy macska, egy hibás felvétel, listázás, felszabadítás, listázás, kilépés):

```bash
printf '1\nk\nBodri\n25\n55\n1\nm\nCirmi\n4,2\nr\n1\nk\nPici\n1,5\n20\n2\n3\n2\n4\n' | java -cp out App
```

```text
Hiba: A súly legalább 2.0 kg kell legyen! A felvétel elmaradt.
...
 1. Kutya   Bodri          25,0 kg, marmagasság: 55 cm,   napi ételadag: 0,500 kg
 2. Macska  Cirmi           4,2 kg, rövidszőrű,           napi ételadag: 0,060 kg
...
Gazdihoz került: Bodri
```

A tizedesjel a gép területi beállításától függ.
