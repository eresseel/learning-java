# 4. lépés – Átutalás két számla között

> Tananyag: Bankszámla TDD feladatlap – lásd a [lánc áttekintőjét](../README.md).

## Feladat

- Átutalás két számla között: `atutalas(Bankszamla cel, long osszeg)`.
- A forrás egyenlege csökken, a célé ugyanannyival nő.
- Fedezethiánynál egyik számla sem változhat.
- Null célra és saját számlára (azonos számlaszám) nem lehet utalni.

## Mit mutat be?

- Két objektum együttműködésének tesztelése.
- „Mindent vagy semmit” jellegű művelet: hiba esetén ne legyen részleges változás.

## Mi változott az előző lépéshez képest?

Előző lépés: [03-kifizetes](../03-kifizetes/)

- Új metódus: `atutalas`, amely a már tesztelt `kifizetes` és `befizetes` metódusokra épül.

## A TDD ciklus ebben a lépésben (piros → zöld → refaktor)

1. **Piros:** `atutalas` teszt – nincs ilyen metódus.
2. **Zöld:** `kifizetes(osszeg); cel.befizetes(osszeg);` – a meglévő, tesztelt metódusokat használjuk.
3. **Piros → zöld:** `atutalasFedezetHianyban` – valójában már elsőre zöld, mert a `kifizetes` a jóváírás ELŐTT dob. Ez is hasznos teszt: rögzíti az elvárt viselkedést, és véd a későbbi regresszió ellen.
4. **Piros → zöld:** `ervenytelenCel` – a null és önmagára utalás ellenőrzése.
5. **Refaktor:** nem kellett – a kód már tiszta.

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

Következő lépés: [05-tranzakcio-naplo](../05-tranzakcio-naplo/)
