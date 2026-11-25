# 10 – CI pipeline: build és telepítési minőségkapuk

> Tananyag: *Tesztautomatizálás* (ISTQB CTAL-TAE), IV. fejezet (CI/CD integráció, kockázatok), **V. fejezet** (tesztszintek a CI/CD-ben, build vs. telepítés, tesztver konfiguráció), **VIII. feladat**; elméleti feladatok mintamegoldása: **VI, VII, IX, XI, XII, XIII** – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Építsünk CI/CD pipeline-t (GitHub Actions), amely **szétválasztja**:
  - a **build-szakasz** tesztjeit (komponens, komponensintegráció – Maven Surefire, `*Test`);
  - a **telepítés utáni** rendszerteszteket (Maven Failsafe, `*IT`), amelyek egy már futó, telepített SUT ellen futnak.
- Maven profilokkal lehessen a szakaszokat külön futtatni.
- A README magyarázza el a build és a telepítési minőségkapuk különbségét, és adjon mintamegoldást a tananyag elméleti feladataira.

## Mit mutat be?

### A pipeline

```
 commit / PR
     │
     ▼
┌───────────────────────────── 1. kapu: BUILD (build-gate) ─────────────────────────────┐
│ mvn -B verify -Pbuild-gate                                                             │
│  • fordítás                                                                            │
│  • TAF önteszt (TafSelfCheckTest, ConfigTest, PreflightCheckTest, ...)  – konfiguráció │
│  • komponenstesztek (CartTest, UsernameValidatorTest, ...)                             │
│  • komponensintegráció (MockMvc, REST Assured + RANDOM_PORT, WireMock PayPal)          │
│  • szerződés- és sémateszt                                                             │
│  • csomagolás → webshop jar (artifact)                                                 │
└────────────────────────────────────────┬───────────────────────────────────────────────┘
                     csak siker esetén   │   (bukás → nincs telepítés)
                                         ▼
┌──────────────────────── 2. kapu: TELEPÍTÉS UTÁN (deploy-gate) ─────────────────────────┐
│ ugyanannak a jar-nak a „telepítése” (indítása) → várakozás health = UP-ra              │
│ WEBSHOP_BASE_URL=http://localhost:8080 mvn -B verify -Pdeploy-gate                     │
│  • preflight (health, tesztadat) → sikertelen telepítés = Inconclusive, nem SUT-hiba    │
│  • rendszertesztek: SmokeIT, CartFlowIT (feketedobozos, Spring nélkül)                  │
│  • riportok + a SUT naplója artifactként (TAS/SUT elemzéshez)                           │
└────────────────────────────────────────────────────────────────────────────────────────┘
 ütemezve, éjjel:  mvn -B verify  (teljes regresszió: minden teszt egy futásban)
```

A workflow: [`.github/workflows/ci.yml`](.github/workflows/ci.yml) (`defaults.run.working-directory` erre a mappára mutat). **A GitHub csak a repó gyökerében lévő `.github/workflows/` mappát olvassa** – élesítéshez a fájlt oda kell másolni; itt tananyagként, a lépés részeként szerepel.

### Maven beállítások

| Parancs | Surefire (`*Test`) | Helyi SUT indítása | Failsafe (`*IT`) | Mire való? |
|---|---|---|---|---|
| `mvn verify` | ✔ | ✔ (véletlen port, `spring-boot:start/stop`) | ✔ a helyi SUT ellen | fejlesztői gép, éjszakai futás |
| `mvn verify -Pbuild-gate` | ✔ | – | – | CI 1. kapu |
| `mvn verify -Pdeploy-gate` | – | – | ✔ a `WEBSHOP_BASE_URL` ellen | CI 2. kapu, telepítés után |

Részletek a `pom.xml`-ben:

