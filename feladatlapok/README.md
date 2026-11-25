# Feladatlapok

Önálló gyakorló feladatlapok és dolgozatok megoldásai, a leckéktől független
sorrendben. Mindegyik Maven projekt (Java 21, JUnit 6).

| Feladat | Téma |
|---|---|
| [Bankszámla – TDD](01-bankszamla-tdd/) | Bankszámla backend TDD-vel, 7 lépésben (piros → zöld → refaktor), fájlkezelés `@TempDir`-rel |
| [Dolgozat II. – Újságok](02-dolgozat-ujsagok/) | Öröklés (`Ujsag` / `Napilap` / `Hetilap`), `final` metódus, setterek kivételekkel, CSV beolvasás tömbbe, ellenőrzött bekérés, hozzáfűzés |
| [Mockito alapfogalmak](03-mockito/) | `mock` vs `spy`, `when/thenReturn`, `verify`, `doReturn`, `doThrow`, `doNothing`, `reset`, `ArgumentCaptor` – a bankszámla témára építve |

## Közös futtatási útmutató

Előfeltétel: JDK 21+ és Maven 3.9+.

Egy projekt (lépés) tesztjei, a projekt mappájából:

```bash
mvn -B -q verify
```

A láncolt feladatoknál (TDD, Mockito) minden lépésmappa külön, teljes projekt;
a lánc README-je írja le, mi változott lépésről lépésre.
