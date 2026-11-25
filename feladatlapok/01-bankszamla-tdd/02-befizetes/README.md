# 2. lépés – Befizetés

> Tananyag: Bankszámla TDD feladatlap – lásd a [lánc áttekintőjét](../README.md).

## Feladat

- Befizetés: `befizetes(long osszeg)` növeli az egyenleget.
- 0 vagy negatív összeg befizetése `IllegalArgumentException`, az egyenleg nem változik.

## Mit mutat be?

- Paraméterezett teszt: `@ParameterizedTest` + `@ValueSource` – ugyanaz a teszt több bemenettel.
- Egy hibás művelet ne hagyjon félkész állapotot.

## Mi változott az előző lépéshez képest?

Előző lépés: [01-szamla-letrehozas](../01-szamla-letrehozas/)

- Új metódus: `befizetes`, és a `pozitivOsszegEllenorzes` segédmetódus.
- Új tesztek a `BankszamlaTest` „2. lépés” blokkjában.

## A TDD ciklus ebben a lépésben (piros → zöld → refaktor)

1. **Piros:** `befizetesNoveliAzEgyenleget` – fordítási hiba, nincs `befizetes` metódus.
2. **Zöld:** `egyenleg += osszeg;` – a legegyszerűbb működő megoldás.
3. **Piros:** `nemPozitivBefizetesHiba` (0, -1, -1000) – nem dob kivételt.
4. **Zöld:** `if (osszeg <= 0) throw ...`.
5. **Refaktor:** az ellenőrzést kiemeltük a `pozitivOsszegEllenorzes` privát metódusba, mert a következő lépések (kifizetés, átutalás) is ugyanezt fogják használni.

## Fájlok

- `pom.xml` – Maven projekt (Java 21, JUnit 6).
- `src/main/java/hu/learning/feladatlapok/bankszamla/Bankszamla.java` – a bankszámla osztály.
- `src/test/java/hu/learning/feladatlapok/bankszamla/BankszamlaTest.java` – a TDD során először megírt tesztek.

## Futtatás

Ebben a lépésben nincs `main` metódus: a „program” a tesztkészlet.

```bash
mvn -B -q verify
```

Részletes, tesztenkénti kimenet (a `@DisplayName` szövegekkel):

```bash
mvn -B test
```

Következő lépés: [03-kifizetes](../03-kifizetes/)
