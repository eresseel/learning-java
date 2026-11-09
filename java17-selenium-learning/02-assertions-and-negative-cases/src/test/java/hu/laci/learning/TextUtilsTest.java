package hu.laci.learning;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextUtilsTest {

    @Test
    void shouldTrimAndLowercaseText() {
        assertEquals("hello world", TextUtils.normalize("  HELLO WORLD  "));
    }

    @Test
    void shouldReturnNullWhenNormalizeReceivesNull() {
        assertEquals(null, TextUtils.normalize(null));
    }

    @Test
    void shouldRejectNull() {
        assertThrows(IllegalArgumentException.class,
                () -> TextUtils.requireNonBlank(null));
    }

    @Test
    void shouldRejectWhitespaceOnlyInput() {
        assertThrows(IllegalArgumentException.class,
                () -> TextUtils.requireNonBlank("   "));
    }

    @Test
    void shouldReturnNonBlankInputUnchanged() {
        assertEquals("GarageFlow", TextUtils.requireNonBlank("GarageFlow"));
    }
}
