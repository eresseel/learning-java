# Mockito – alapfogalmak lépésről lépésre

> Tananyag: *Mockito – alapfogalmak és legfontosabb metódusok* (gyakorló feladatlap) – lásd a [feladatlapok áttekintőjét](../README.md).

A Mockito egy Java keretrendszer, amellyel egységtesztekben a külső függőségeket
(adatbázis, napló, külső szolgáltatás) **hamis objektumokkal** helyettesítjük. Így a
teszt kizárólag a vizsgált osztály logikáját ellenőrzi.

A példák a [bankszámla TDD](../01-bankszamla-tdd/) feladat tartományára épülnek:
egy `SzamlaService` függ egy `SzamlaRepository` interfésztől (tárolás) és egy
`AuditNaplo` interfésztől (naplózás). Minden lépés az előző teljes másolata, plusz az új rész.

## Lépések

| Lépés | Téma |
|-------|------|
| [01 – mock, when/thenReturn](01-mock-when-thenreturn/) | `mock()`, `@Mock` + `@InjectMocks`, `when(...).thenReturn(...)`, beállítatlan mock alapértékei |
| [02 – verify, times, never, atLeastOnce](02-verify-times-never-atleastonce/) | hívások ellenőrzése, argumentum-illesztők (`eq`, `any`, `anyString`) |
| [03 – spy, doReturn](03-spy-doreturn/) | `spy(new ...)`, `doReturn(...).when(...)`, miért nem `when(spy.x())` |
| [04 – doThrow, doNothing](04-dothrow-donothing/) | kivétel szimulálása, void metódus mellékhatásának kikapcsolása |
| [05 – reset és ArgumentCaptor](05-reset-and-argumentcaptor/) | `reset(mock)`, `ArgumentCaptor` / `@Captor` |

## Mock és Spy

| Fogalom | Jelentés |
|---------|----------|
| `mock()` | Teljesen hamis objektum. Minden metódusa alapértelmezett értéket ad vissza (`null`, `false`, `0`, üres kollekció); csak a beállított viselkedés működik. |
| `spy()` | Valódi objektum „becsomagolva”. A metódusai az eredeti logikát futtatják, kivéve azokat, amelyeket kifejezetten felülírunk. |

**Mikor melyiket?**

- **Mock**: ha a függőség logikája nem érdekes, csak a hívásokat akarjuk megfigyelni és ellenőrizni.
- **Spy**: ha a valódi logika nagy része fusson, de néhány metódust felülírnánk (pl. adatbázis- vagy hálózati hívás kikapcsolása).

## A legfontosabb metódusok

| Metódus | Mire való? | Lépés |
|---------|-----------|-------|
| `when(...).thenReturn(...)` | Megadja, mit adjon vissza a mockolt metódus. | 01 |
| `doReturn(...).when(...)` | Alternatív szintaxis, főleg spy-hoz, ha a valódi metódust nem szabad meghívni. | 03 |
| `doNothing().when(...)` | A void metódus ne csináljon semmit (mellékhatás kikapcsolása). | 04 |
| `doThrow(...).when(...)` | Kivételdobást szimulál. | 04 |
| `verify(...)` | Meghívták-e a metódust, és milyen paraméterekkel. | 02 |
| `verify(..., times(n))` | Pontosan n hívás történt. | 02 |
| `verify(..., never())` | Egyszer sem hívták. | 02 |
| `verify(..., atLeastOnce())` | Legalább egyszer hívták. | 02 |
| `reset(mock)` | Alapállapotba állítja a mockot (beállítások és rögzített hívások törlése). | 05 |

## A Mockito használat 4 alaplépése

1. **Mock vagy spy létrehozása**: `mock(MyClass.class)` vagy `spy(new MyClass())` (vagy `@Mock` / `@Spy` annotáció).
2. **Viselkedés beállítása**: `when(...).thenReturn(...)`, `doReturn(...).when(...)`.
3. **A tesztelt metódus meghívása.**
4. **Verifikálás**: `verify(mock).metodus(...)`, `verify(mock, times(n))` stb.

A tesztekben ezek az *Arrange* (1–2), *Act* (3) és *Assert* (4) kommentek alatt jelennek meg.

## Közös futtatási útmutató

Minden lépés önálló Maven projekt (Java 21, JUnit 6.1.3, Mockito 5.24.0). A lépés mappájában:

```bash
mvn -B clean verify
```

A `pom.xml` a `maven-dependency-plugin` `properties` céljával a `mockito-core` jar-t
Java agentként tölti be (`-javaagent:...`), mert JDK 21-től a futás közbeni (dinamikus)
agent-betöltés figyelmeztetést ad. A `-Xshare:off` a JVM ezzel járó CDS-figyelmeztetését némítja el.

A példák saját készítésűek; adatbázis vagy hálózat nem kell hozzájuk.
