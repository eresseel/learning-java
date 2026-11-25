package hu.learning.webshop.tesztautomatizalas.tests;

import static org.assertj.core.api.Assertions.assertThat;

import hu.learning.webshop.tesztautomatizalas.domain.Product;
import hu.learning.webshop.tesztautomatizalas.taf.core.Problem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Termékkeresés és -megtekintés tesztszkriptjei (V. feladat: termékkereső folyamat). */
@DisplayName("Tesztszkript – termékek")
class ProductTest extends AbstractShopTest {

    /** Keresés: két egér, azonosító szerinti sorrendben. */
    @Test
    void searchFindsMice() {
        List<Product> hits = shop.products().search("egér");
        assertThat(hits).extracting(Product::id).containsExactly("P001", "P006");
    }

    /** Teljes lista, duplikáció nélkül. */
    @Test
    void listAllProducts() {
        assertThat(shop.products().search(null)).hasSize(6).extracting(Product::id).doesNotHaveDuplicates();
    }

    /** Ismeretlen termék: 404 a megfelelő hibakóddal. */
    @Test
    void unknownProductIsNotFound() {
        Problem problem = shop.products().tryGet("P404").then().statusCode(404).extract().as(Problem.class);
        assertThat(problem.errorCode()).isEqualTo("UNKNOWN_PRODUCT");
        assertThat(problem.instance()).isEqualTo("/products/P404");
    }
}
