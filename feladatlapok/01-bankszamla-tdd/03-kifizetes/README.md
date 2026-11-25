# 3. lépés – Kifizetés, fedezethiány

> Tananyag: Bankszámla TDD feladatlap – lásd a [lánc áttekintőjét](../README.md).

## Feladat

- Kifizetés: `kifizetes(long osszeg)` csökkenti az egyenleget.
- Ha nincs elég fedezet, dobjon saját `NincsFedezetException`-t, és az egyenleg maradjon változatlan.
- Nem pozitív összeg itt is hiba.

## Mit mutat be?

- Saját kivételosztály (`extends RuntimeException`).
- `assertThrows` visszatérési értékének vizsgálata (üzenet).
- Határeset-tesztelés: a teljes egyenleg még felvehető, egy forinttal több már nem.

## Mi változott az előző lépéshez képest?

Előző lépés: [02-befizetes](../02-befizetes/)

- Új osztály: `NincsFedezetException`.
- Új metódus: `kifizetes`.

## A TDD ciklus ebben a lépésben (piros → zöld → refaktor)

1. **Piros:** `kifizetesCsokkentiAzEgyenleget` – nincs `kifizetes` metódus.
2. **Zöld:** `egyenleg -= osszeg;`.
3. **Piros:** `fedezetNelkuliKifizetes` – nincs `NincsFedezetException` osztály (fordítási hiba), utána pedig nem dob kivételt.
4. **Zöld:** létrehoztuk a kivételt, és a levonás ELÉ betettük a fedezetvizsgálatot.
5. **Piros → zöld:** `teljesEgyenlegFelveheto` határeset (`>` és nem `>=` a feltétel!), `nemPozitivKifizetesHiba`.
6. **Refaktor:** a már meglévő `pozitivOsszegEllenorzes` újrahasznosítása.

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

Következő lépés: [04-atutalas](../04-atutalas/)
