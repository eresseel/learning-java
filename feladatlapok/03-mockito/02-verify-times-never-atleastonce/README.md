# 02 – verify, times(n), never(), atLeastOnce()

> Tananyag: Mockito alapfogalmak – `verify(...)` és változatai – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- A szolgáltatás minden pénzmozgást és elutasított kifizetést naplózzon egy `AuditNaplo`-ba.
- Tesztekkel ellenőrizzük, hogy a service a megfelelő függőség-metódusokat hívja-e, hányszor és milyen paraméterrel.

## Mit mutat be?

- **`verify(mock).metodus(arg)`** – pontosan egyszer, ezzel a paraméterrel hívták-e (= `times(1)`).
- **`verify(mock, times(n))`** – pontosan n hívás.
- **`verify(mock, never())`** – egyszer sem hívták (pl. fedezethiánynál nincs mentés).
- **`verify(mock, atLeastOnce())`** – legalább egy hívás.
- **Argumentum-illesztők**: `any(Szamla.class)`, `anyString()`, `eq("111")`, `startsWith("ELUTASITVA")`.
  Szabály: ha egy hívásban egy paraméterhez illesztőt használunk, akkor mindegyikhez azt kell.
- `verifyNoMoreInteractions(...)` – a felsoroltakon kívül nem volt más hívás.
- A void metódusú `AuditNaplo` tipikus „csak megfigyeljük” függőség – ez a mock fő terepe.

## Mi változott az előző lépéshez képest?

- Új interfész: `AuditNaplo` (`void ir(String uzenet)`).
- `SzamlaService` konstruktora két függőséget kap (`repository`, `naplo`); befizetés/kifizetés naplóz.
- A korábbi tesztek kiegészültek a napló mockkal (különben nem jönne létre a service).
- Új teszt: `SzamlaServiceVerifyTest`.

## Fájlok

- `src/main/java/.../AuditNaplo.java` – új: naplózó függőség.
- `src/main/java/.../SzamlaService.java` – naplózással bővítve.
- `src/main/java/.../Szamla.java`, `SzamlaRepository.java`, `FedezethianyException.java` – változatlan.
- `src/test/java/.../SzamlaServiceVerifyTest.java` – új: verify-változatok.
- `src/test/java/.../SzamlaServiceMockTest.java`, `SzamlaServiceAnnotacioTest.java` – az 1. lépésből.

## Futtatás

```bash
mvn -B clean verify
```

Várt eredmény: 13 teszt, mind zöld.
