package hu.learning.webshop.tesztautomatizalas.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import io.restassured.response.ValidatableResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

/**
 * <b>Adatvezérelt tesztek (DDT)</b>: egy strukturált tesztszkript, amelyet külső CSV fájlok
 * sorai „etetnek” ({@code src/test/resources/testdata/}).
 *
 * <p>A tananyag szerint a DDT előnye, hogy a tesztesetek egyszerűen bővíthetők (új sor a CSV-ben,
 * kódváltozás nélkül), és a tesztelemzők szabadon dolgozhatnak; hátránya az adatkezelési kihívás
 * (pl. melyik adat melyik környezethez/verzióhoz tartozik – lásd 09. lépés, X. feladat).</p>
 */
@DisplayName("DDT – adatvezérelt API-tesztek CSV-ből")
class DataDrivenApiTest extends AbstractApiTest {

    /**
     * Felhasználónév-szabály CSV-ből. A {@code megjegyzes} oszlop csak a riportba kerül (név).
     *
     * @param username      a vizsgált név
     * @param expectedValid az elvárt eredmény
     * @param megjegyzes    emberi magyarázat a sorhoz
     */
    @ParameterizedTest(name = "[{index}] \"{0}\" -> {1} ({2})")
    @CsvFileSource(resources = "/testdata/usernames.csv", delimiter = ';', numLinesToSkip = 1, encoding = "UTF-8")
    void usernameRule(String username, boolean expectedValid, String megjegyzes) {
        given().spec(api).body(Map.of("username", username))
        .when().post("/validate-username")
        .then().spec(okJson).body("valid", equalTo(expectedValid));
    }

    /**
     * Kosárba helyezés különböző mennyiségekkel: határértékek és hibakódok CSV-ből.
     *
     * @param productId         termék
     * @param quantity          mennyiség
     * @param expectedStatus    elvárt HTTP státusz
     * @param expectedErrorCode elvárt hibakód (sikeres esetben üres → {@code null})
     * @param megjegyzes        magyarázat
     */
    @ParameterizedTest(name = "[{index}] {0} × {1} -> {2} {3} ({4})")
    @CsvFileSource(resources = "/testdata/cart-quantities.csv", delimiter = ';', numLinesToSkip = 1, encoding = "UTF-8")
    void addToCartQuantities(String productId, int quantity, int expectedStatus,
                             String expectedErrorCode, String megjegyzes) {
        // Act
        ValidatableResponse response = given().spec(api)
                .body(Map.of("productId", productId, "quantity", quantity))
                .when().post("/cart/items")
                .then().statusCode(expectedStatus);
        // Assert – sikernél a mennyiség, hibánál a hibakód
        if (expectedErrorCode == null) {
            response.body("items[0].quantity", equalTo(quantity));
        } else {
            response.body("errorCode", equalTo(expectedErrorCode));
        }
    }

    /**
     * Termékkeresés: találatszám és első találat CSV-ből.
     *
     * @param query           keresőszöveg
     * @param expectedCount   elvárt találatszám
     * @param expectedFirstId az első találat azonosítója (üres, ha nincs találat)
     */
    @ParameterizedTest(name = "[{index}] keresés \"{0}\" -> {1} találat")
    @CsvFileSource(resources = "/testdata/search.csv", delimiter = ';', numLinesToSkip = 1, encoding = "UTF-8")
    void search(String query, int expectedCount, String expectedFirstId) {
        ValidatableResponse response = given().spec(api).queryParam("q", query)
                .when().get("/products")
                .then().spec(okJson).body("$", hasSize(expectedCount));
        if (expectedFirstId != null) {
            response.body("[0].id", equalTo(expectedFirstId));
        }
    }
}