- `build-helper-maven-plugin:reserve-network-port` – szabad portot foglal a SUT-nak és a JMX vezérlésnek (nincs portütközés a CI runneren);
- `spring-boot-maven-plugin` `start` (pre-integration-test) / `stop` (post-integration-test) – a csomagolt alkalmazás helyi „telepítése”;
- `maven-failsafe-plugin` – az `integration-test` fázisban futtatja az `*IT`-ket; bukásnál is lefut a `post-integration-test` (SUT leállítás), és csak a `verify` fázis buktatja el a buildet;
- kapcsolók: `skipUnitTests`, `skipITs`, `skipLocalSut`; a `webshop.baseUrl` system property-ként jut a Failsafe-be.

### Build vs. telepítési minőségkapu (V. fejezet)

| | Build-kapu | Telepítési kapu |
|---|---|---|
| Mikor fut? | minden commitnál, telepítés **előtt** | telepítés **közben/után**, egy tesztkörnyezetben |
| Tesztszintek | TAF/TAS konfigurációs teszt, komponens, komponensintegráció, szerződés | rendszer-, rendszerintegrációs, füstteszt |
| Mit tesztel? | a forráskódot és a modulok együttműködését (mockolt külső függőségekkel) | a **telepített** terméket: valódi csomag, valódi konfiguráció, valódi hálózat |
| Sebesség | percek – gyors visszajelzés | lassabb (telepítés + indulás) |
| Bukás következménye | nincs csomag, nincs telepítés | a telepítés nem léphet tovább (pl. staging → éles) |
| Újrafuttatás | olcsó | „Telepítés közbeni tesztelés: minőségkapu, de újrafuttatás = újratelepítés” |

A tananyag két változatot említ a rendszertesztek beillesztésére:

- **telepítés közbeni** tesztelés – a teszt a telepítési folyamat része, minőségkapuként megállítja a hibás telepítést; hátránya, hogy újrafuttatáshoz újra kell telepíteni. A mi `deploy-test` jobunk ilyen: a `needs: build-gate` és a sikertelen *IT a folyamatot megállítja.
- **telepítés utáni** tesztelés – külön, független folyamat; rugalmasabb (bármikor újrafuttatható ugyanarra a környezetre: `mvn verify -Pdeploy-gate`), de nem állítja meg a hibás telepítést. Az éjszakai regresszió ilyen.

Fontos elv: **ugyanazt az artifactot** teszteljük a telepítési kapuban, amit a build-kapu elkészített (`upload-artifact` → `download-artifact`), nem építjük újra.

### VIII. feladat – tesztautomatizálási lehetőségek a „Kosár” funkcióhoz, tesztszintenként

| Tesztszint | Mit ellenőrzünk a kosárnál? | A demóban | Pipeline-szakasz |
|---|---|---|---|
| TAF/TAS konfigurációs teszt | a tesztadatok, sémák, forgatókönyvek megvannak és helyesek | `TafSelfCheckTest`, `ConfigTest` | build |
| Komponens | hozzáadás, mennyiség 1..99, összevonás, végösszeg, törlés – a `Cart` osztályon | `domain/CartTest` | build |
| Komponensintegráció | controller + szolgáltatás + domain + JSON + hibakezelés; PayPal kliens WireMock-kal | `CartApiIntegrationTest` (MockMvc), `tests/CartTest` (REST Assured), `PaymentClientWireMockTest` | build |
| Szerződés | a frontend által használt kosár-mezők és hibakódok | `ContractVerificationTest`, `JsonSchemaTest` | build |
| Rendszer | a telepített alkalmazáson a teljes kosár-folyamat | `CartFlowIT`, `SmokeIT` | telepítés után |
| Rendszerintegráció | kosár → fizetés a valódi PayPal sandbox ellen | (WireMock helyett sandbox a staging környezetben – itt nem része) | telepítés után / éjjel |
| Elfogadás | „vásárló 2 terméket vesz, módosítja, fizet” üzleti forgatókönyvek | KDT `vasarlas-fizetessel.csv` (BDD-vel is írható) | éjjel / release előtt |
| Nem-funkcionális | válaszidő, terhelés (sok egyidejű kosár-módosítás) | egyszerű válaszidő-ellenőrzés (04. lépés); terheléshez Gatling/JMeter | éjjel |

