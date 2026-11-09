package hu.laci.learning;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link Calculator}.
 */
class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    void shouldAddPositiveNumbers() {
        // Arrange: the calculator is created in the field above.
        // Act: add two positive numbers.
        int result = calculator.add(2, 3);

        // Assert: the result must equal the expected sum.
        assertEquals(5, result);
    }

    @Test
    void shouldAddNegativeNumbers() {
        assertEquals(-7, calculator.add(-3, -4));
    }

    @Test
    void shouldAddZero() {
        assertEquals(8, calculator.add(8, 0));
    }
}
