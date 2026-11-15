# REST Assured

**Dokumentáció:** <https://rest-assured.io/>  
**GitHub:** <https://github.com/rest-assured/rest-assured>

---

## `given() / when() / then()`

A REST Assured kérés-ellenőrzés három részre osztja a tesztet, hasonlóan az Arrange-Act-Assert mintához.

```java
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

given()
    .queryParam("userId", 1)       // lekérdezési paraméter (?userId=1)
    .header("Accept", "application/json")
.when()
    .get("/posts")                 // HTTP metódus és útvonal
.then()
    .statusCode(200)               // státuszkód ellenőrzése
    .contentType("application/json")
    .body("size()", equalTo(10))   // JSON törzs ellenőrzése Hamcrest-tel
    .body("userId", everyItem(equalTo(1)));
```

---

## `RequestSpecification` / `RequestSpecBuilder`

Közös kérés-beállítások (alap URL, formátum, fejlécek) újrafelhasználható csomagja. Ez a globális `RestAssured.baseURI` tisztább alternatívája, mert nem módosít globális állapotot.

```java
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

RequestSpecification spec = new RequestSpecBuilder()
        .setBaseUri("https://jsonplaceholder.typicode.com")
        .setContentType(ContentType.JSON)
        .setAccept(ContentType.JSON)
        .build();

// Minden kérésnél:
given().spec(spec).when().get("/posts/1").then().statusCode(200);
```

---

## `Response` és `jsonPath()`

Ha a then() helyett a válasz egész tartalmát szeretnéd Java kódban vizsgálni, a `extract().response()` visszaadja a `Response` objektumot.

```java
import io.restassured.response.Response;

Response response = given().spec(spec).when().get("/posts/1");

int statusCode = response.statusCode();
String contentType = response.header("Content-Type");

// Értékek kiolvasása típusosan:
int id = response.jsonPath().getInt("id");
String title = response.jsonPath().getString("title");

// Teljes JSON-t Java objektummá alakítani (Jackson szükséges):
Post post = response.as(Post.class);
```

---

## `matchesJsonSchemaInClasspath`

JSON séma validáció: ellenőrzi, hogy a válasz megfelel-e a sémának (kötelező mezők, típusok).

```java
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

given().spec(spec)
.when()
    .get("/posts/1")
.then()
    .statusCode(200)
    .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
```

A séma fájl (`src/test/resources/schemas/post-schema.json`):
```json
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "type": "object",
  "required": ["userId", "id", "title", "body"],
  "properties": {
    "userId": { "type": "integer" },
    "id":     { "type": "integer" },
    "title":  { "type": "string" },
    "body":   { "type": "string" }
  }
}
```

