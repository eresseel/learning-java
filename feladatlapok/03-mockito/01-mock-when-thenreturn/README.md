# 01 – mock() és when(...).thenReturn(...)

> Tananyag: Mockito alapfogalmak – `mock()`, `when(...).thenReturn(...)` – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Bankszámla szolgáltatás (`SzamlaService`) készítése, amely egy `SzamlaRepository` interfésztől függ.
- Funkciók: egyenleg lekérdezése, befizetés, kifizetés (fedezethiány esetén `FedezethianyException`).
- A szolgáltatás tesztelése valódi adatbázis nélkül, a repository helyére mockot téve.

## Mit mutat be?

- **`mock(Osztaly.class)`**: teljesen hamis objektum. A beállítatlan metódusok alapértéket adnak:
  objektumra `null`, `boolean`-ra `false`, számra `0` (lásd `beallitatlanMockAlapertelmezettErtekeketAd`).
- **`when(mock.metodus(arg)).thenReturn(ertek)`**: csak erre a paraméterre adja a megadott értéket;
  más paraméterre továbbra is az alapérték jön. Több érték megadásakor az egymás utáni hívások sorban kapják őket.
- **`@ExtendWith(MockitoExtension.class)` + `@Mock` + `@InjectMocks`**: ugyanez annotációkkal;
  a Mockito a mockot konstruktoron keresztül injektálja a tesztelt objektumba.
- Függőséginjektálás: a service a konstruktorban kapja a repositoryt, ezért cserélhető.

## Fájlok

- `src/main/java/.../Szamla.java` – record: számlaszám + egyenleg.
- `src/main/java/.../SzamlaRepository.java` – a tárolás interfésze (ezt mockoljuk).
- `src/main/java/.../SzamlaService.java` – az üzleti logika (ezt teszteljük).
- `src/main/java/.../FedezethianyException.java` – fedezethiány kivétel.
- `src/test/java/.../SzamlaServiceMockTest.java` – `mock()` + `when/thenReturn`, alapértékek.
- `src/test/java/.../SzamlaServiceAnnotacioTest.java` – `@Mock` + `@InjectMocks`.

## Futtatás

```bash
mvn -B clean verify
```

Várt eredmény: 8 teszt, mind zöld.
