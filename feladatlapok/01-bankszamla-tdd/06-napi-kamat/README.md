# 6. lépés – Napi kamat jóváírása

> Tananyag: Bankszámla TDD feladatlap – lásd a [lánc áttekintőjét](../README.md).

## Feladat

- Napi kamat jóváírása: `napiKamatJovairas()` – kamat = egyenleg × napiKamat / 100, egész forintra kerekítve.
- A jóváírt kamat kerüljön a naplóba; 0 Ft-os kamatot ne könyveljünk.

## Mit mutat be?

- Lebegőpontos számítás és kerekítés (`Math.round`) tesztelése határértékekkel.
- Feltételezés rögzítése tesztben: a napi kamat **százalékban** értendő.

## Mi változott az előző lépéshez képest?

Előző lépés: [05-tranzakcio-naplo](../05-tranzakcio-naplo/)

- Új metódus: `napiKamatJovairas`.

## A TDD ciklus ebben a lépésben (piros → zöld → refaktor)

1. **Piros:** `napiKamatJovairas` – nincs ilyen metódus.
2. **Zöld:** `long kamat = (long) (egyenleg * napiKamat / 100); egyenleg += kamat;` – a 100 Ft-os eset zöld.
3. **Piros:** `kamatKerekites` – a 15 000 Ft-os eset 1-et ad 2 helyett, mert a `(long)` levágja a tizedeseket.
4. **Zöld:** `Math.round(...)`.
5. **Piros → zöld:** `nullaKamatNemKonyvelodik` – `if (kamat > 0)` a naplózás elé.
6. **Refaktor:** Javadoc a képlettel.

## Fájlok

- `pom.xml` – Maven projekt (Java 21, JUnit 6).
- `src/main/java/hu/learning/feladatlapok/bankszamla/Bankszamla.java` – a bankszámla osztály.
- `src/main/java/hu/learning/feladatlapok/bankszamla/NincsFedezetException.java` – saját kivétel fedezethiányra.
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

Következő lépés: [07-fajl-io](../07-fajl-io/)
