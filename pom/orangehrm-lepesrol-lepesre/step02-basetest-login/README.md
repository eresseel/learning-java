# 2. lépés – közös ősosztály (BaseTest) és a bejelentkezési tesztek

## Cél
A böngésző életciklusát kivinni a tesztekből, és megírni a vizsgakövetelmény **2. pontját** (bejelentkezés: egy pozitív és két negatív eset).

## Mi változott az 1. lépéshez képest
- **Új:** `BaseTest.java`: `@BeforeEach` elindítja, `@AfterEach` bezárja a böngészőt.
- **Új:** `Test02_LoginTest.java` (3 teszt), a `FirstLoginTest` helyett (törölve).

## Új fogalmak
- **`@BeforeEach` / `@AfterEach`**: JUnit 5 életciklus-metódusok. Minden tesztmetódus előtt/után lefutnak. A szülőosztály `@BeforeEach`-e mindig előbb fut, mint a gyerekosztályé.
- **Öröklés** (`extends BaseTest`): a teszt megkapja a `driver` mezőt.
- **Függetlenség**: minden teszt új böngészőt kap, ezért nincs megosztott állapot (süti, session). Ára: lassabb futás.
- **`@DisplayName`**: olvasható név a tesztjelentésben.
- **Negatív teszt**: azt igazolja, hogy a hibás adat el lesz utasítva (itt: "Invalid credentials").

## Hogyan működik
A `Test02_LoginTest` három tesztje ugyanazt a `login(user, pass)` segédmetódust használja:

| Teszt | Adatok | Elvárt eredmény |
|---|---|---|
| `testSikeresBejelentkezes` | `Admin` / `admin123` | az URL tartalmazza: `dashboard` |
| `testSikertelenBejelentkezesHibasJelszoval` | `Admin` / `hibasJelszo123` | "Invalid credentials" |
| `testSikertelenBejelentkezesHibasFelhasznalonevvel` | `RosszAdmin` / `admin123` | "Invalid credentials" |

A hibaüzenet lokátora két mintát fogad el (`|` = vagy), mert az OrangeHRM DOM-ja verziónként eltérhet.

## Még mindig gyenge pont
A lokátorok és a login-lépések a tesztben vannak. Ha a login oldal megváltozik, minden tesztben javítani kell. Ezt oldja meg a 3. lépés.

## Futtatás

```bash
cd step02-basetest-login
mvn clean test
```

(JDK 17+, Google Chrome és internet kell. A Maven Wrapper csak a 10. lépéstől van a mappában; addig `mvn` kell, vagy másold át a 10. lépésből az `mvnw`, `mvnw.cmd` és `.mvn/` elemeket.)

## Kész, ha
JUnit: 3 sikeres teszt.

## A védéshez
„Azért vezettem be a BaseTest-et, hogy a böngésző kezelése egy helyen legyen, és minden teszt tiszta lappal induljon.”
