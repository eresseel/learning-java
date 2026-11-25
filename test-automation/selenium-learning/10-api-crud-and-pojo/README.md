# 10 – CRUD műveletek, POJO és API kliens osztály

**Nehézség:** ★★★☆☆

## Cél
A teljes CRUD ciklus (létrehozás, olvasás, módosítás, törlés) tesztelése, és az API tesztek átszervezése a Page Object Model mintájára.

## Feladat
- Készíts `Post` modellt (record), és küldj be belőle JSON-t `POST`-tal.
- Olvasd vissza a választ Java objektumba (`response.as(Post.class)`).
- Teszteld a `PUT`, a `PATCH` és a `DELETE` hívást.
- Vidd át a kérések összeállítását a `PostsClient` osztályba.

## Mit tanulunk?
- JSON szerializáció és deszerializáció Jackson-nel;
- `RequestSpecification`: közös beállítások újrafelhasználása a globális állapot helyett;
- API kliens osztály mint a POM megfelelője: a teszt csak a szándékot írja le;
- a PUT és PATCH különbsége (teljes csere és részleges módosítás);
- a tesztelt szolgáltatás korlátai (a JSONPlaceholder csak szimulálja az írást).

## Futtatás
```bash
cd 10-api-crud-and-pojo
mvn clean test
```

## Továbbfejlesztési feladat
1. Add hozzá a `PostsClient`-hez a `getAllPosts()` metódust, és tesztelj vele.
2. Írj egy `Comment` recordot, és olvasd be a `/posts/1/comments` listát `List<Comment>` típusként (`response.jsonPath().getList("", Comment.class)`).
3. Gondold végig: mit ellenőriznél a POST után egy valódi rendszeren, hogy biztos legyen, hogy az adat tényleg elmentődött?
