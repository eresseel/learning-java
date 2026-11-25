# 4. lépés – a végleges BaseTest (headless, letöltési mappa) és a kijelentkezés

## Cél
A BaseTest felkészítése arra, hogy a tesztek **helyben, Dockerben és CI-ben** is fussanak, valamint a vizsgakövetelmény **11. pontja** (kijelentkezés).

## Mi változott
- **Módosult:** `BaseTest.java`: letöltési mappa, `ChromeOptions`, headless-detektálás környezeti változókból.
- **Módosult:** `DashboardPage.java`: új: `logout()` (két lépés: a felhasználói menü megnyitása, majd a Logout link).
- **Új:** `Test11_LogoutTest.java`.

## A BaseTest három új felelőssége
1. **Letöltési mappa** (`target/downloads`): létrehozza, és minden teszt előtt kiüríti. Így egy korábbi futás CSV-je nem adhat álpozitív eredményt.
2. **Chrome beállítások** (`ChromeOptions`): a letöltés célmappája, és nincs "hová mentsem" ablak.
3. **Headless-detektálás**: ha a `GITHUB_ACTIONS`, a `DOCKER_CONTAINER` vagy a `HEADLESS=true` környezeti változó be van állítva, a Chrome ablak nélkül indul.

| Kapcsoló | Miért kell |
|---|---|
| `--headless=new` | a modern, a rendes Chrome-hoz közelebbi headless mód |
| `--no-sandbox` | konténerben, root felhasználóval a Chrome sandbox nem indul |
| `--disable-dev-shm-usage` | konténerben kicsi a `/dev/shm`, e nélkül a Chrome összeomolhat |
| `--window-size=1920,1080` | headlessben alapból kicsi az ablak, és az OrangeHRM reszponzív felülete összecsukhatja a menüt |
| `--lang=en-US`, fix User-Agent | a szöveges lokátorok (`Invalid credentials`, `Yes, Delete`) angol felületet várnak |

A `maximize()` csak nem headless módban fut, mert headlessben felülírná az ablakméretet.

## A kijelentkezés tesztje
`dashboardPage.logout()`: megnyitja a jobb felső felhasználói menüt (`oxd-userdropdown-tab`), majd a `Logout` linkre kattint. A teszt azt ellenőrzi, hogy az URL tartalmazza a `login` szót.

## Futtatás headless módban
```bash
HEADLESS=true mvn clean test
```

## Futtatás

```bash
cd step04-headless-logout
mvn clean test
```

(JDK 17+, Google Chrome és internet kell. A Maven Wrapper csak a 10. lépéstől van a mappában; addig `mvn` kell, vagy másold át a 10. lépésből az `mvnw`, `mvnw.cmd` és `.mvn/` elemeket.)

## Kész, ha
4 + 0 zöld teszt (3 login + 1 logout), és a `HEADLESS=true` változóval ablak nélkül is lefut.

## A védéshez
„Egy kódbázis, külön konfiguráció nélkül fut helyben, Dockerben és CI-ben is, mert a headless módot környezeti változóból döntöm el.”
