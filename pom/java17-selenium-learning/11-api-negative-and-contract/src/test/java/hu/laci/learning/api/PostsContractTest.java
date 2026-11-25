package hu.laci.learning.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * "Szerződés" (contract) tesztek: azt ellenőrizzük, hogy a válasz FELÉPÍTÉSE megfelel a megállapodásnak,
 * nem csak egy-egy érték helyes. Ide tartozik a JSON séma és a válaszidő vizsgálata is.
 */
class PostsContractTest {

    private final PostsClient client = new PostsClient();

    // JSON Schema validáció: a séma fájl leírja a kötelező mezőket és azok típusát.
    // Ha egy mező hiányzik vagy rossz típusú, a teszt elbukik, anélkül hogy minden mezőt kézzel ellenőriznénk.
    @Test
    void shouldMatchPostJsonSchema() {
        client.getPost(1)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    // Válaszidő-ellenőrzés: a hívás 5 másodpercen belül térjen vissza.
    // A határ szándékosan laza, mert a nyilvános szolgáltatás sebessége ingadozik.
    @Test
    void shouldRespondQuickly() {
        client.getPost(1)
                .then()
                .time(lessThan(5000L));
    }

    // Adatvezérelt teszt: több (bejegyzés-azonosító, elvárt szerző) páros ugyanazzal a teszttel.
    // A JSONPlaceholder-ben 10 bejegyzés tartozik egy szerzőhöz.
    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "10, 1",
            "11, 2",
            "100, 10"
    })
    void shouldMapPostToExpectedUser(int postId, int expectedUserId) {
        Response response = client.getPost(postId);

        assertEquals(200, response.statusCode());
        assertEquals(expectedUserId, response.jsonPath().getInt("userId"));
    }
}
