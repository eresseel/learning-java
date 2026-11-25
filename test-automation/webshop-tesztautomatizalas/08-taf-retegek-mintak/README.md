# 08 – TAF rétegek és tervezési minták

> Tananyag: *Tesztautomatizálás* (ISTQB CTAL-TAE), III. fejezet (TAF elemei és rétegei; tervezési alapelvek és minták: Facade, Singleton, POM), **III. feladat** (TAF rétegezés a belépési funkcióhoz), **V. feladat** (tervezési minták a termékkereséshez és kosárba helyezéshez) – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- A 04–07. lépésben gyűlt API-teszteket szervezzük át a tananyag szerinti **három TAF rétegbe**:
  1. **tesztszkriptek** – *mit* ellenőrzünk, üzleti nyelven;
  2. **üzleti logika** – a webshop fogalmai (kosár, termék, fizetés) → API-hívások;
  3. **alapkódkönyvtárak** – általános, üzleti tudás nélküli eszközök (HTTP kliens, konfiguráció, mock szerver, JSON-egyeztetés).
- Alkalmazzuk a **Facade** és a **Singleton** mintát; a README-ben válaszoljunk a III. és V. feladatra.

## Mit mutat be?

### A rétegek a kódban

```
src/test/java/hu/learning/webshop/tesztautomatizalas/
├── tests/                     1. TESZTSZKRIPTEK  (UsernameTest, CartTest, CheckoutTest, ProductTest,
│                                                  DataDrivenTest, KeywordDrivenTest, JsonSchemaTest,
│                                                  ContractVerificationTest, AbstractShopTest)
├── taf/business/              2. ÜZLETI LOGIKA   (ShopFacade, CartSteps, ProductSteps, UsernameSteps,
│                                                  CheckoutSteps, ProviderStates)
├── taf/keyword/                  KDT futtató az üzleti rétegre építve (KeywordRunner, parser)
├── taf/core/                  3. ALAPKÖNYVTÁRAK  (ApiClient, Config, PaypalSimulator, JsonShapeMatcher, Problem)
│
├── domain/, validation/          komponenstesztek (változatlanok – nem részei az API TAF-nak)
├── web/                          MockMvc komponensintegrációs tesztek (változatlanok)
└── payment/                      PaymentClient WireMock-teszt (változatlan)
```

**Függőségi szabály:** csak lefelé lehet hivatkozni (tests → business → core). A core semmit nem tud a webshopról; a tesztszkriptekben nincs URL, HTTP metódus vagy JSON.

| | Előtte (07. lépés) | Utána (08. lépés) |
|---|---|---|
| Egy kosár-teszt | `given().spec(api).body(new AddCartItemRequest("P001", 2)).when().post("/cart/items").then().statusCode(201)...` | `shop.cart().add("P001", 2)` |
| PayPal stub | `paypal.stubFor(post("/v2/checkout/orders").willReturn(aResponse().withStatus(422)))` | `PAYPAL.willDecline()` |
| Ha a `/cart/items` útvonal megváltozik | minden tesztfájlt módosítani kell | csak a `CartSteps`-et |
| Spring kontextusok száma az API-tesztekhez | 3 (eltérő WireMock-portok miatt) | 1 (közös `PaypalSimulator.shared()`) |

### Tervezési minták

- **Singleton** – `taf.core.ApiClient`: egyetlen, közös HTTP belépési pont (alap URL, port, JSON-beállítás, naplózás egy helyen). „Holder” idióma: lusta, szálbiztos. Hátránya (tudatosan vállalva): globális állapot – párhuzamos futtatásnál vagy több környezetnél óvatosan.
- **Facade** – `taf.business.ShopFacade`: egyetlen egyszerű belépési pont a négy lépésosztályhoz, és összetett folyamatok egy hívásban (`fillCart(...)`, `buy(...)`).
- **Adapter** (ráadás) – `taf.core.PaypalSimulator`: a WireMock API-t szándék-alapú metódusokra fordítja (`willComplete`, `willDecline`, `willRespondSlowly`); eszközcsere esetén csak ezt kell átírni.
- **POM (Page Object Model)** – UI-hoz való minta (oldalanként egy osztály a lokátorokkal és műveletekkel). API-szinten a megfelelője a végpontcsoportonkénti **Steps** osztály („Service Object”): `CartSteps` ≈ „kosár oldal”. A repó Selenium-példái (`test-automation/selenium-learning`, `orangehrm-lepesrol-lepesre`) mutatják a valódi POM-ot.

### SOLID a TAF-ban

