# 09 – Konfiguráció, naplózás, hibaelemzés

> Tananyag: *Tesztautomatizálás* (ISTQB CTAL-TAE), V. fejezet (tesztver konfiguráció), VI. fejezet (adatgyűjtés, naplózás, tesztállapotok, TAS/SUT alapú elemzés), VII. fejezet (a tesztautomatizálási környezet ellenőrzése), **IX. és X. feladat** – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- A TAF kapjon **konfigurációt** környezeti változóból / system property-ből, alapértelmezésekkel.
- Futtatás előtt **előzetes ellenőrzés** (preflight) a SUT health végpontján – X. feladat A/B.
- **Strukturált naplózás** a kérésekről/válaszokról (REST Assured szűrő), **korrelációs azonosítóval**, ami a SUT naplójában is megjelenik.
- JUnit kiterjesztés, amely az eredményeket a tananyag négy **tesztállapotába** sorolja: Passed / Failed / TAS error / Inconclusive.
- A **X. feladat** forgatókönyve: egy teszt a régi felhasználónév-szabályt várja és elbukik; elemezzük, hogy a TAS vagy a SUT hibás, és javítsuk.

## Mit mutat be?

### 1. Konfiguráció (`taf/core/Config`)

| Beállítás | System property | Környezeti változó | Alapértelmezés |
|---|---|---|---|
| SUT címe | `webshop.baseUrl` | `WEBSHOP_BASE_URL` | a teszt által indított SUT (`http://localhost:<véletlen port>`) |
| Kérés időkorlát (ms) | `webshop.timeoutMs` | `WEBSHOP_TIMEOUT_MS` | `5000` |
| Törzsek naplózása sikernél is | `webshop.logBodies` | `WEBSHOP_LOG_BODIES` | `false` (hibánál mindig naplóz) |
| Preflight be/ki | `webshop.preflight` | `WEBSHOP_PREFLIGHT` | `true` |
| Eredményfájl | `webshop.resultsFile` | – | `target/taf-results.csv` |

Sorrend: system property → környezeti változó → alapértelmezés. A hibás konfiguráció (hiányzó/érvénytelen URL) **betöltéskor**, érthető üzenettel bukik (`ConfigTest`). A SUT oldalon a PayPal címe ugyanígy állítható (`PAYPAL_BASE_URL`).

Ha `WEBSHOP_BASE_URL` meg van adva, a tesztszkriptek a **külső** (pl. staging) SUT-ot hívják; a 10. lépés `*IT` tesztjei így futnak a telepítés után.

### 2. Preflight – a X. feladat A része automatizálva (`taf/core/PreflightCheck`)

`AbstractShopTest` `@BeforeAll` metódusa (`PER_CLASS` életciklus, így látja a portot):

1. betölti és naplózza a konfigurációt (`event=TAS_CONFIG ...`);
2. ellenőrzi: `GET /actuator/health` → `{"status":"UP"}`, és a termékkatalógus nem üres (tesztadat-előfeltétel);
3. ha valami nem jó, a `@BeforeEach` minden tesztet megszakít (`Assumptions.assumeTrue`) → a tesztek **Inconclusive** állapotba kerülnek (Surefire: *skipped*), nem „pirosak” – így egy leállt környezet nem látszik SUT-hibának (VI. fejezet: hamis hibák elkerülése).

Az eredmény környezetenként gyorsítótárazott (egy futásban egyszer fut). Saját tesztje WireMock-kal szimulál UP / DOWN / elérhetetlen / üres katalógus állapotot (`PreflightCheckTest`).

### 3. Naplózás és korrelációs ID

- **TAS oldal** – `StructuredLoggingFilter` (REST Assured `Filter`): minden kéréshez `X-Correlation-Id` fejlécet tesz, és egysoros, kulcs=érték naplót ír:
  `event=HTTP_CALL method=POST path=/validate-username status=200 durationMs=12 correlationId=tas-35e9…`
  Hibás válasznál (≥ 400) a kérés és a válasz törzse is bekerül.
