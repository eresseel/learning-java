# OrangeHRM UI tesztautomatizálás – lépésről lépésre

Tíz, egymásra épülő, **önállóan futtatható** Maven projekt. Az 1. lépés a legegyszerűbb működő teszt, a 9. lépés a vizsgakövetelmény mind a 11 pontját lefedi, a 10. lépés pedig hozzáadja a futtatási infrastruktúrát (Maven Wrapper, Docker, GitHub Actions). Minden mappa teljes projekt, a saját `README.md`-jével: cél, mi változott, magyarázat, futtatás, „kész, ha”, és egy mondat a védéshez.

| # | Mappa | Új a korábbi lépéshez képest | Követelmény |
|---|---|---|---|
| 1 | `step01-elso-teszt` | `pom.xml`, egyetlen teszt, minden egy helyen | – |
| 2 | `step02-basetest-login` | `BaseTest`, bejelentkezési tesztek | 2 |
| 3 | `step03-page-object` | `LoginPage`, `DashboardPage`, Page Object Model | 2 |
| 4 | `step04-headless-logout` | végleges `BaseTest` (headless, letöltési mappa), kijelentkezés | 11 |
| 5 | `step05-listazas` | `AdminPage` (listázás) | 4 |
| 6 | `step06-regisztracio` | új felhasználó létrehozása (Add User) | 1 |
| 7 | `step07-adatvezerelt-felvitel` | `PimPage` (Add Employee), `@CsvFileSource` | 6, 7 |
| 8 | `step08-lapozas-modositas-torles` | lapozás, módosítás, törlés | 5, 8, 9 |
| 9 | `step09-riport-adatkezelesi-nyilatkozat` | riport CSV-be, Privacy Policy | 10, 3 |
| 10 | `step10-docker-ci-wrapper` | Maven Wrapper, Docker, GitHub Actions | – |

## Használat
- Előfeltétel: JDK 17+, Google Chrome, internetkapcsolat (a tesztek a publikus demót használják: `https://opensource-demo.orangehrmlive.com`, `Admin` / `admin123`). A 10. lépés a Maven Wrappert is tartalmazza, a többihez `mvn` kell.
- Egy lépés futtatása: `cd stepXX-... && mvn clean test`
- **Két lépés különbsége** (a legjobb tanulási mód): `diff -ru step02-basetest-login step03-page-object`

## Tudnivalók (őszintén)
- **A kódot nem fordítottam és nem futtattam**, mert a készítés közben nem volt Maven, `javac`, böngésző és hálózat. A 5–10. lépés kódja az eredeti projekt osztályaiból készült (részhalmazok), az 1–4. lépés kódja új, kézzel írt. Az elkészítés után statikus ellenőrzést végeztem (hivatkozások, zárójelek), de az első futtatást neked kell elvégezned. Ha egy lépés hibát ad, jelezd, és javítom.
- A tesztek a **publikus, megosztott demót** használják. Ingadozhat a sebesség, és a Test08/Test09 a demo adatait módosítja/törli.
- A Javadoc a page osztályokon a végleges osztály leírását mondja, a korai lépésekben ennek csak egy része van meg.
- Az eredeti zip tartalmaz néhány, sehol nem hívott metódust. Ezeket a lépések kihagyják (lásd a 9. lépés README-jét).