| Elv | Hol? |
|---|---|
| Egyetlen felelősség | Minden Steps osztály egy végpontcsoport; a Facade csak összefog; a `Config` csak adatot tárol |
| Nyitott/zárt | Új kulcsszó vagy provider state hozzáadható a meglévő tesztek módosítása nélkül |
| Függőség-inverzió | A tesztszkriptek az üzleti absztrakciótól függenek, nem a REST Assured-től |
| Interfész-szegregáció | A tesztszkript csak a számára szükséges Steps-et használja (`shop.cart()`, `shop.products()`) |

## III. feladat – TAF rétegezés a belépési (bejelentkezési) funkcióhoz

| Réteg | Feladata | Webes belépés (UI) példa | A repóban (API) |
|---|---|---|---|
| Tesztszkriptek | Tesztesetek üzleti nyelven, adatokkal, ellenőrzésekkel | `loginWithValidUserShowsDashboard()`, `loginWithWrongPasswordShowsError()` | `UsernameTest.admin12IsAccepted()` |
| Üzleti logika | Funkcionális lépések, folyamatok | `LoginSteps.loginAs(user)`, `LoginPage` (POM): `enterUsername`, `enterPassword`, `submit`, `errorMessage()` | `UsernameSteps.validate(name)`, `ShopFacade` |
| Alapkódkönyvtárak | Eszközközeli, újrahasznosítható szolgáltatások | `DriverFactory` (Singleton WebDriver), várakozások, képernyőkép, `Config` (URL, böngésző), naplózás | `ApiClient` (Singleton), `Config`, `Problem` |

Ha a belépési űrlap egy mezője megváltozik, csak a `LoginPage` lokátora módosul; ha új böngésző kell, csak a `DriverFactory`; a tesztszkriptek változatlanok.

## V. feladat – tervezési minták a termékkereséshez és kosárba helyezéshez

| Minta | Szerep a folyamatban | Itt |
|---|---|---|
| Facade | `shop.fillCart(...)`, `shop.buy(...)` – keresés/kosár/fizetés egy egyszerű felületen | `ShopFacade` |
| Singleton | Közös HTTP kliens / WebDriver példány | `ApiClient` |
| POM / Service Object | `SearchPage`, `ProductPage`, `CartPage` (UI) – `ProductSteps`, `CartSteps` (API) | `ProductSteps`, `CartSteps` |
| Folyamat-alapú modell (FMP) | Az üzleti folyamat lépései (keres → kosárba tesz → fizet) önálló, újrafelhasználható blokkok | `vasarlas-fizetessel.csv` + `KeywordRunner` |
| Adapter | Külső szolgáltatás (PayPal) szimulációjának elrejtése | `PaypalSimulator` |

Alapelvek: absztrakció és egységbezárás (a Steps elrejti a HTTP-t), egyetlen felelősség, DRY (közös spec és hibakezelés egy helyen).

## Mi változott az előző lépéshez képest?

- A `src/test/java/.../api`, `contract`, `keyword` csomagok megszűntek; helyettük `tests/` (tesztszkriptek), `taf/business/`, `taf/keyword/`, `taf/core/`.
- Új: `ApiClient` (Singleton), `Config`, `PaypalSimulator` (közös WireMock szerver), `ShopFacade`, `*Steps`.
- `JsonShapeMatcher` és tesztje a core rétegbe, `ProviderStates` az üzleti rétegbe költözött (és a Facade-et használja).
- A `KeywordRunner` a Facade-re és a szimulátorra épül; a kulcsszavak és forgatókönyvek változatlanok.
- Az API-tesztek tartalma lényegében azonos, tömörebb formában (126 teszt összesen); egyetlen Spring kontextus az összes rendszerszintű teszthez.
- Az alkalmazás kódja és a tesztadatok változatlanok.

## Fájlok

- `src/test/java/.../taf/core/` – alapkönyvtárak.
- `src/test/java/.../taf/business/` – üzleti lépések és a Facade.
- `src/test/java/.../taf/keyword/` – kulcsszóvezérelt futtató.
- `src/test/java/.../tests/` – tesztszkriptek; `AbstractShopTest` a közös bekötés.

## Futtatás

```bash
mvn -B -q verify
```

Csak a tesztszkript-réteg:

```bash
mvn test -Dtest='hu.learning.webshop.tesztautomatizalas.tests.*Test'
```

## Továbblépés

A [09-konfiguracio-naplozas-hibaelemzes](../09-konfiguracio-naplozas-hibaelemzes/) lépés konfigurációt, előzetes környezet-ellenőrzést, naplózást és eredmény-osztályozást ad a TAF-hoz, és végigvezeti a X. feladat hibaelemzését.