- **SUT oldal** – `CorrelationIdFilter` (servlet szűrő): átveszi az azonosítót, MDC-be teszi (a naplóminta `[cid:…]` része), visszaküldi, és naplózza a kérést; a `UsernameController` **auditnaplót** ír a tananyag formátumában: `event=USERNAME_VALIDATED, input=admin12, result=success`.
- Egy elbukott teszt naplósorából a korrelációs ID-vel a SUT összes kapcsolódó naplósora visszakereshető (`grep tas-35e9…`).

### 4. Eredmény-osztályozás (`taf/core/ResultClassifierExtension`, `TestOutcome`)

| Állapot | Mikor? | Mit jelent? |
|---|---|---|
| **Passed** | nincs kivétel | minden ellenőrzés teljesült |
| **Failed** | `AssertionError` (JUnit, AssertJ, Hamcrest, REST Assured) | egy elvárás nem teljesült – *feltételezett* SUT-hiba |
| **TAS error** | bármely más kivétel (NPE, hálózati hiba, rossz konfiguráció, ismeretlen kulcsszó) | a teszt el sem jutott az ellenőrzésig – a TAS/környezet hibája |
| **Inconclusive** | megszakított teszt (`Assumptions`, preflight) | az előfeltétel nem teljesült, az eredmény nem eldönthető |

Kimenet: tesztenkénti naplósor (bukásnál az ok és az **utolsó HTTP hívás** korrelációs ID-vel), osztályonkénti összesítés (`event=TEST_SUMMARY ... Passed=4 Failed=0 TAS_error=0 Inconclusive=0`), és CSV sor a `target/taf-results.csv` fájlba (trendelemzéshez; `mvn clean` nélkül futásról futásra gyűlik). A kiterjesztést a JUnit Platform TestKit-tel teszteljük: egy mintaosztály négy tesztje a négy állapotba kerül (`ResultClassifierExtensionTest`).

> Fontos: az automatikus osztályozás csak **első szűrés**. A X. feladat épp azt mutatja, hogy egy *Failed* (assertion) eredmény mögött is lehet TAS-hiba – ezt csak az elemzés dönti el.

## X. feladat – TAS vagy SUT? Elemzés és javítás

A tananyag táblázata (47. dia) az elbukott teszthez gyűjtött adatokat összegzi:

| Oldal | Adatgyűjtési módszer | Eredmény | Mire utal? |
|---|---|---|---|
| TAS | Tesztlog | Step 2: Assertion failed – „admin12” should be invalid | a teszt elvárása: érvénytelen |
| TAS | Screenshot | a UI „Felhasználónév elfogadva” üzenetet mutat | a SUT elfogadta – a TAS ezt helyesen rögzítette |
| TAS | Lokátor-definíciók | a validációs üzenet a megfelelő UI-elemhez tartozik | **nem** lokátorhiba – a TAS a jó elemet olvasta |
| TAS | Teszteset metaadatok | „csak kisbetű és szám, min. 8 karakter” – **egy korábbi szabály!** | ⚠ az elvárás forrása elavult |
| TAS | Tesztparaméterek | a teszt az „admin12” nevet hibásnak várta, de a rendszer elfogadta | a régi szabályból levezetett elvárás |
| SUT | UI-interakció naplózás | „Valid username” visszajelzés | a SUT elfogad |
| SUT | Szervernapló | `POST /validate-username → status=valid` | a SUT elfogad |
| SUT | Frontend konzolnapló | „minimum 6 karakter, betű vagy szám” | **ez az aktuális szabály** |
| SUT | API válasz | `{ "valid": true }` | a SUT elfogad |
| SUT | Auditnapló | `event=USERNAME_VALIDATED, input=admin12, result=success` | a SUT elfogad |

**Következtetés:**

1. A SUT minden adatforrása (UI, szerver, API, audit, konzol) **egybehangzó és konzisztens** az aktuális szabállyal: az `admin12` 7 karakter, csak betű és szám → az új szabály szerint érvényes. A SUT **helyesen** működik.
2. A TAS technikailag jól működött (jó elemet olvasott, a képernyőkép és a log egyezik), de a **tesztorákulum elavult**: a teszteset metaadata a régi (v1.0) szabályt tartalmazza, amit a követelményváltozáskor nem frissítettek.
3. Ez tehát **TAS-hiba (hamis pozitív)** – a JUnit szintjén „Failed”-nek látszik (assertion bukott), de az elemzés után átsorolandó. Hibajegyet nem a fejlesztőknek, hanem a tesztautomatizálási csapatnak kell nyitni.

