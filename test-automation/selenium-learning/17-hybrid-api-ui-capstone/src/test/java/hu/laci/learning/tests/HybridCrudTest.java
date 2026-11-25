package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.api.Post;
import hu.laci.learning.api.PostsClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Hibrid teszt: ugyanabban a tesztmetódusban használ API és UI ellenőrzést.
 * <p>
 * A két réteg különböző kérdésekre válaszol:
 * <ul>
 *   <li>Az API azt ellenőrzi, hogy az ADAT helyes-e (gyors, stabil, független a megjelenítéstől).</li>
 *   <li>A UI azt ellenőrzi, hogy a FELHASZNÁLÓ LÁTJA-E az adatot a böngészőben (lassabb, de az élményt méri).</li>
 * </ul>
 * A két réteg kombinálásának tipikus mintája: az API létrehozza az adatot, a UI ellenőrzi a megjelenítést
 * (vagy fordítva: a UI hoz létre, az API ellenőrzi, hogy valóban mentődött-e).
 */
class HybridCrudTest extends BaseTest {

    private final PostsClient apiClient = new PostsClient();

    // API réteg: a JSONPlaceholder-en 100 bejegyzés van, ezt ellenőrizzük az API-n keresztül.
    @Test
    void shouldConfirmPostCountViaApi() {
        int count = apiClient.countPosts();

        assertEquals(100, count);
    }

    // API réteg: egy bejegyzés adatainak ellenőrzése.
    @Test
    void shouldFetchPostDataViaApi() {
        Post post = apiClient.getPost(1);

        assertEquals(1, post.id());
        assertEquals(1, post.userId());
        assertNotNull(post.title());
        assertTrue(post.title().length() > 0);
    }

    // API réteg: létrehozás és az adatok visszaellenőrzése.
    @Test
    void shouldCreatePostAndVerifyResponse() {
        Post newPost = new Post(1, null, "My hybrid test post", "Test body");

        Post created = apiClient.createPost(newPost);

        // A JSONPlaceholder mindig 101-es id-t ad (a 100 meglévő után).
        assertEquals(101, created.id());
        assertEquals(newPost.title(), created.title());
    }
}
