package hu.laci.learning;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A {@link TextUtils} tesztjei: pozitív esetek, határesetek és az elvárt hibák (negatív tesztek).
 */
class TextUtilsTest {

    // Pozitív eset: a szóközök levágódnak és a szöveg kisbetűs lesz.
    @Test
    void shouldTrimAndLowercaseText() {
        assertEquals("hello world", TextUtils.normalize("  HELLO WORLD  "));
    }

    // Határeset: null bemenetre a metódusnak null-t kell visszaadnia, nem kivételt dobnia.
    @Test
    void shouldReturnNullWhenNormalizeReceivesNull() {
        assertEquals(null, TextUtils.normalize(null));
    }

    // Negatív teszt: a hibás bemenetre elvárt kivételt az assertThrows ellenőrzi.
    // A lambda ( () -> ... ) az a kódrészlet, amelynek kivételt kell dobnia.
    @Test
    void shouldRejectNull() {
        assertThrows(IllegalArgumentException.class,
                () -> TextUtils.requireNonBlank(null));
    }

    // Negatív teszt: a csak szóközt tartalmazó szöveg is "üresnek" számít.
    @Test
    void shouldRejectWhitespaceOnlyInput() {
        assertThrows(IllegalArgumentException.class,
                () -> TextUtils.requireNonBlank("   "));
    }

    // Pozitív eset: érvényes bemenetnél a metódus változtatás nélkül visszaadja az értéket.
    @Test
    void shouldReturnNonBlankInputUnchanged() {
        assertEquals("GarageFlow", TextUtils.requireNonBlank("GarageFlow"));
    }
}