**Javítás a kódban (`UsernameRuleRegressionTest`):**

- `admin12ShouldBeInvalid_OLD_RULE` – az eredeti teszt, **`@Disabled`** indoklással (a build zöld marad, de a története megmarad).
- `admin12IsAcceptedByCurrentRule` – a javított teszt: az elvárás nem beégetett, hanem a verziózott orákulumból jön (`UsernameRules.CURRENT`, forrásmegjelöléssel).
- `sutFollowsCurrentRuleWhereRulesDiffer` – regressziós háló azokra a bemenetekre, ahol a régi és az új szabály eltér (`admin12`, `abcdef`, `Admin123`, `ABCDEFGH`, ...).

**Folyamatjavítás** (hogy ne forduljon elő újra): a tesztesetek metaadatában a követelmény azonosítója és verziója; követelményváltozáskor az érintett tesztek automatikus listázása (nyomonkövethetőség); a tesztorákulum egy helyen (itt: `UsernameRules`); kódáttekintés a tesztkódra is.

**Kipróbálás** – az eredeti, elavult teszt bekapcsolása (a build szándékosan bukik):

```bash
mvn test -Dtest=UsernameRuleRegressionTest -Djunit.jupiter.conditions.deactivate='org.junit.*DisabledCondition'
```

A naplóban:

```text
[cid:...] AUDIT     : event=USERNAME_VALIDATED, input=admin12, result=success
[cid:] TAS.HTTP     : event=HTTP_CALL method=POST path=/validate-username status=200 durationMs=12 correlationId=tas-35e9…
[cid:] TAS.RESULT   : event=TEST_RESULT outcome=FAILED test=UsernameRuleRegressionTest.admin12ShouldBeInvalid_OLD_RULE()
                      cause=AssertionFailedError: ["admin12" should be invalid] lastHttp=[... status=200 ... correlationId=tas-35e9…]
```

A korrelációs ID összeköti a TAS bukását a SUT auditsorával – pontosan az elemzéshez szükséges adat.

### X. feladat A – ellenőrző lista a futtatás előtt

| # | Ellenőrzés | Automatizálva? |
|---|---|---|
| 1 | A TAS verziója és függőségei rögzítettek (pom.xml, lock-olt verziók), a build lefordul | `mvn verify` (compile fázis) |
| 2 | A konfiguráció teljes és érvényes (SUT URL, időkorlátok, hitelesítő adatok forrása) | `Config.load` – hibás URL-nél azonnal bukik |
| 3 | A SUT elérhető és egészséges (health = UP) | `PreflightCheck` |
| 4 | A SUT verziója a tervezett (pl. `/actuator/info` build-verzió) | kézi / bővíthető |
| 5 | Külső függőségek (PayPal sandbox / mock) elérhetők | WireMock a tesztben; stagingen bővíthető |
| 6 | A tesztadat-előfeltételek teljesülnek (katalógus nem üres, tesztfelhasználók léteznek) | `PreflightCheck` (katalógus) |
| 7 | A tesztorákulumok/metaadatok az aktuális követelményverzióra mutatnak | `UsernameRules` verzió + review |
| 8 | Hálózat, tűzfal, jogosultságok rendben (TAS → SUT) | preflight kapcsolódási hibát jelez |
| 9 | Erőforrások (CPU, RAM, lemez) elegendők | CI runner monitorozás |
| 10 | A naplózás és a riportkimenet működik (írható `target/`, korrelációs ID megjelenik) | `ObservabilityTest` |

### X. feladat B – terv a telepítés és konfiguráció ellenőrzésére projektinduláskor

