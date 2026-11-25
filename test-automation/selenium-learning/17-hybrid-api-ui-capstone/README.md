# 17 – Hibrid teszt: API + UI együtt (zárófeladat)

**Nehézség:** ★★★★★

## Cél
Az összes tanult technika összekapcsolása egy valósabb feladatban, ahol az API és a UI egymást kiegészítve dolgozik.

## Feladat
- Ellenőrizd a JSONPlaceholder adatait az API rétegen keresztül (`PostsClient`).
- Ellenőrizd a helyi HTML oldal megjelenítését és a szűrőjét a UI rétegen keresztül (`PostListPage`).
- Gondold végig a hibrid teszt mintáját: mi az API dolga, és mi a UI dolga a tesztelésben?

## Mit tanulunk?
- API és UI tesztelés kombinálása egyetlen projektben;
- mi a különbség az API és a UI ellenőrzés között, és mikor melyiket érdemes használni;
- az API teszt gyorsabb és stabilabb, de a megjelenítési hibát nem találja meg;
- a UI teszt lassabb, de azt is ellenőrzi, amit a felhasználó lát;
- a teljes tesztstratégia: nem mindent kell UI-on tesztelni, amit API-n is meg lehet.

## A "hibrid minta" leggyakoribb formái
1. **API → UI**: az API létrehozza az előfeltételt (pl. bejelentkeztet), a UI ellenőrzi a megjelenítést.
2. **UI → API**: a UI elvégzi a műveletet (pl. töröl egy elemet), az API ellenőrzi, hogy valóban eltűnt-e.
3. **Párhuzamos ellenőrzés**: ugyanazt az állapotot az API és a UI is ellenőrzi (eltérő szempontok alapján).

## Futtatás
```bash
cd 17-hybrid-api-ui-capstone
mvn clean test
```
Internetkapcsolat kell az API tesztekhez (JSONPlaceholder). Chrome kell a UI tesztekhez.

## Továbbfejlesztési feladat
1. Kapcsold be a párhuzamos futtatást (másold a 16. szint `junit-platform.properties` fájlját), és igazítsd a `BaseTest`-et a `DriverFactory`-ra.
2. Egészítsd ki a `PostsClient`-et `getAllPosts()` metódussal. Az API adja vissza a bejegyzések listáját, a UI oldal szimuláljon ugyanennyi elemet, és ellenőrizd, hogy a darabszámok egyeznek.
3. Adj hozzá hibakezelő tesztet: mi történik, ha a szűrő különleges karaktereket tartalmaz?
