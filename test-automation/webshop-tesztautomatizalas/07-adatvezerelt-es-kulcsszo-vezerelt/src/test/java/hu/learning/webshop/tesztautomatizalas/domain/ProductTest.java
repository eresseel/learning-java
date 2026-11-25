package hu.learning.webshop.tesztautomatizalas.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** A {@link Product} invariánsainak (érvényességi szabályainak) tesztjei. */
class ProductTest {

    /** Az ár mindig 2 tizedesjegyre normalizálódik. */
    @Test
    void priceIsNormalizedToTwoDecimals() {
        Product p = new Product("P1", "Teszttermék", new BigDecimal("100"));
        assertEquals(new BigDecimal("100.00"), p.price());
    }

    /** Nulla vagy negatív ár nem megengedett. */
    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "-0.01"})
    void nonPositivePriceIsRejected(String price) {
        assertThrows(IllegalArgumentException.class,
                () -> new Product("P1", "Teszttermék", new BigDecimal(price)));
    }

    /** Hiányzó azonosító vagy név esetén nem jön létre termék. */
    @Test
    void blankIdOrNameIsRejected() {
        BigDecimal price = BigDecimal.TEN;
        assertThrows(IllegalArgumentException.class, () -> new Product(" ", "Név", price));
        assertThrows(IllegalArgumentException.class, () -> new Product("P1", "", price));
    }
}
