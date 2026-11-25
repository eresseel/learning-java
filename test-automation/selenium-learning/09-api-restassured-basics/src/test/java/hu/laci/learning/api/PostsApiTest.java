package hu.laci.learning.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Az első API tesztek: GET kérések a nyilvános JSONPlaceholder szolgáltatás ellen.
 * <p>
 * A REST Assured "given - when - then" szerkezetet használ:
 * <ul>
 *   <li><b>given()</b>: a kérés előkészítése (paraméterek, fejlécek),</li>
 *   <li><b>when()</b>: a kérés elküldése (get, post, ...),</li>
 *   <li><b>then()</b>: a válasz ellenőrzése (státuszkód, fejléc, JSON mezők).</li>
 * </ul>
 */
class PostsApiTest {

    // @BeforeAll: az osztály összes tesztje előtt egyszer fut le (statikus metódus kell).
    @BeforeAll
    static void configureBaseUri() {
        // Globális alap URL: a tesztekben elég a "/posts/1" jellegű relatív útvonal.
        // Figyelem: ez a RestAssured statikus, GLOBÁLIS beállítása. Nagyobb projektben ezt
        // RequestSpecification-nel kerüljük el (lásd a 10. lépést).
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    // Egyetlen erőforrás lekérése és a legfontosabb elemek ellenőrzése.
    @Test
    void shouldReturnSinglePost() {
        given()
        .when()
                .get("/posts/1")
        .then()
                // HTTP státuszkód: 200 = OK.
                .statusCode(200)
                // A Content-Type fejléc JSON-t jelezzen (a charset részt nem vizsgáljuk).
                .contentType(containsString("application/json"))
                // A body("id", ...) a JSON "id" mezőjét ellenőrzi Hamcrest matcherrel.
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", not(emptyOrNullString()));
    }

    // Lista lekérése: a gyökér egy JSON tömb, a "size()" a tömb elemszámát adja.
    @Test
    void shouldReturnHundredPosts() {
        given()
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .body("size()", equalTo(100));
    }

    // Szűrés lekérdezési paraméterrel: GET /posts?userId=1
    @Test
    void shouldFilterPostsByUserId() {
        given()
                // A queryParam a "?userId=1" részt fűzi az URL végére.
                .queryParam("userId", 1)
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .body("size()", equalTo(10))
                // Az "userId" a lista MINDEN elemének userId mezőjét jelenti, az everyItem pedig
                // azt ellenőrzi, hogy mindegyik egyenlő 1-gyel.
                .body("userId", everyItem(equalTo(1)));
    }

    // Ha a válasz részleteit a saját Java kódunkban akarjuk vizsgálni, kinyerjük a Response objektumot.
    @Test
    void shouldExtractValuesFromResponse() {
        Response response = given()
                .when()
                .get("/posts/2");

        // A kinyert értékeket a megszokott JUnit assertionökkel ellenőrizzük.
        assertEquals(200, response.statusCode());
        assertTrue(response.header("Content-Type").contains("json"));

        // A jsonPath() a válasz törzséből olvas ki mezőket típus szerint.
        int userId = response.jsonPath().getInt("userId");
        String title = response.jsonPath().getString("title");

        assertEquals(1, userId);
        assertFalse(title.isBlank());
    }
}
