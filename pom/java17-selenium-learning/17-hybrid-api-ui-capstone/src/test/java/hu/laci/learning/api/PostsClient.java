package hu.laci.learning.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * API kliens a /posts erőforráshoz.
 * <p>
 * A kapituláris feladatban az API kliens és a UI Page Object egymás mellett dolgozik:
 * az API hoz létre és ellenőriz adatokat, a UI a valódi felhasználói élményt teszteli.
 */
public class PostsClient {

    private static final String BASE_URI = "https://jsonplaceholder.typicode.com";

    private final RequestSpecification spec;

    public PostsClient() {
        this.spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    /** Lekéri az összes bejegyzés darabszámát. */
    public int countPosts() {
        return given().spec(spec)
                .when().get("/posts")
                .then().statusCode(200)
                .extract().jsonPath().getList("$").size();
    }

    /** Lekéri egy bejegyzés adatait Java objektumként. */
    public Post getPost(int id) {
        return given().spec(spec)
                .when().get("/posts/{id}", id)
                .then().statusCode(200)
                .extract().as(Post.class);
    }

    /** Létrehoz egy új bejegyzést, és visszaadja a szerver válaszában szereplő adatokat. */
    public Post createPost(Post post) {
        Response response = given().spec(spec).body(post)
                .when().post("/posts")
                .then().statusCode(201)
                .extract().response();
        return response.as(Post.class);
    }
}
