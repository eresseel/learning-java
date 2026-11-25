package hu.learning.webshop.tesztautomatizalas.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** A memóriabeli katalógus keresési funkciójának tesztjei. */
class InMemoryProductCatalogTest {

    /** A mintakatalógus – minden teszt ugyanazt a determinisztikus adatot látja. */
    private final ProductCatalog catalog = InMemoryProductCatalog.withSampleData();

    /** Keresés: kis-/nagybetű és ékezet-érzékeny részszöveg-egyezés, várt találatszámmal. */
    @ParameterizedTest(name = "keresés \"{0}\" -> {1} találat")
    @CsvSource({
            "egér,        2",
            "EGÉR,        2",
            "billentyűzet, 1",
            "'  töltő  ', 1",
            "nincsilyen,  0",
            "'',          6"
    })
    void searchFindsByNamePart(String query, int expectedHits) {
        // Act
        List<Product> hits = catalog.search(query);
        // Assert
        assertEquals(expectedHits, hits.size(), () -> "találatok: " + hits);
    }

    /** Létező és nem létező azonosító feloldása. */
    @Test
    void findById() {
        assertEquals("Vezeték nélküli egér", catalog.findById("P001").orElseThrow().name());
        assertTrue(catalog.findById("P999").isEmpty());
    }

    /** A findAll azonosító szerint rendezett listát ad. */
    @Test
    void findAllIsSortedById() {
        List<String> ids = catalog.findAll().stream().map(Product::id).toList();
        assertEquals(List.of("P001", "P002", "P003", "P004", "P005", "P006"), ids);
    }

    /** Duplikált azonosító már a katalógus felépítésekor kiderül. */
    @Test
    void duplicateIdIsRejected() {
        Product p = new Product("X", "x", BigDecimal.ONE);
        assertThrows(IllegalArgumentException.class, () -> new InMemoryProductCatalog(List.of(p, p)));
    }
}
