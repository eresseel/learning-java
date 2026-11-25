# 07 – Adatvezérelt és kulcsszóvezérelt tesztek

> Tananyag: *Tesztautomatizálás* (ISTQB CTAL-TAE), III. fejezet (tesztautomatizálási megközelítések: lineáris, strukturált, adatvezérelt, kulcsszóvezérelt), **IV. feladat** (megközelítések a kosár és fizetés folyamathoz) – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Készítsünk **adatvezérelt** API-teszteket: a tesztadatok CSV fájlokban vannak (`@CsvFileSource`), a tesztkód egyetlen strukturált szkript.
- Készítsünk egy kicsi **kulcsszóvezérelt** futtatót: a forgatókönyvek táblázatok (`ADD_TO_CART;P001;2`, `UPDATE_QTY;P001;5`, `CHECK_TOTAL;52940`), a futtató a kulcsszavakat API-hívásokra fordítja.
- IV. feladat: határozzunk meg különböző megközelítéseket a kosár és fizetés folyamat teszteléséhez (lásd a táblázatot).

## Mit mutat be?

- **Adatvezérelt tesztelés (DDT)** – `DataDrivenApiTest`:
  - `testdata/usernames.csv` – a felhasználónév-szabály 12 esete (az X. feladat `admin12`-je, határértékek, régi/új szabály eltérései);
  - `testdata/cart-quantities.csv` – mennyiségi határértékek és hibakódok;
  - `testdata/search.csv` – keresés találatszámmal.
  - Új eset = új sor a CSV-ben, kódváltozás nélkül. A `megjegyzes` oszlop a riportban megjelenő tesztnévbe kerül.
- **Kulcsszóvezérelt tesztelés (KDT)** – `keyword` csomag:
  - `KeywordScriptParser` – a `keywords/*.csv` táblázat beolvasása (megjegyzések, sorszámok);
  - `KeywordRunner` – kulcsszó → API-hívás leképezés; *akció* kulcsszavak (nem ellenőriznek) és *ellenőrző* kulcsszavak; hibánál fájl:sor pontosságú üzenet; minden lépés naplózva;
  - `KeywordDrivenTest` – három forgatókönyv lejátszása + a futtató saját hibakezelésének tesztje (ismeretlen kulcsszó = TAS-hiba).
  - A `vasarlas-fizetessel.csv` a teljes folyamatot fedi le: keresés → kosár → elutasított fizetés → sikeres fizetés; a PayPal viselkedését a `PAYPAL_RESPONDS` kulcsszó állítja (WireMock).

### Kulcsszavak

| Kulcsszó | Argumentumok | Jelentés |
|---|---|---|
| `CLEAR_CART` | – | kosár ürítése |
| `ADD_TO_CART` | termék; db | termék kosárba |
| `UPDATE_QTY` | termék; db | mennyiség felülírása |
| `REMOVE_FROM_CART` | termék | tétel törlése |
| `SEARCH` | szöveg | termékkeresés |
| `PAYPAL_RESPONDS` | `COMPLETED` / `DECLINED` / `ERROR` | a szimulált PayPal következő válasza |
| `CHECKOUT` | – | fizetés |
| `EXPECT_STATUS` | HTTP kód | az utolsó akció státusza |
| `EXPECT_ERROR` | HTTP kód; errorCode | az utolsó akció hibás, adott hibakóddal |
| `CHECK_TOTAL` | összeg | a kosár végösszege |
| `CHECK_ITEM_COUNT` | db | darabok száma a kosárban |
| `CHECK_QTY` | termék; db | egy tétel mennyisége |
| `CHECK_RESULT_COUNT` | db | az utolsó keresés találatainak száma |

Példa (`keywords/kosar-alap.csv`):

```text
CLEAR_CART
ADD_TO_CART;P001;2
EXPECT_STATUS;201
UPDATE_QTY;P001;5
CHECK_QTY;P001;5
CHECK_TOTAL;39950
```

### IV. feladat – megközelítések a kosár és fizetés folyamathoz

| Megközelítés | Alkalmazás a webshopban | Hol látható a repóban? | Mikor éri meg? |
|---|---|---|---|
| Rögzítés/lejátszás | A böngészőben felvett kosár-folyamat visszajátszása (pl. Selenium IDE) | – (UI) | Gyors demo, ritkán változó UI; karbantartása nehéz |
| Lineáris szkriptelés | Egy tesztmetódus egymás után hívja a végpontokat, minden adat beégetve | 03. lépés `CartApiIntegrationTest.addUpdateGetRemoveFlow` | Néhány egyszeri ellenőrzés |
| Strukturált szkriptelés | Közös spec-ek, segédmetódusok, ősosztály | 04. lépés `AbstractApiTest`, 08. lépés TAF rétegek | Hosszú távú karbantarthatóság |
| Adatvezérelt (DDT) | Ugyanaz a kosár-teszt sok mennyiséggel/termékkel CSV-ből | **ez a lépés**: `DataDrivenApiTest` | Sok hasonló eset, határérték-elemzés |
| Kulcsszóvezérelt (KDT) | Üzleti lépések táblázatban (`ADD_TO_CART`, `CHECKOUT`) | **ez a lépés**: `KeywordRunner` | Üzleti elemzők is írnak teszteket; nagyobb rendszer |
| TDD | A kosárszabályokat előbb teszt, aztán kód | 01. lépés (validátor), 02. lépés (kosár) | Komponensszint, fejlesztők |
| BDD | „Adott egy 2 egeret tartalmazó kosár, amikor a PayPal elutasítja a fizetést, akkor a kosár megmarad” – Gherkin + Cucumber/Karate | – (a KDT forgatókönyv hasonló szerepet tölt be) | Közös nyelv üzlet és fejlesztés között |

## Mi változott az előző lépéshez képest?

- Új tesztadat: `src/test/resources/testdata/` (3 CSV), `src/test/resources/keywords/` (3 forgatókönyv).
- Új tesztek: `api/DataDrivenApiTest` (24 eset), `keyword/KeywordDrivenTest` (4) + `KeywordRunner`, `KeywordScriptParser`, `KeywordStep`. Összesen 133 teszt.
- A `pom.xml`-ben csak a leírás változott; az alkalmazás kódja változatlan.

## Fájlok

- `src/test/resources/testdata/*.csv` – DDT adatok (pontosvesszővel tagolt, fejléces).
- `src/test/resources/keywords/*.csv` – KDT forgatókönyvek.
- `src/test/java/.../api/DataDrivenApiTest.java` – adatvezérelt tesztek.
- `src/test/java/.../keyword/` – a kulcsszóvezérelt futtató és tesztje.

A tesztadatok saját készítésűek, az eredeti tesztadatbázis nem volt a tananyag része.

## Futtatás

```bash
mvn -B -q verify
```

```bash
mvn test -Dtest='DataDrivenApiTest,KeywordDrivenTest'
```

**Gyakorlás:** adj új sort a `testdata/cart-quantities.csv`-hez (pl. `P002;98;201;;majdnem max`), vagy írj új forgatókönyvet `keywords/sajat.csv` néven, és vedd fel a `KeywordDrivenTest` `@ValueSource` listájába.

## Továbblépés

A [08-taf-retegek-mintak](../08-taf-retegek-mintak/) lépés a teszteket TAF rétegekbe szervezi (tesztszkript → üzleti logika → alapkönyvtár).
