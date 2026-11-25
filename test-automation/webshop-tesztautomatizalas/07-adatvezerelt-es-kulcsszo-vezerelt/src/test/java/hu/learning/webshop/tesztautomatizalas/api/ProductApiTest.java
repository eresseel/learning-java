package hu.learning.webshop.tesztautomatizalas.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

import hu.learning.webshop.tesztautomatizalas.domain.Product;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** A termék-végpontok API-tesztjei (keresés, lekérdezés). */
@DisplayName("API – termékek")
class ProductApiTest extends AbstractApiTest {

    /** Keresés query paraméterrel: Hamcrest matcherek a JSON tömb elemeire (GPath kifejezések). */
    @Test
    void searchByQueryParam() {
        given().spec(api).queryParam("q", "egér")
        .when().get("/products")
        .then().spec(okJson)
                .body("$", hasSize(2))
                .body("id", contains("P001", "P006"))                    // a tömb összes "id" mezője, sorrendben
                .body("name", everyItem(containsStringIgnoringCase("egér")))
                .body("find { it.id == 'P006' }.price", comparesEqualTo(new BigDecimal("3990")));
    }

    /** Deszerializáció POJO-tömbbé, majd AssertJ ellenőrzés. */
    @Test
    void deserializeToPojoArray() {
        // Act – a JSON tömb Product[]-tá alakul (Jackson a recordokat is kezeli)
        Product[] products = given().spec(api)
                .when().get("/products")
                .then().spec(okJson)
                .extract().as(Product[].class);

        // Assert – AssertJ: olvasható, láncolható ellenőrzések
        assertThat(products).hasSize(6)
                .extracting(Product::id)
                .startsWith("P001")
                .doesNotHaveDuplicates();
    }

    /** Ismeretlen termék: 404 ProblemDetail – a hibaválasz is szerződés! */
    @Test
    void unknownProductIs404() {
        Problem problem = given().spec(api)
                .when().get("/products/{id}", "P404")                  // útvonal-paraméter
                .then().spec(problem(404, "UNKNOWN_PRODUCT"))
                .extract().as(Problem.class);

        assertThat(problem.instance()).isEqualTo("/products/P404");
        assertThat(problem.detail()).isEqualTo("Ismeretlen termék: P404");
    }
}
