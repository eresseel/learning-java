package hu.learning.webshop.tesztautomatizalas.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * A {@code POST /checkout} API-tesztje: a webshop valódi HTTP-n fut (REST Assured), a mögötte
 * lévő „PayPal”-t WireMock szimulálja. Ez a rendszerintegrációs határ tesztje: a SUT és a külső
 * szolgáltatás közötti kommunikáció hibáit keresi (CTFL ismétlés: rendszerintegrációs teszt).
 */
@DisplayName("API – fizetés (PayPal WireMock-kal)")
class CheckoutApiTest extends AbstractApiTest {

    /** A szimulált PayPal. Statikus, mert a Spring kontextus indulása előtt kell a címe. */
    @RegisterExtension
    static WireMockExtension paypal = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().http2PlainDisabled(true))
            .build();

    /**
     * A SUT konfigurációját a WireMock címére irányítjuk (a kódban semmi nincs beégetve).
     * A lambda lustán értékelődik ki, amikor a kontextus épül – addigra a WireMock már fut.
     *
     * @param registry a Spring dinamikus property-regisztere
     */
    @DynamicPropertySource
    static void paypalProperties(DynamicPropertyRegistry registry) {
        registry.add("paypal.base-url", paypal::baseUrl);
        registry.add("paypal.timeout", () -> "500ms");
    }

    /** Segéd: 2 db P001 (2 × 7990 = 15980 Ft) a kosárba. */
    private void putTwoMiceInCart() {
        given().spec(api).body(Map.of("productId", "P001", "quantity", 2)).post("/cart/items")
                .then().statusCode(201);
    }

    /** Siker: 201, PAID, a kosár kiürül, és a PayPal pontosan a végösszeget kapta. */
    @Test
    void successfulCheckout() {
        // Arrange
        putTwoMiceInCart();
        paypal.stubFor(post("/v2/checkout/orders").willReturn(okJson("{\"id\":\"PAY-OK\",\"status\":\"COMPLETED\"}")));

        // Act + Assert – a válasz
        given().spec(api)
        .when().post("/checkout")
        .then().statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/checkout.json"))
                .body("status", equalTo("PAID"))
                .body("paymentId", equalTo("PAY-OK"))
                .body("total", comparesEqualTo(new BigDecimal("15980")));

        // Assert – mellékhatás: üres kosár
        given().spec(api).get("/cart").then().body("items", empty());
        // Assert – a külső hívás: egyszer, a helyes összeggel
        paypal.verify(1, postRequestedFor(urlEqualTo("/v2/checkout/orders"))
                // (a WireMock saját equalTo-ja – a Hamcrest equalTo-val névütközés miatt osztálynévvel)
                .withRequestBody(matchingJsonPath("$.purchase_units[0].amount.value", WireMock.equalTo("15980.00"))));
    }

    /** 4xx a PayPaltól → 402 PAYMENT_DECLINED, a kosár megmarad (újrapróbálható). */
    @Test
    void declinedPaymentKeepsCart() {
        putTwoMiceInCart();
        paypal.stubFor(post("/v2/checkout/orders").willReturn(aResponse().withStatus(422)));

        given().spec(api).when().post("/checkout")
                .then().spec(problem(402, "PAYMENT_DECLINED"))
                .body(matchesJsonSchemaInClasspath("schemas/problem.json"));
        given().spec(api).get("/cart").then().body("items", hasSize(1));
    }

    /** 5xx a PayPaltól → 502 PAYMENT_PROVIDER_ERROR. */
    @Test
    void providerErrorIs502() {
        putTwoMiceInCart();
        paypal.stubFor(post("/v2/checkout/orders").willReturn(aResponse().withStatus(500)));

        given().spec(api).when().post("/checkout").then().spec(problem(502, "PAYMENT_PROVIDER_ERROR"));
        given().spec(api).get("/cart").then().body("items", hasSize(1));
    }

    /** Lassú PayPal (1,5 s > 500 ms korlát) → 504 PAYMENT_TIMEOUT. */
    @Test
    void slowProviderIs504() {
        putTwoMiceInCart();
        paypal.stubFor(post("/v2/checkout/orders")
                .willReturn(okJson("{\"id\":\"PAY-late\",\"status\":\"COMPLETED\"}").withFixedDelay(1500)));

        given().spec(api).when().post("/checkout").then().spec(problem(504, "PAYMENT_TIMEOUT"));
    }

    /** Üres kosár → 409 EMPTY_CART, és a PayPal-t meg sem hívjuk. */
    @Test
    void emptyCartIsRejectedWithoutCallingPaypal() {
        given().spec(api).when().post("/checkout").then().spec(problem(409, "EMPTY_CART"));
        paypal.verify(0, anyRequestedFor(anyUrl()));
    }
}
