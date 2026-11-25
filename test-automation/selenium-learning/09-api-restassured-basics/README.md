# 09 – API tesztelés alapjai REST Assured-del

**Nehézség:** ★★☆☆☆

## Cél
Az első HTTP API tesztek megírása. Nem böngészőt vezérlünk, hanem közvetlenül a szerver REST felületét hívjuk, és a válaszát ellenőrizzük.

## Feladat
- Kérj le egy erőforrást (`GET /posts/1`), és ellenőrizd a státuszkódot, a `Content-Type` fejlécet és a JSON mezőket.
- Kérd le a teljes listát, és ellenőrizd az elemszámot.
- Szűrj lekérdezési paraméterrel (`?userId=1`).
- Olvass ki értékeket a válaszból a `jsonPath()`-szal.

## Mit tanulunk?
- a REST Assured `given() / when() / then()` szerkezete;
- státuszkód, fejléc és JSON törzs ellenőrzése Hamcrest matcherekkel (`equalTo`, `everyItem`, `not`);
- a `Response` objektum és a `jsonPath()` használata;
- miért gyorsabb és stabilabb az API teszt, mint a UI teszt.

## Futtatás
```bash
cd 09-api-restassured-basics
mvn clean test
```
Böngésző nem kell, internetkapcsolat igen: a tesztek a nyilvános `https://jsonplaceholder.typicode.com` szolgáltatást hívják.

## Ha hibát kapsz
A nyilvános szolgáltatás néha lassú, vagy 403-mal elutasítja a kéréseket. Ilyenkor próbáld újra később, vagy ugorj a 12. lépésre, ahol a külső szolgáltatást helyi mock váltja ki, és a teszt offline is fut.

## Továbbfejlesztési feladat
1. Kérd le a `/users/1` erőforrást, és ellenőrizd az `address.city` beágyazott mezőt.
2. Kérd le a `/posts/1/comments` listát, és ellenőrizd, hogy minden elem `postId` mezője 1.
3. Írj tesztet a `/comments?postId=1` hívásra, és hasonlítsd össze az előző eredményével.
