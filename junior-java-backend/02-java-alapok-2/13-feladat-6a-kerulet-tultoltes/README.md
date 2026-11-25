# Feladat VI.A – Kerületszámítás túltöltéssel

> Tananyag: Junior Java Backend fejlesztő II. – Java alapok II. (JuniorJavaBackend_02.pdf), Feladat VI.A – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- **A.** Készítsünk programot, amely ki tudja számolni egy téglalap, négyzet és háromszög kerületét; használjunk alprogram-túltöltést.
- **B.** Bővítsük a programot a kör kerületének kiszámításával.
- **C.** Készítsük el túltöltéses metódusokkal a területszámítást is.
- Képletek: téglalap K = 2(a+b), T = a·b; négyzet K = 4a, T = a²; háromszög K = a+b+c, T = (a·mₐ)/2; kör K = 2rπ, T = r²π.

Lépések: [A – kerület](../13-feladat-6a-kerulet-tultoltes/) → [B – kör](../14-feladat-6b-kor-kerulete/) → [C – terület](../15-feladat-6c-terulet-tultoltes/).

Ez az **A** lépés.

## Mit mutat be?

- Metódus-túltöltés (overloading): három azonos nevű `kerulet` metódus, eltérő számú paraméterrel.
- A metódus szignatúrája = név + paraméterlista (típusok és sorrend); a fordító ez alapján választ.

| Hívás | Alakzat |
|---|---|
| `kerulet(int a)` | négyzet |
| `kerulet(int a, int b)` | téglalap |
| `kerulet(int a, int b, int c)` | háromszög |

- Menü nyilas `switch`-csel (`case 1 -> { ... }`).

Feltételezés: az oldalhosszak pozitív egész centiméterek (ez a B és C lépésnél fontos lesz a túltöltés miatt). A háromszög-egyenlőtlenséget a program nem ellenőrzi.

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
$ printf '1\n3\n4\n' | java -cp out App
Kerületszámítás – 1: téglalap, 2: négyzet, 3: háromszög
Választás: a oldal (cm): b oldal (cm): A téglalap kerülete: 14 cm
```