1. **Infrastruktúra kódként**: a TAS telepítése szkriptelt és verziózott (Maven Wrapper, `pom.xml`, CI workflow – 10. lépés); kézi lépés nincs, minden környezetben ugyanaz.
2. **Konfiguráció egy helyen**: környezetenkénti értékek környezeti változókban / CI titkokban, dokumentált kulcsokkal (fenti táblázat); titok soha a repóban (VII. fejezet: statikus elemzés – „plaintext jelszavak a tesztszkriptekben”).
3. **Önteszt (smoke) a TAF-ra**: a TAF saját komponenseinek tesztjei (`ConfigTest`, `PreflightCheckTest`, `ResultClassifierExtensionTest`, `JsonShapeMatcherTest`) minden build első lépései közt futnak.
4. **Preflight minden futás előtt**: SUT health, verzió, tesztadat; sikertelenség → Inconclusive, és értesítés az üzemeltetésnek, nem a fejlesztőknek.
5. **Mintafutás (pilot)**: egy ismert állapotú SUT-verzió ellen a teljes készlet lefut; az eredmény a referencia (baseline) a későbbi trendekhez.
6. **Dokumentáció és átadás**: README (futtatás, konfiguráció, hibaelemzési útmutató), felelősök kijelölése; a konfiguráció változásai kódáttekintésen mennek át.

### IX. feladat – mit vizsgálnánk egy elbukott mobil UI-tesztnél? (3–3 módszer)

| TAS oldal | SUT oldal |
|---|---|
| 1. Tesztlog (lépések, időzítések, assertion üzenet, stack trace) | 1. Alkalmazás- és szervernapló (API hívás, válasz, hibák) – korrelációs ID-vel |
| 2. Képernyőkép / videó a bukás pillanatáról | 2. API válasz naplózás (pl. a bejelentkezés utáni felhasználó-lekérdezés JSON-ja) |
| 3. Lokátor-definíciók, tesztparaméterek, teszteset-metaadatok, az emulátor és a driver (Appium) naplója/verziója | 3. Auditnapló / felhasználói események (sikeres bejelentkezés megtörtént-e), eszköznapló (logcat) |

(A X. feladat táblázata pontosan ezeknek a módszereknek az eredménye.)

## Mi változott az előző lépéshez képest?

- `pom.xml`: `spring-boot-starter-actuator` (health végpont), `junit-platform-testkit` (teszt).
- SUT: új `web/CorrelationIdFilter`; `UsernameController` auditnaplót ír; `application.properties`: korrelációs naplóminta, health kitettség.
- TAF core: `Config` átírva (property → env → alapértelmezés, `baseUrl`, timeout, külső SUT), `ApiClient` időkorláttal és naplószűrővel; új `StructuredLoggingFilter`, `PreflightCheck`, `TestOutcome`, `ResultClassifierExtension`.
- TAF business: új `UsernameRules` (verziózott tesztorákulum).
- Tesztek: `AbstractShopTest` (`PER_CLASS`, `@BeforeAll` preflight, eredmény-osztályozó); új `UsernameRuleRegressionTest` (X. feladat), `ObservabilityTest`, `ConfigTest`, `PreflightCheckTest`, `ResultClassifierExtensionTest`. Összesen 147 teszt (1 szándékosan `@Disabled`).

## Fájlok

- `src/main/java/.../web/CorrelationIdFilter.java` – SUT oldali korrelációs ID és kérésnapló.
- `src/test/java/.../taf/core/Config.java`, `PreflightCheck.java`, `StructuredLoggingFilter.java`, `TestOutcome.java`, `ResultClassifierExtension.java` – az új TAF képességek.
- `src/test/java/.../taf/business/UsernameRules.java` – a szabályverziók.
- `src/test/java/.../tests/UsernameRuleRegressionTest.java` – a X. feladat tesztjei.

## Futtatás

```bash
mvn -B -q verify
```

Részletes naplóval, a sikeres kérések törzsével együtt:

```bash
mvn test -Dwebshop.logBodies=true
```

Külső (már futó) SUT ellen – pl. egy másik terminálban `mvn spring-boot:run` után:

```bash
WEBSHOP_BASE_URL=http://localhost:8080 mvn test -Dtest='**/tests/UsernameTest,**/tests/CartTest'
```

Preflight kipróbálása elérhetetlen SUT-tal (a 3 teszt *Inconclusive* / skipped lesz, a naplóban `Inconclusive=3`, a build nem bukik el):

```bash
mvn test -Dtest='hu.learning.webshop.tesztautomatizalas.tests.ProductTest' -Dwebshop.baseUrl=http://localhost:1
```

## Továbblépés

A [10-ci-pipeline](../10-ci-pipeline/) lépés a teszteket CI/CD pipeline-ba szervezi: build-szakasz (unit, komponens) és telepítés utáni rendszertesztek külön minőségkapuval.
