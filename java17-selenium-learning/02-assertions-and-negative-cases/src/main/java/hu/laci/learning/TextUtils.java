package hu.laci.learning;

import java.util.Locale;

/**
 * String helper methods used in the assertion exercises.
 */
public final class TextUtils {

    private TextUtils() {
        // Utility classes should not be instantiated.
    }

    /**
     * Trims surrounding whitespace and converts the text to lowercase.
     */
    public static String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Returns the input if it contains non-whitespace characters.
     *
     * @throws IllegalArgumentException when the value is null or blank
     */
    public static String requireNonBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Value must not be blank");
        }
        return value;
    }
}
