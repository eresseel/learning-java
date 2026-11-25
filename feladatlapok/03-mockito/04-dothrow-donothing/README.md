# 04 – doThrow(...) és doNothing()

> Tananyag: Mockito alapfogalmak – `doThrow(...).when(...)`, `doNothing().when(...)` – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Ha a repository mentése technikai hibát dob (`AdatbazisHibaException`), a `SzamlaService`
  naplózza a hibát, és `SzamlaMuveletException`-be csomagolva továbbdobja (az eredeti ok megmarad).
- Ezt a hibát valódi adatbázis nélkül kell tesztelni.
- A valódi, konzolra író napló (`KonzolNaplo`) mellékhatását tesztben ki kell tudni kapcsolni.

## Mit mutat be?

- **`doThrow(new AdatbazisHibaException(...)).when(repository).save(any())`** – kivételdobás szimulálása.
  Void metódusnál (pl. `save`) csak ez a forma működik: a `when(repository.save(..))` le sem fordul.
  Nem void metódusra (`findBySzamlaszam`) is használható.
- **`assertThrows(...)`** a dobott kivétel elkapására, `getCause()` az eredeti ok ellenőrzésére.
- **`doNothing().when(spy).ir(anyString())`** – a valódi `KonzolNaplo.ir()` nem ír ki semmit,
  de a hívás továbbra is `verify`-jal ellenőrizhető. Mocknál a void metódus amúgy is „semmit sem csinál”,
  ezért a `doNothing` spy esetén igazán hasznos.

## Mi változott az előző lépéshez képest?

- Új kivételek: `AdatbazisHibaException`, `SzamlaMuveletException`.
- Új valódi napló: `KonzolNaplo`.
- `SzamlaService`: a mentés egy `mentes(...)` segédmetódusba került, amely elkapja és becsomagolja a tárolási hibát.
- Új tesztek: `SzamlaServiceDoThrowTest`, `KonzolNaploDoNothingTest`.
  (A `doNothingNelkulValodiLogikaFut` teszt szándékosan kiír egy `[AUDIT] ...` sort.)

## Fájlok

- `src/main/java/.../AdatbazisHibaException.java`, `SzamlaMuveletException.java` – új kivételek.
- `src/main/java/.../KonzolNaplo.java` – új: konzolra író napló.
- `src/main/java/.../SzamlaService.java` – hibakezeléssel bővítve.
- a többi forrás változatlan.
- `src/test/java/.../SzamlaServiceDoThrowTest.java`, `KonzolNaploDoNothingTest.java` – új tesztek; a korábbi tesztek megmaradtak.

## Futtatás

```bash
mvn -B clean verify
```

Várt eredmény: 22 teszt, mind zöld.