## Elméleti feladatok – mintamegoldások

### VI. feladat – irányelvek a fizetési folyamat Selenium-alapú pilot projektjéhez

1. **Cél és mérhető siker-kritériumok** előre: pl. a fizetési folyamat 10 kritikus esete automatizálva, < 2% instabil futás, a regresszió ideje 1 napról 1 órára csökken.
2. **Szűk, reprezentatív hatókör**: néhány stabil, gyakran futtatandó, nagy kockázatú eset (kosár → fizetés → visszaigazolás; elutasított kártya); nem az egész alkalmazás.
3. **Tesztelhetőség biztosítása**: stabil azonosítók (`data-testid`) a fizetési űrlapon; tesztkörnyezet PayPal **sandbox**-szal vagy mockkal; tesztadat (felhasználók, kártyák) előkészítése és visszaállítása.
4. **Architektúra az első naptól**: TAF rétegek, **Page Object Model**, közös `DriverFactory`, konfiguráció környezeti változókból, explicit várakozások (nincs `Thread.sleep`).
5. **Eszköz- és környezetértékelés**: böngészők/verziók, Selenium Grid vagy konténer, headless futás CI-ban; licenc- és biztonsági szabályok.
6. **CI integráció** már a pilotban: minden commitra füstteszt, éjjel teljes készlet; riport és képernyőkép bukásnál.
7. **Csapat és tudás**: felelős TAE, képzés, kódáttekintés a tesztkódra is; kódolási konvenciók.
8. **Értékelés a pilot végén**: mért eredmények vs. célok, karbantartási ráfordítás, instabil tesztek aránya → döntés a kiterjesztésről, a tanulságok beépítése a bevezetési tervbe (fokozatos kiterjesztés modulonként).

### VII. feladat – banki mobilalkalmazás automatizált regressziója: kockázatok és enyhítés

| Kockázat | Enyhítési stratégia |
|---|---|
| Eszköz- és OS-töredezettség (sok Android/iOS verzió) | Kockázatalapú eszközmátrix (piaci részesedés), emulátor + valós eszközfarm (felhő) kombinációja |
| Instabil (flaky) UI-tesztek, időzítés | Explicit várakozások, stabil azonosítók (accessibility ID), újrafuttatás csak elemzéssel, a flaky tesztek karanténba |
| Biztonság: valódi ügyféladat, hitelesítő adatok | Szintetikus/maszkolt tesztadat, titkok vaultban (soha a kódban), elkülönített tesztkörnyezet; statikus kódelemzés a tesztkódra |
| 2FA / OTP, biometria | Tesztkörnyezetben tesztüzemmód / mockolt OTP-szolgáltató, tesztfiókok |
| Külső rendszerek (core banking, kártyarendszer) elérhetetlensége | Szolgáltatás-virtualizáció (WireMock), szerződéstesztek |
| Hálózati körülmények, megszakadó kapcsolat | Hálózati profilok szimulálása, a hibaágak külön tesztjei |
| Gyakori UI-változás → magas karbantartási költség | POM/Screen Object, TAF rétegek, a tesztek nagy része API-szinten (tesztpiramis) |
| Erőforrások (CPU, RAM, eszközök), futásidő | Párhuzamosítás, tesztkészlet-priorizálás (füstteszt minden buildnél, teljes készlet éjjel) |
| Áruház-kiadási ciklus, verziófrissítések (Appium, OS) | Verziózott TAS, fokozatos frissítés, regresszió a TAS-ra is; reprodukálható környezet |
| Eredmények értelmezése | Naplózás, képernyőkép/videó, korrelációs ID, TAS/SUT hibaosztályozás (09. lépés) |

