# 05 – reset(mock) és ArgumentCaptor

> Tananyag: Mockito alapfogalmak – `reset(mock)`; kiegészítésként `ArgumentCaptor` – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Ellenőrizzük, hogy a `SzamlaService` által **újonnan létrehozott és elmentett** `Szamla` egyenlege helyes.
- Mutassuk be, mi történik a mockkal `reset` után.

## Mit mutat be?

- **`ArgumentCaptor<Szamla>`** (`@Captor` annotációval vagy `ArgumentCaptor.forClass(...)`):
  `verify(repository).save(captor.capture())` közben elkapja a paramétert, majd `getValue()` /
  `getAllValues()` adja vissza. Akkor hasznos, ha a paramétert a tesztelt kód belül állítja elő,
  és nem csak egyenlőséget, hanem egyes mezőit akarjuk vizsgálni.
- **`reset(mock)`**: törli a beállított viselkedést (utána újra `null`/`0`/`false` jön) **és**
  a rögzített hívásokat is (`verifyNoInteractions` igaz lesz). Jól szervezett teszteknél ritkán kell,
  mert minden teszt friss mockot kap (`@BeforeEach`, `MockitoExtension`).

## Mi változott az előző lépéshez képest?

- A forráskód változatlan; csak két új tesztosztály került be:
  `SzamlaServiceCaptorTest` és `SzamlaServiceResetTest`.

## Fájlok

- `src/main/java/...` – a 4. lépéssel azonos.
- `src/test/java/.../SzamlaServiceCaptorTest.java` – új: ArgumentCaptor.
- `src/test/java/.../SzamlaServiceResetTest.java` – új: reset.
- a 01–04. lépés tesztjei.

## Futtatás

```bash
mvn -B clean verify
```

Várt eredmény: 26 teszt, mind zöld (a kimenetben egy szándékos `[AUDIT] ...` sor látható).
