package hu.laci.learning.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Negatív API tesztek: nem azt nézzük, hogy a jó kérés működik, hanem azt, hogy a HIBÁS kérésre
 * a szerver helyesen utasít el (megfelelő státuszkód), és nem omlik össze.
 */
class PostsNegativeTest {

    private final PostsClient client = new PostsClient();

    // Nem létező azonosító: a szerver 404 Not Found-ot kell hogy adjon.
    @Test
    void shouldReturn404ForUnknownPost() {
        client.getPost(99999)
                .then()
                .statusCode(404);
    }

    // Nem létező végpont: szintén 404.
    @Test
    void shouldReturn404ForUnknownEndpoint() {
        client.getRaw("/this-endpoint-does-not-exist")
                .then()
                .statusCode(404);
    }

    // Érvénytelen típusú azonosító (szöveg szám helyett): ugyanaz a viselkedés várt.
    // A @ParameterizedTest egyetlen tesztmetódusból több futást csinál az adatokkal.
    @ParameterizedTest
    @ValueSource(strings = {"abc", "-1", "0", "1.5"})
    void shouldReturn404ForInvalidIds(String invalidId) {
        client.getRaw("/posts/" + invalidId)
                .then()
                .statusCode(404);
    }
}
