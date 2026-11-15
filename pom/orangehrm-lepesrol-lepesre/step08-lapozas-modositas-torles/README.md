# 8. lépés – lapozás, módosítás, törlés

## Cél
Vizsgakövetelmények **5., 8. és 9. pont**: többoldalas lista bejárása, meglévő adat módosítása, adat törlése.

## Mi változott
- **Módosult:** `PimPage.java`: új: `navigateToEmployeeList`, `traverseAllPagesAndCount`, `editFirstEmployee`, `deleteFirstEmployee`, `isDeleteSuccessful`.
- **Új:** `Test05_PaginationTest.java`, `Test08_EditEmployeeTest.java`, `Test09_DeleteFirstEmployeeTest.java`.

## Új fogalmak
### Lapozás (Test05)
- A `traverseAllPagesAndCount()` ciklusban: megvárja a táblázatot → megkeresi a Next gombot → ellenőrzi, hogy nem inaktív-e → rákattint → megvárja a loader eltűnését és az új sorokat → növeli a számlálót.
- **Inaktív gomb felismerése**: két jel is lehet: a `disabled` attribútum vagy a szülő elem `disabled` CSS osztálya.
- **Biztonsági korlát** (`pageCount > 100`): hibás lokátor esetén se fusson végtelenül a ciklus.
- A visszaadott érték az oldalak száma, amit a teszt naplóz.

### Módosítás (Test08)
- Az első sor ceruza ikonja (`bi-pencil-fill`) nyitja a szerkesztőt.
- A kód megvárja, hogy a mező **tényleg megkapja** az alkalmazott nevét, különben üres szöveget olvasna ki régi névként (race condition az aszinkron betöltéssel).
- **A mező tartalmának cseréje**: kijelölés (`Cmd+A` macOS-en, `Ctrl+A` máshol) + `Backspace` + új szöveg. A `clear()` ezen a mezőn nem mindig vált ki eseményt, ezért ez megbízhatóbb.
- A metódus a **régi nevet adja vissza**, a teszt ezt naplózza: `'Régi' -> 'ModifiedName'`.

### Törlés (Test09)
- Az első sor nevét **a törlés előtt** olvassa ki (utána a sor eltűnik a DOM-ból).
- Kuka ikon → megerősítő ablak → `Yes, Delete` gomb.
- A teszt a siker toast üzenetét ellenőrzi.

## Fontos őszinte megjegyzések
- A Test08 és a Test09 **a megosztott demo adatait módosítja és törli**, és csak az „első sort” használja. Ez dokumentált korlát: a teszt nem hermetikus.
- A Test05 assertion-je csak azt ellenőrzi, hogy a bejárt oldalak száma legalább 1. Ez gyenge: egy oldal bejárásakor is zöld lenne. Javítás: `> 1`, vagy az összes sor számának összevetése.
- A Test08 és Test09 csak a toast üzenetet nézi, nem olvassa vissza az eredményt. Jobb lenne a módosított név visszaolvasása, illetve a sor hiányának ellenőrzése.

## Futtatás

```bash
cd step08-lapozas-modositas-torles
mvn clean test
```

(JDK 17+, Google Chrome és internet kell. A Maven Wrapper csak a 10. lépéstől van a mappában; addig `mvn` kell, vagy másold át a 10. lépésből az `mvnw`, `mvnw.cmd` és `.mvn/` elemeket.)

## Kész, ha
A Test05, Test08, Test09 zöld. Egyenként: `mvn test -Dtest=Test05_PaginationTest`

## A védéshez
„A lapozásnál biztonsági korlátot tettem a ciklusba, a módosításnál a régi nevet visszaadom és naplózom. A gyenge assertionöket tudom, és megmondom, hogyan javítanám.”
