package hu.laci.learning;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A {@link Calculator} osztály unit tesztjei (JUnit 5).
 * <p>
 * A Maven Surefire plugin az osztálynév alapján ("...Test" végződés) találja meg és futtatja.
 * JUnit 5-ben az osztály és a tesztmetódusok lehetnek csomag-szintű láthatóságúak (nem kell {@code public}).
 */
class CalculatorTest {

    // A tesztelt objektum. Mivel a JUnit minden @Test metódushoz új osztálypéldányt hoz létre,
    // minden teszt friss Calculator-t kap, így a tesztek nem befolyásolják egymást.
    private final Calculator calculator = new Calculator();

    // A @Test jelöli, hogy ez a metódus egy futtatandó teszt.
    @Test
    void shouldAddPositiveNumbers() {
        // Arrange (előkészítés): a számológép a fenti mezőben már létrejött.
        // Act (végrehajtás): két pozitív számot adunk össze.
        int result = calculator.add(2, 3);

        // Assert (ellenőrzés): az eredménynek egyeznie kell az elvárt összeggel.
        // Az assertEquals első paramétere az ELVÁRT, a második a TÉNYLEGES érték.
        assertEquals(5, result);
    }

    // Negatív számok: ellenőrzi, hogy az összeadás előjeles értékekkel is helyes.
    @Test
    void shouldAddNegativeNumbers() {
        assertEquals(-7, calculator.add(-3, -4));
    }

    // Határeset: a nulla hozzáadása nem változtathatja meg az értéket.
    @Test
    void shouldAddZero() {
        assertEquals(8, calculator.add(8, 0));
    }
}
