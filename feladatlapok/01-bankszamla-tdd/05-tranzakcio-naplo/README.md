# 5. lépés – Tranzakciók naplózása

> Tananyag: Bankszámla TDD feladatlap – lásd a [lánc áttekintőjét](../README.md).

## Feladat

- Minden sikeres művelet kerüljön be a számla tranzakciós listájába (`ArrayList<String> tranzakciok`).
- Sikertelen művelet ne kerüljön be.
- Átutalásnál mindkét számla naplójába kerüljön bejegyzés.
- A lista kívülről ne legyen módosítható.

## Mit mutat be?

- Belső állapot védelme: `List.copyOf` csak olvasható másolatot ad (egységbezárás).
- Lista tartalmának összehasonlítása `assertEquals(List.of(...), ...)`-szal.

## Mi változott az előző lépéshez képest?

Előző lépés: [04-atutalas](../04-atutalas/)

- Új mező: `tranzakciok`, új metódusok: `getTranzakciok`, `naploz`.
- A `befizetes` és `kifizetes` naplóz.
- Az `atutalas` saját bejegyzéseket ír (`ATUTALAS KI` / `ATUTALAS BE`), ezért nem hívja tovább a `kifizetes`/`befizetes` metódust (különben dupla bejegyzés lenne).

## A TDD ciklus ebben a lépésben (piros → zöld → refaktor)

1. **Piros:** `uresNaplo` – nincs `getTranzakciok`.
2. **Zöld:** mező + getter.
3. **Piros:** `muveletekNaplozasa` – a lista üres marad.
4. **Zöld:** `naploz(...)` hívás a `befizetes` és `kifizetes` végén.
5. **Piros:** `atutalasNaplozasa` – a forrásnál `KIFIZETES`, a célnál `BEFIZETES` szöveg jelenik meg, nem a várt átutalásos bejegyzés.
6. **Zöld + refaktor:** az `atutalas` saját maga végzi a levonást/jóváírást és a naplózást; a fedezetvizsgálat továbbra is a módosítás ELŐTT van. A korábbi átutalás-tesztek továbbra is zöldek – ezért érdemes megtartani őket refaktor közben.
7. **Piros → zöld:** `naploNemModosithato` – a getter `List.copyOf`-ot ad vissza.

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

Következő lépés: [06-napi-kamat](../06-napi-kamat/)
