package hu.learning.webshop.tesztautomatizalas.tests;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Séma-validáció (VIII. fejezet) – a nyers válaszokat az üzleti réteg try* metódusai adják. */
@DisplayName("Tesztszkript – JSON séma")
class JsonSchemaTest extends AbstractShopTest {

    /** Termék, terméklista, kosár. */
    @Test
    void responsesMatchSchemas() {
        shop.products().trySearch(null).then().statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/product-list.json"));
        shop.products().tryGet("P001").then().statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/product.json"));
        shop.cart().tryAdd("P003", 2).then().statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/cart.json"));
        shop.cart().tryCurrent().then().statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/cart.json"));
    }

    /** Felhasználónév-válasz mindkét változata. */
    @Test
    void usernameResponsesMatchSchema() {
        for (String name : new String[] {"admin12", "adm"}) {
            shop.usernames().tryValidate(name).then().statusCode(200)
                    .body(matchesJsonSchemaInClasspath("schemas/username-validation.json"));
        }
    }

    /** Hibaválaszok és sikeres fizetés. */
    @Test
    void problemAndCheckoutMatchSchemas() {
        shop.products().tryGet("P404").then().statusCode(404)
                .body(matchesJsonSchemaInClasspath("schemas/problem.json"));
        shop.checkout().tryPay().then().statusCode(409)
                .body(matchesJsonSchemaInClasspath("schemas/problem.json"));
        PAYPAL.willComplete("PAY-SCHEMA");
        shop.cart().add("P002", 1);
        shop.checkout().tryPay().then().statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/checkout.json"));
    }

    /** A séma szándékosan hibás JSON-t elutasít (a tesztorákulum ellenőrzése). */
    @Test
    void schemaRejectsBrokenPayloads() {
        assertThat("{\"id\":\"P001\",\"name\":\"x\",\"price\":\"7990\"}",
                not(matchesJsonSchemaInClasspath("schemas/product.json")));
        assertThat("{\"valid\":true,\"reason\":\"x\"}",
                not(matchesJsonSchemaInClasspath("schemas/username-validation.json")));
    }
}
