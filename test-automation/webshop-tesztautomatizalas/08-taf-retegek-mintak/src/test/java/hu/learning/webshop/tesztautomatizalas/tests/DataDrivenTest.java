package hu.learning.webshop.tesztautomatizalas.tests;

import static org.assertj.core.api.Assertions.assertThat;

import hu.learning.webshop.tesztautomatizalas.domain.Product;
import hu.learning.webshop.tesztautomatizalas.taf.core.Problem;
import hu.learning.webshop.tesztautomatizalas.web.dto.CartResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

/** Adatvezérelt tesztek (07. lépés) – a TAF üzleti rétegére átírva; az adatfájlok változatlanok. */
@DisplayName("Tesztszkript – adatvezérelt (CSV)")
class DataDrivenTest extends AbstractShopTest {

    /**
     * @param username      a vizsgált név
     * @param expectedValid elvárt eredmény
     * @param megjegyzes    magyarázat (a riportban)
     */
    @ParameterizedTest(name = "[{index}] \"{0}\" -> {1} ({2})")
    @CsvFileSource(resources = "/testdata/usernames.csv", delimiter = ';', numLinesToSkip = 1, encoding = "UTF-8")
    void usernameRule(String username, boolean expectedValid, String megjegyzes) {
        assertThat(shop.usernames().isAccepted(username)).isEqualTo(expectedValid);
    }

    /**
     * @param productId         termék
     * @param quantity          mennyiség
     * @param expectedStatus    elvárt HTTP státusz
     * @param expectedErrorCode elvárt hibakód (sikernél {@code null})
     * @param megjegyzes        magyarázat
     */
    @ParameterizedTest(name = "[{index}] {0} × {1} -> {2} {3} ({4})")
    @CsvFileSource(resources = "/testdata/cart-quantities.csv", delimiter = ';', numLinesToSkip = 1, encoding = "UTF-8")
    void addToCartQuantities(String productId, int quantity, int expectedStatus,
                             String expectedErrorCode, String megjegyzes) {
        Response response = shop.cart().tryAdd(productId, quantity);
        assertThat(response.statusCode()).isEqualTo(expectedStatus);
        if (expectedErrorCode == null) {
            assertThat(response.as(CartResponse.class).items().getFirst().quantity()).isEqualTo(quantity);
        } else {
            assertThat(response.as(Problem.class).errorCode()).isEqualTo(expectedErrorCode);
        }
    }

    /**
     * @param query           keresőszöveg
     * @param expectedCount   elvárt találatszám
     * @param expectedFirstId első találat (vagy {@code null})
     */
    @ParameterizedTest(name = "[{index}] keresés \"{0}\" -> {1} találat")
    @CsvFileSource(resources = "/testdata/search.csv", delimiter = ';', numLinesToSkip = 1, encoding = "UTF-8")
    void search(String query, int expectedCount, String expectedFirstId) {
        List<Product> hits = shop.products().search(query);
        assertThat(hits).hasSize(expectedCount);
        if (expectedFirstId != null) {
            assertThat(hits.getFirst().id()).isEqualTo(expectedFirstId);
        }
    }
}
