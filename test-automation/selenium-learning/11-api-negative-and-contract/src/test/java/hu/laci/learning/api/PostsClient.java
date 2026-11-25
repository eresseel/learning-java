package hu.laci.learning.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * API kliens a /posts erőforráshoz (a 10. lépés kliensének rövidített változata,
 * kiegészítve egy tetszőleges útvonalat hívó metódussal a negatív teszteléshez).
 */
public class PostsClient {

    private static final String BASE_URI = "https://jsonplaceholder.typicode.com";

    private final RequestSpecification spec;

    public PostsClient() {
        this.spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setAccept(ContentType.JSON)
                .build();
    }

    /** GET /posts/{id}: egy bejegyzés lekérése. */
    public Response getPost(int id) {
        return given().spec(spec).when().get("/posts/{id}", id);
    }

    /** GET egy tetszőleges útvonalra: hibás vagy nem létező végpontok teszteléséhez. */
    public Response getRaw(String path) {
        return given().spec(spec).when().get(path);
    }
}
