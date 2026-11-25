package hu.learning.webshop.tesztautomatizalas.domain;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Memóriában tárolt termékkatalógus – adatbázis helyett.
 *
 * <p>A tananyag SUT-ja MySQL-t használ; a tanulópéldában a memóriabeli tároló
 * determinisztikus, gyors és nem igényel külső környezetet.</p>
 */
public class InMemoryProductCatalog implements ProductCatalog {

    /** Azonosító → termék; a {@link LinkedHashMap} megőrzi a beszúrási sorrendet. */
    private final Map<String, Product> products = new LinkedHashMap<>();

    /**
     * @param initialProducts a katalógus kezdeti tartalma
     * @throws IllegalArgumentException ha két termék azonosítója megegyezik
     */
    public InMemoryProductCatalog(List<Product> initialProducts) {
        for (Product p : initialProducts) {
            // putIfAbsent: ha már volt ilyen kulcs, a régi értéket adja vissza → duplikáció
            if (products.putIfAbsent(p.id(), p) != null) {
                throw new IllegalArgumentException("Duplikált termékazonosító: " + p.id());
            }
        }
    }

    /**
     * Mintakatalógus a demóhoz és a tesztekhez.
     * A mintaadat saját készítésű, az eredeti adatbázis nem volt a tananyag része.
     *
     * @return 6 termékből álló katalógus
     */
    public static InMemoryProductCatalog withSampleData() {
        return new InMemoryProductCatalog(List.of(
                new Product("P001", "Vezeték nélküli egér", new BigDecimal("7990")),
                new Product("P002", "Mechanikus billentyűzet", new BigDecimal("24990")),
                new Product("P003", "USB-C töltő 65 W", new BigDecimal("12990")),
                new Product("P004", "Monitor, 68 cm képátló", new BigDecimal("89990")),
                new Product("P005", "Laptoptáska 40 cm", new BigDecimal("9990")),
                new Product("P006", "Vezetékes egér", new BigDecimal("3990"))));
    }

    /** {@inheritDoc} */
    @Override
    public List<Product> findAll() {
        return products.values().stream()
                .sorted(Comparator.comparing(Product::id))
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    /** {@inheritDoc} */
    @Override
    public List<Product> search(String query) {
        // 1. Üres keresés = teljes lista (a felületen az üres keresőmező is ezt adja)
        if (query == null || query.isBlank()) {
            return findAll();
        }
        // 2. Kisbetűsítés magyar területi beállítással, hogy az ékezetes betűk is egyezzenek
        Locale hu = Locale.forLanguageTag("hu");
        String needle = query.trim().toLowerCase(hu);
        // 3. Szűrés stream-mel: a név tartalmazza-e a keresett részt
        return findAll().stream()
                .filter(p -> p.name().toLowerCase(hu).contains(needle))
                .toList();
    }
}
