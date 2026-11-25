# Bankszámla – TDD lépésről lépésre

> Tananyag: Bankszámla TDD feladatlap – lásd a [feladatlapok áttekintőjét](../README.md).

Egy majdani banki alkalmazás backend oldalának bankszámla-funkcionalitása,
**TDD (Test Driven Development)** szerint fejlesztve.

## A feladat (a feladatlap alapján)

- A számla adatai: számlaszám (szöveges), egyenleg (egész szám), napi kamat (valós szám), a tranzakciók listája (`ArrayList<String>`).
- Funkciók: egyenleg betöltése fájlból (`"egyenleg" + számlaszám + ".txt"`), befizetés, kifizetés, átutalás számlák között, tranzakciók naplózása, napi kamat jóváírás, egyenleg kiírása fájlba, tranzakciótörténet kiírása fájlba (`"tranzakcio" + dátum + ".txt"`).
- A funkcionalitás TDD szerint valósuljon meg.

## A TDD ciklus

1. **Piros** – megírunk egy tesztet egy még nem létező viselkedésre. A teszt bukik (vagy le sem fordul).
2. **Zöld** – a lehető legkevesebb kóddal elérjük, hogy a teszt (és az összes korábbi) átmenjen.
3. **Refaktor** – a zöld tesztek védelme alatt kitakarítjuk a kódot (duplikáció, elnevezések, kiemelés).

Minden lépésmappa egy teljes, önállóan futtatható Maven projekt a lépés végi
(zöld) állapotban. A lépés README-je leírja, milyen sorrendben születtek a
tesztek és a kód (piros → zöld → refaktor).

## Lépések

| Lépés | Téma |
|---|---|
| [01-szamla-letrehozas](01-szamla-letrehozas/) | Számla létrehozása, konstruktorbeli ellenőrzés |
| [02-befizetes](02-befizetes/) | Befizetés, paraméterezett teszt |
| [03-kifizetes](03-kifizetes/) | Kifizetés, saját `NincsFedezetException` |
| [04-atutalas](04-atutalas/) | Átutalás két számla között |
| [05-tranzakcio-naplo](05-tranzakcio-naplo/) | Tranzakciók naplózása (`ArrayList<String>`) |
| [06-napi-kamat](06-napi-kamat/) | Napi kamat jóváírása, kerekítés |
| [07-fajl-io](07-fajl-io/) | Egyenleg betöltése / kiírása, tranzakciótörténet fájlba, `@TempDir` |

## Saját döntések

- Az egyenleg típusa `long` (egész forint).
- A napi kamat **százalékban** értendő (0.01 = napi 0,01%); a kamat `Math.round`-dal egész forintra kerekítődik, a 0 Ft-os kamat nem kerül a naplóba.
- A tranzakciós fájlnévben a dátum ISO formátumú (`tranzakcio2026-11-25.txt`).
- A fájlműveletek a könyvtárat (és a dátumot) paraméterként kapják, hogy a tesztek ideiglenes mappában, rögzített dátummal futhassanak.

A [Mockito feladatlap](../03-mockito/) ugyanerre a bankszámla-témára épül, ott a függőségek (repository, napló) mockolása a téma.

## Futtatás

Bármelyik lépés mappájából:

```bash
mvn -B -q verify
```

Az összes lépés egyszerre (a `01-bankszamla-tdd` mappából):

```bash
for d in 0*/; do (cd "$d" && mvn -B -q verify) || echo "HIBA: $d"; done
```
