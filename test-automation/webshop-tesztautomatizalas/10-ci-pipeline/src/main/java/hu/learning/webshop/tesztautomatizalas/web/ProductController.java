package hu.learning.webshop.tesztautomatizalas.web;

import hu.learning.webshop.tesztautomatizalas.domain.Product;
import hu.learning.webshop.tesztautomatizalas.domain.ProductCatalog;
import hu.learning.webshop.tesztautomatizalas.domain.UnknownProductException;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Termékek listázása, keresése és lekérdezése (I. feladat: „termékek keresése és megtekintése”). */
@RestController
@RequestMapping("/products")
public class ProductController {

    /** A termékkatalógus (repository). */
    private final ProductCatalog catalog;

    /**
     * @param catalog a katalógus bean
     */
    public ProductController(ProductCatalog catalog) {
        this.catalog = catalog;
    }

    /**
     * {@code GET /products} vagy {@code GET /products?q=egér}.
     *
     * @param query opcionális keresőszöveg
     * @return a (szűrt) terméklista
     */
    @GetMapping
    public List<Product> list(@RequestParam(name = "q", required = false) String query) {
        return catalog.search(query);
    }

    /**
     * {@code GET /products/{id}}.
     *
     * @param id termékazonosító
     * @return a termék
     * @throws UnknownProductException ha nincs ilyen (→ 404, lásd {@link ApiExceptionHandler})
     */
    @GetMapping("/{id}")
    public Product get(@PathVariable String id) {
        return catalog.findById(id).orElseThrow(() -> new UnknownProductException(id));
    }
}
