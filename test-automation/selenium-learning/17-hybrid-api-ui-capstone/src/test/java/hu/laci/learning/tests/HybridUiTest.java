package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.pages.PostListPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UI réteg: a helyi HTML oldal megjelenítését és a szűrő működését ellenőrzi.
 * <p>
 * Ezek a tesztek azt mérik, amit a felhasználó lát és használ.
 * A megjelenítési logika hibája itt derül ki, nem az API tesztekben.
 */
class HybridUiTest extends BaseTest {

    private PostListPage postListPage;

    @BeforeEach
    void openPage() {
        postListPage = new PostListPage(driver).open();
    }

    @Test
    void shouldDisplayAllPosts() {
        assertEquals(5, postListPage.getPostCount());
    }

    @Test
    void shouldShowCorrectTitles() {
        assertTrue(postListPage.getPostTitles().contains("First post"));
        assertTrue(postListPage.getPostTitles().contains("Final post"));
    }

    @Test
    void shouldFilterPostsByTitle() {
        postListPage.filterBy("post");

        // "Another one" nem tartalmaz "post" szót, tehát 4 elem marad látható.
        assertEquals(4, postListPage.getVisiblePostCount());
    }

    @Test
    void shouldShowNoResultsForNonMatchingFilter() {
        postListPage.filterBy("zzznomatch");

        assertEquals(0, postListPage.getVisiblePostCount());
    }
}
