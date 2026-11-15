package hu.laci.learning.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * API kliens a /posts erőforráshoz. Ez a Page Object Model API-s megfelelője:
 * a végpontok útvonalait és a kérések összeállítását egy osztályba zárja, így a tesztek
 * csak azt írják le, MIT akarnak csinálni (pl. "hozz létre egy bejegyzést"),
 * azt nem, hogy HOGYAN épül fel a HTTP kérés.
 * <p>
 * Az osztály nem ellenőriz semmit: a Response objektumot adja vissza, az assertion a teszt dolga.
 */
public class PostsClient {

    private static final String BASE_URI = "https://jsonplaceholder.typicode.com";

    // A RequestSpecification a közös kérés-beállítások (alap URL, formátum) újrafelhasználható csomagja.
    // Ez a globális RestAssured.baseURI beállítás tisztább alternatívája.
    private final RequestSpecification spec;

    public PostsClient() {
        this.spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                // A kérés törzse JSON, és JSON választ kérünk.
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    /** GET /posts/{id}: egy bejegyzés lekérése. */
    public Response getPost(int id) {
        // A {id} helykitöltőt a REST Assured behelyettesíti az átadott értékkel.
        return given().spec(spec).when().get("/posts/{id}", id);
    }

    /** POST /posts: új bejegyzés létrehozása. */
    public Response createPost(Post post) {
        // A body(Object) a Post objektumot automatikusan JSON-ná alakítja (Jackson).
        return given().spec(spec).body(post).when().post("/posts");
    }

    /** PUT /posts/{id}: a teljes bejegyzés lecserélése. */
    public Response updatePost(int id, Post post) {
        return given().spec(spec).body(post).when().put("/posts/{id}", id);
    }

    /** PATCH /posts/{id}: a bejegyzés részleges módosítása (csak a cím). */
    public Response patchTitle(int id, String newTitle) {
        // Részleges módosításnál elég egy egyszerű Map: csak a változtatandó mezőt küldjük.
        return given().spec(spec).body(Map.of("title", newTitle)).when().patch("/posts/{id}", id);
    }

    /** DELETE /posts/{id}: a bejegyzés törlése. */
    public Response deletePost(int id) {
        return given().spec(spec).when().delete("/posts/{id}", id);
    }
}
