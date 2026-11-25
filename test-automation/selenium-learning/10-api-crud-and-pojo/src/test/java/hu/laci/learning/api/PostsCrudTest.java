package hu.laci.learning.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CRUD műveletek tesztelése: Create (POST), Read (GET), Update (PUT/PATCH), Delete (DELETE).
 * <p>
 * Fontos: a JSONPlaceholder csak SZIMULÁLJA az írást. A POST 201-et és új id-t ad vissza,
 * de az adat valójában nem mentődik el, ezért a létrehozott elem később nem kérhető le.
 * Éles rendszeren ilyenkor a létrehozás után egy GET-tel is ellenőriznénk az adatot.
 */
class PostsCrudTest {

    private final PostsClient client = new PostsClient();

    // READ: a válasz törzsét a Post record-ba olvassuk vissza.
    @Test
    void shouldReadPostAsObject() {
        Response response = client.getPost(1);

        response.then().statusCode(200);

        // as(Post.class): a JSON válasz automatikus átalakítása Java objektummá.
        Post post = response.as(Post.class);
        assertEquals(1, post.id());
        assertEquals(1, post.userId());
    }

    // CREATE: új bejegyzés küldése. Sikeres létrehozás után 201 Created a várt státusz.
    @Test
    void shouldCreatePost() {
        // Létrehozáskor az id még null: a szerver osztja ki.
        Post newPost = new Post(1, null, "My title", "My body");

        Response response = client.createPost(newPost);

        response.then().statusCode(201);

        Post created = response.as(Post.class);
        // A JSONPlaceholder mindig 101-es id-t ad (a 100 meglévő után).
        assertEquals(101, created.id());
        // A visszaküldött adat egyezzen azzal, amit elküldtünk.
        assertEquals(newPost.title(), created.title());
        assertEquals(newPost.body(), created.body());
    }

    // UPDATE (PUT): a teljes erőforrás cseréje.
    @Test
    void shouldReplacePostWithPut() {
        Post replacement = new Post(1, 1, "Updated title", "Updated body");

        client.updatePost(1, replacement)
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated title"))
                .body("body", equalTo("Updated body"));
    }

    // UPDATE (PATCH): csak egy mező módosítása.
    @Test
    void shouldPatchOnlyTheTitle() {
        client.patchTitle(1, "Patched title")
                .then()
                .statusCode(200)
                .body("title", equalTo("Patched title"))
                // A nem küldött mezők megmaradnak az eredeti értéken.
                .body("id", equalTo(1));
    }

    // DELETE: a törlés 200 OK-t ad (a JSONPlaceholder üres JSON objektummal válaszol).
    @Test
    void shouldDeletePost() {
        client.deletePost(1)
                .then()
                .statusCode(200);
    }
}