### IX. feladat – adatok a TAS vs. SUT hiba eldöntéséhez (3–3 módszer)

- **TAS**: tesztlog (lépések, assertion, stack trace); képernyőkép/videó; lokátor-definíciók, tesztparaméterek, teszteset-metaadatok (+ emulátor/Appium napló).
- **SUT**: szervernapló; API válasz naplózás; auditnapló / felhasználói események (+ frontend konzol, logcat).

Részletesen és a X. feladat elemzésével együtt: [09. lépés](../09-konfiguracio-naplozas-hibaelemzes/README.md).

### XI. feladat – 600 tesztes éjszakai regresszió optimalizálása adatgyűjtéssel

**Adatgyűjtés:** tesztenkénti futásidő, eredmény-történet (pass/fail/flaky), talált hibák (hibakövetőből, teszthez kötve), kódlefedettség modulonként, a modulok hibasűrűsége és változási gyakorisága (verziókezelőből), tesztadat-használat.

**Elemzés és javaslatok:**

1. **Teszt-hisztogram / trend** a futásidőkről: a leglassabb 10% optimalizálása (pl. UI helyett API-szintű előkészítés, felesleges várakozások megszüntetése); **párhuzamos futtatás**.
2. **„Soha nem talál hibát” tesztek**: lefedettség-elemzéssel a redundánsak összevonása/törlése; a stabil, alacsony kockázatú területek tesztjei ritkábban (heti) fussanak – **kockázatalapú priorizálás**.
3. **Ismétlődő tesztadatok**: adatvezérelt (DDT) átalakítás ekvivalenciaosztályokkal és határértékekkel; adatgenerálás – kevesebb, de több hibát felfedő adat.
4. **Hibagyakori, de alulfedett modulok**: hibasűrűség × lefedettség térkép → új tesztek ezekre (hibacsoportosulás elve).
5. **Változásalapú tesztkiválasztás** (test impact analysis): a commit által érintett modulok tesztjei fussanak először / CI-ban; a teljes készlet éjjel.
6. **Flaky tesztek** karanténba, gyökérok-elemzéssel.
7. A mérőszámok **dashboardon** (futásidő, hibafelfedési arány, instabilitás) – a döntések ezekre épüljenek, és a hatásukat is mérni kell.

### XII. feladat – lassú, instabil, nehezen karbantartható Selenium TAS fejlesztése

| Tünet | Valószínű ok (TAA/TAS) | Javaslat |
|---|---|---|
| 90 perces regresszió | soros futás, minden teszt UI-n át készít elő, fix `sleep`-ek | párhuzamosítás (Selenium Grid / konténerek), előkészítés API-n át, explicit várakozás, tesztpiramis: logika le az API/komponens szintre |
| 15–20% véletlenszerű bukás | instabil lokátorok, versenyhelyzetek, megosztott tesztadat, környezeti instabilitás | stabil `data-testid` lokátorok, tesztizoláció (saját adat tesztenként), preflight környezet-ellenőrzés, flaky-karantén és gyökérok-elemzés, újrafuttatás csak jelöléssel (nem eltakarva) |
| Nehezen olvasható, hiányos logok | nincs egységes naplózás | strukturált naplózás (lépés, időzítés, korrelációs ID), képernyőkép + DOM + böngészőnapló bukáskor, tesztállapot-osztályozás (Passed/Failed/TAS error/Inconclusive), riportkészítő (pl. Allure) |
| Új tesztek lassan készülnek, bonyolult, ismétlődő kód | nincs rétegezés, copy-paste | TAF rétegek (szkript → üzleti → core), POM + Facade, közös könyvtárak, DDT/KDT, kódolási konvenciók, kódáttekintés, statikus elemzés |

Bevezetés: a változtatásokat fokozatosan, mérve (futásidő, instabilitási arány előtte/utána), a TAS saját regressziójával.

### XIII. feladat – a tesztver átstrukturálása az új UI-hoz és API-réteghez (TAA szinten)

