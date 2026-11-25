# Feladat I.B – Kerületszámítás `Kor` osztállyal

> Tananyag: Junior Java Backend fejlesztő III. (OOP alapok), Feladat I.B – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Vegyük elő a korábbi kerületszámító megoldásunkat (2. lecke, Feladat VI: téglalap, négyzet, háromszög és kör kerülete alprogram-túltöltéssel).
- Módosítsuk úgy, hogy **logikai típusú paraméter helyett** létrehozunk egy `Kor` osztályt, és azt adjuk át típusként a metódusnak.

Előzmény: a 2. lecke Feladat VI megoldásai: [VI.A – kerület túltöltéssel](../../02-java-alapok-2/13-feladat-6a-kerulet-tultoltes/), [VI.B – kör kerülete](../../02-java-alapok-2/14-feladat-6b-kor-kerulete/), [VI.C – terület túltöltéssel](../../02-java-alapok-2/15-feladat-6c-terulet-tultoltes/). Ez a változat önállóan, az előzmény nélkül is futtatható.

## Mit mutat be?

- miért gond a túltöltésnél, ha két változat paraméterlistája azonos (`kerulet(double)` – négyzet és kör);
- saját típus (osztály) mint paramétertípus: `kerulet(Kor kor)`;
- statikus metódusok egy segédosztályban (`Kerulet`).

## Mi változott az előző lépéshez képest?

- A négyzet és a kör kerülete is egyetlen számból számolható, ezért a túltöltésnél ütköznének. A diasor szerinti korábbi megoldásban ezt egy logikai paraméter döntötte el (pl. `kerulet(double ertek, boolean kor)`); a [repó 2. leckés megoldása](../../02-java-alapok-2/14-feladat-6b-kor-kerulete/) pedig a paraméter típusával (`kerulet(int)` négyzet, `kerulet(double)` kör) – ami törékeny, hiszen `kerulet(5)` és `kerulet(5.0)` mást számol.
- Most a kör egy `Kor` objektum, a metódus szignatúrája `kerulet(Kor kor)`, így a fordító egyértelműen a típus alapján választ, és a hívás is beszédesebb. A többi síkidom `double` oldalhosszakat kap.

## Fájlok

- `src/Kor.java` – a kör (sugár).
- `src/Kerulet.java` – a négy túltöltött `kerulet` metódus.
- `src/App.java` – menü, bekérés, számítás.

## Futtatás

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Példa:

```text
=== Kerületszámítás ===
...
Választás: 4
sugár: 2
A kör kerülete: 12,57
```

A tizedes- és ezreselválasztó a gép területi beállításától (locale) függ, angol beállításnál pl. `12.57`, illetve `230,000`.
