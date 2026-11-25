# 03 – spy() és doReturn(...).when(...)

> Tananyag: Mockito alapfogalmak – `spy()`, `doReturn(...).when(...)` – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Napi kamat jóváírása (`KamatService.kamatJovairas`): a kamatot a `KamatKalkulator` számolja,
  az éves kamatlábat pedig egy „külső rendszerből” kérdezné le (`aktualisKamatlab()`).
- Teszteljük úgy, hogy a valódi számolás fusson, de a külső lekérdezés ne.

## Mit mutat be?

- **`spy(new KamatKalkulator())`**: valódi objektum; a felül nem írt metódusok (pl. `napiKamat`) az eredeti logikát futtatják.
- **`doReturn(0.0365).when(spy).aktualisKamatlab()`**: csak ezt az egy metódust írjuk felül, és a valódi kód **nem fut le**.
- **Miért nem `when(spy.aktualisKamatlab())`?** Mert a `when(...)` argumentumát a Java előbb kiértékeli,
  vagyis a valódi metódus meghívódik – itt ez kivételt dob (lásd `whenSzintaxisSpyonMeghivjaAValodiMetodust`).
- Spy valódi repositoryn (`MemoriaSzamlaRepository`): a tárolás tényleg működik, közben `verify`-jal ellenőrizhető.

**Mock vagy spy?** A mock minden metódusa hamis (csak megfigyelésre jó); a spy-nál a logika nagy része valódi,
és csak a „veszélyes” (adatbázis, hálózat) részt kapcsoljuk ki.

## Mi változott az előző lépéshez képest?

- Új valódi osztályok: `KamatKalkulator`, `MemoriaSzamlaRepository` (HashMap-es repository), `KamatService`.
- Új teszt: `KamatServiceSpyTest`. A `SzamlaService` és a korábbi tesztek változatlanok.

## Fájlok

- `src/main/java/.../KamatKalkulator.java` – új: napi kamat + „külső” kamatláb.
- `src/main/java/.../KamatService.java` – új: kamat jóváírása.
- `src/main/java/.../MemoriaSzamlaRepository.java` – új: memóriás repository.
- `src/main/java/.../SzamlaService.java`, `Szamla.java`, `SzamlaRepository.java`, `AuditNaplo.java`, `FedezethianyException.java` – változatlan.
- `src/test/java/.../KamatServiceSpyTest.java` – új: spy és doReturn.
- a 01–02. lépés tesztjei.

## Futtatás

```bash
mvn -B clean verify
```

Várt eredmény: 17 teszt, mind zöld.