1. **Hatáselemzés**: az elbukott 40% csoportosítása ok szerint (lokátor, folyamatváltozás, új validáció); a régi és új UI/API összevetése; mely tesztek maradnak érvényesek.
2. **Architektúra (TAA) frissítése**: új **API-adapter** a core rétegben az új API-réteghez; a tesztek súlypontját API-szintre helyezni (gyorsabb, stabilabb), UI-szinten csak a valódi felhasználói folyamatok maradjanak.
3. **Üzleti réteg** változatlan interfésszel: a `Steps`/Facade metódusok neve és jelentése marad, csak a megvalósításuk vált az új UI/API-ra → a tesztszkriptek nagy része érintetlen.
4. **Page Objectek újraírása** az új UI-hoz, stabil azonosítókkal (`data-testid` megállapodás a fejlesztőkkel – tesztelhetőség); közös komponens-objektumok (űrlapmező, lista, gomb) a duplikáció ellen.
5. **Tesztadat és orákulumok** frissítése az új validációs szabályokhoz, verziózva (lásd 09. lépés, `UsernameRules`); szerződéstesztek az új API-ra.
6. **Elnevezési konvenciók és szabványok** egységesítése, a meglévő szkriptek értékelése: amelyik már nem ad értéket, törlendő.
7. **Párhuzamos átállás**: a régi és új készlet egy ideig együtt fut (feature flag / SUT-verzió szerinti tesztkészlet-választás – V. fejezet: „verzió és funkciókapcsoló alapján dinamikus futtatás”), a TAS és a SUT együtt verziózva.
8. **Ellenőrzés és dokumentáció**: a TAS regressziója (önteszt), CI-ba integrálás, a változások dokumentálása, a tanulságok beépítése (pl. UI-változás előtti értesítés a TAE-knek).

## Mi változott az előző lépéshez képest?

- `pom.xml`: `build-helper-maven-plugin` (portfoglalás), `spring-boot-maven-plugin` `start`/`stop`, `maven-failsafe-plugin`, Surefire `skipTests` kapcsoló, `build-gate` és `deploy-gate` profilok.
- Új: `.github/workflows/ci.yml` (build-kapu, telepítési kapu, éjszakai regresszió).
- Új tesztek: `systemtests/AbstractSystemIT`, `SmokeIT`, `CartFlowIT` (Failsafe, Spring nélkül, külső SUT ellen); `taf/TafSelfCheckTest` (TAF erőforrások ellenőrzése a build elején).
- `Config`: fel nem oldott Maven placeholder (`${env.…}`) = nincs megadva → érthető hibaüzenet.
- Összesen 160 build-szakasz teszt (1 `@Disabled`) + 5 rendszerteszt.

## Fájlok

- `.github/workflows/ci.yml` – a pipeline.
- `pom.xml` – Surefire/Failsafe szétválasztás, helyi SUT indítás, profilok.
- `src/test/java/.../systemtests/` – telepítés utáni rendszertesztek.
- `src/test/java/.../taf/TafSelfCheckTest.java` – TAF/TAS konfigurációs teszt.

## Futtatás

Teljes helyi ellenőrzés (unit tesztek, majd a csomagolt alkalmazás indítása és az *IT-k):

```bash
mvn -B -q verify
```

Csak a build-kapu:

```bash
mvn -B verify -Pbuild-gate
```

A telepítési kapu helyben – egy terminálban a „telepítés”:

```bash
mvn -B -q package -DskipTests
java -jar target/10-ci-pipeline-1.0.0-SNAPSHOT.jar --server.port=8080
```

Egy másikban a rendszertesztek:

```bash
WEBSHOP_BASE_URL=http://localhost:8080 mvn -B verify -Pdeploy-gate
```

Riportok: `target/surefire-reports/`, `target/failsafe-reports/`, `target/taf-results.csv`, `target/taf-results-it.csv`.
