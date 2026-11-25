# 1. lépés – Számla létrehozása

> Tananyag: Bankszámla TDD feladatlap – lásd a [lánc áttekintőjét](../README.md).

## Feladat

- Legyen egy `Bankszamla` osztály a következő adatokkal: számlaszám (`String`), egyenleg (egész, `long`), napi kamat (`double`).
- Új számla 0 Ft egyenleggel nyílik.
- Üres / `null` számlaszámmal és negatív kamattal ne lehessen számlát nyitni.

## Mit mutat be?

- A TDD alapciklusa: **piros → zöld → refaktor**.
- JUnit 6 alapok: `@Test`, `@BeforeEach`, `@DisplayName`, `assertEquals`, `assertThrows`.
- Konstruktorbeli ellenőrzés (érvénytelen állapotú objektum ne jöhessen létre).

## A TDD ciklus ebben a lépésben (piros → zöld → refaktor)

1. **Piros:** megírtuk az `ujSzamlaAdatai` tesztet. Még a `Bankszamla` osztály sem létezett, így a teszt **le sem fordult** – TDD-ben ez is piros állapot.
2. **Zöld:** létrehoztuk az osztályt a három mezővel, konstruktorral és getterekkel.
3. **Piros:** új tesztek: `uresSzamlaszamHiba`, `negativKamatHiba` – buknak, mert nincs ellenőrzés.
4. **Zöld:** a konstruktorba bekerült a két `if` + `IllegalArgumentException`.
5. **Refaktor:** a hibaüzenetek egységesítése, Javadoc; a tesztek közös előkészítését `@BeforeEach`-be tettük.

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

Következő lépés: [02-befizetes](../02-befizetes/)
