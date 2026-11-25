package hu.learning.webshop.tesztautomatizalas.api;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * <b>Séma-validáció</b> (VIII. fejezet: „automatikus validálás – adattípusok, kötelező mezők –
 * rövidebb és hatékonyabb kód”).
 *
 * <p>Egyetlen {@code matchesJsonSchemaInClasspath(...)} sor ellenőrzi az összes mező meglétét,
 * típusát, tartományát és azt, hogy nincs-e váratlan mező – mezőnkénti {@code body(...)}
 * hívások tucatjai helyett. A sémák: {@code src/test/resources/schemas/}.</p>
 */
@DisplayName("API – JSON séma validáció")
class JsonSchemaTest extends AbstractApiTest {

    /** Terméklista és egy termék sémája. */
    @Test
    void productsMatchSchema() {
        given().spec(api).when().get("/products")
                .then().spec(okJson).body(matchesJsonSchemaInClasspath("schemas/product-list.json"));
        given().spec(api).when().get("/products/P001")
                .then().spec(okJson).body(matchesJsonSchemaInClasspath("schemas/product.json"));
    }

    /** Üres és teli kosár is megfelel a kosár-sémának. */
    @Test
    void cartMatchesSchema() {
        given().spec(api).when().get("/cart")
                .then().spec(okJson).body(matchesJsonSchemaInClasspath("schemas/cart.json"));
        given().spec(api).body(Map.of("productId", "P003", "quantity", 2)).when().post("/cart/items")
                .then().statusCode(201).body(matchesJsonSchemaInClasspath("schemas/cart.json"));
    }

    /** A felhasználónév-válasz mindkét változata (valid / invalid + reason) megfelel. */
    @Test
    void usernameResponsesMatchSchema() {
        for (String name : new String[] {"admin12", "adm"}) {
            given().spec(api).body(Map.of("username", name)).when().post("/validate-username")
                    .then().spec(okJson).body(matchesJsonSchemaInClasspath("schemas/username-validation.json"));
        }
    }

    /** A hibaválaszok (404, 400) is sémát követnek. */
    @Test
    void problemsMatchSchema() {
        given().spec(api).when().get("/products/P404")
                .then().statusCode(404).body(matchesJsonSchemaInClasspath("schemas/problem.json"));
        given().spec(api).body("{}").when().post("/cart/items")
                .then().statusCode(400).body(matchesJsonSchemaInClasspath("schemas/problem.json"));
    }

    /**
     * A séma valóban „harap”: szándékosan hibás JSON-okat <em>elutasít</em>.
     * (Egy validáció, ami soha nem bukik el, nem ér semmit – a tesztorákulumot is ellenőrizni kell.)
     */
    @Test
    void schemaRejectsBrokenPayloads() {
        // Szöveges ár szám helyett
        assertThat("{\"id\":\"P001\",\"name\":\"x\",\"price\":\"7990\"}",
                not(matchesJsonSchemaInClasspath("schemas/product.json")));
        // 0 darabos tétel és hiányzó currency
        assertThat("{\"items\":[{\"productId\":\"P001\",\"name\":\"x\",\"unitPrice\":1,\"quantity\":0,\"lineTotal\":0}],"
                        + "\"itemCount\":0,\"total\":0}",
                not(matchesJsonSchemaInClasspath("schemas/cart.json")));
        // valid=true MELLETT reason – a séma oneOf szabálya ezt tiltja
        assertThat("{\"valid\":true,\"reason\":\"x\"}",
                not(matchesJsonSchemaInClasspath("schemas/username-validation.json")));
    }
}
