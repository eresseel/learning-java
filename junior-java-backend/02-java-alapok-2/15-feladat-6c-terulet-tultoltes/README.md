# Feladat VI.C – Területszámítás túltöltéssel

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Feladat VI.C – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- **A.** Készítsünk programot, amely ki tudja számolni egy téglalap, négyzet és háromszög kerületét; használjunk alprogram-túltöltést.
- **B.** Bővítsük a programot a kör kerületének kiszámításával.
- **C.** Készítsük el túltöltéses metódusokkal a területszámítást is.
- Képletek: téglalap K = 2(a+b), T = a·b; négyzet K = 4a, T = a²; háromszög K = a+b+c, T = (a·mₐ)/2; kör K = 2rπ, T = r²π.

Lépések: [A – kerület](../13-feladat-6a-kerulet-tultoltes/) → [B – kör](../14-feladat-6b-kor-kerulete/) → [C – terület](../15-feladat-6c-terulet-tultoltes/).

Ez a **C** (végső) lépés.

## Mit mutat be?

A túltöltött metódusok teljes készlete:

| Kerület | Terület | Alakzat |
|---|---|---|
| `kerulet(int a)` | `terulet(int a)` | négyzet |
| `kerulet(int a, int b)` | `terulet(int a, int b)` | téglalap |
| `kerulet(int a, int b, int c)` | `terulet(double a, double ma)` | háromszög |
| `kerulet(double r)` | `terulet(double r)` | kör |

- A háromszög és a téglalap területe is kétparaméteres: csak a **típus** (`int` vs. `double`) különbözteti meg őket.
  Ha a háromszög területét két `int`-tel hívnánk, a fordító a téglalap képletét választaná – ezért a hívásnál `double` értékek szerepelnek.
- Implicit bővítő konverzió (`int` → `double`) szerepe a metódusválasztásban.

## Mi változott az előző lépéshez képest?

- Négy új túltöltött `terulet` metódus.
- Minden menüpont a kerületet és a területet is kiírja.
- A háromszögnél az `a` oldalhoz tartozó magasságot (`ma`) is bekéri.

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

Példa:

```text
$ printf '3\n3\n4\n5\n4\n' | java -cp out App
Kerület- és területszámítás – 1: téglalap, 2: négyzet, 3: háromszög, 4: kör
Választás: a oldal (cm): b oldal (cm): c oldal (cm): az a oldalhoz tartozó magasság (cm): A háromszög kerülete: 12 cm
A háromszög területe: 6.00 cm²
```
