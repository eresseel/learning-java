# Feladat VI.B – Kör kerülete

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Feladat VI.B – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- **A.** Készítsünk programot, amely ki tudja számolni egy téglalap, négyzet és háromszög kerületét; használjunk alprogram-túltöltést.
- **B.** Bővítsük a programot a kör kerületének kiszámításával.
- **C.** Készítsük el túltöltéses metódusokkal a területszámítást is.
- Képletek: téglalap K = 2(a+b), T = a·b; négyzet K = 4a, T = a²; háromszög K = a+b+c, T = (a·mₐ)/2; kör K = 2rπ, T = r²π.

Lépések: [A – kerület](../13-feladat-6a-kerulet-tultoltes/) → [B – kör](../14-feladat-6b-kor-kerulete/) → [C – terület](../15-feladat-6c-terulet-tultoltes/).

Ez a **B** lépés.

## Mit mutat be?

- A túltöltés korlátját: a kör kerülete is **egyparaméteres**, akárcsak a négyzeté. Két `kerulet(int)` nem létezhet, mert a szignatúrájuk azonos lenne – a paraméter neve nem része a szignatúrának.
- A megoldás: a sugár `double` típusú, így a `kerulet(double r)` már más szignatúra.
- Csapda: `kerulet(5)` a négyzetet, `kerulet(5.0)` a kört számolja! A fordító a legpontosabb típusegyezést keresi.
  (Valós projektben ezért inkább beszédes, eltérő neveket vagy külön osztályokat használnánk – ezek későbbi leckék témái.)

## Mi változott az előző lépéshez képest?

- Új menüpont (4: kör) és új túltöltött metódus: `double kerulet(double r)`.
- Új bekérő metódus valós számhoz: `valosBekerese` (nem túltöltés, mert a paraméterlistája megegyezne az `egeszBekerese`-ével).

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
$ printf '4\n2.5\n' | java -cp out App
Kerületszámítás – 1: téglalap, 2: négyzet, 3: háromszög, 4: kör
Választás: sugár (cm): A kör kerülete: 15.71 cm
```

A tizedesjel (`2.5` vagy `2,5`) a rendszer területi beállításától függ.
