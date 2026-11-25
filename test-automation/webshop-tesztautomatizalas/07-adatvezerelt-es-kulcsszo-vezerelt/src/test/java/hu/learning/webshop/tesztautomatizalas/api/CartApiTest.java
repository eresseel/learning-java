package hu.learning.webshop.tesztautomatizalas.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import hu.learning.webshop.tesztautomatizalas.web.dto.AddCartItemRequest;
import hu.learning.webshop.tesztautomatizalas.web.dto.CartResponse;
import hu.learning.webshop.tesztautomatizalas.web.dto.UpdateQuantityRequest;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * A kosár API-tesztjei – a VIII. feladat „Kosár” funkciója rendszerszinten, valódi HTTP-n.
 */
@DisplayName("API – kosár")
class CartApiTest extends AbstractApiTest {

    /** Pozitív (boldog út) esetek. */
    @Nested
    class Pozitiv {

        /** POJO szerializáció (kérés) és deszerializáció (válasz) recordokkal. */
        @Test
        void addItemWithPojo() {
            // Arrange – a kérés-törzs Java objektum, a REST Assured Jackson-nel JSON-ná alakítja
            AddCartItemRequest request = new AddCartItemRequest("P002", 2);

            // Act
            CartResponse cart = given().spec(api).body(request)
                    .when().post("/cart/items")
                    .then().statusCode(201)
                    .extract().as(CartResponse.class);

            // Assert – típusos objektumon, AssertJ-vel
            assertThat(cart.items()).singleElement()
                    .satisfies(item -> {
                        assertThat(item.productId()).isEqualTo("P002");
                        assertThat(item.quantity()).isEqualTo(2);
                    });
            assertThat(cart.total()).isEqualByComparingTo("49980");
        }

        /** Hozzáadás + mennyiség frissítés: a végösszeg követi a változást. */
        @Test
        void addThenUpdateQuantity() {
            given().spec(api).body(new AddCartItemRequest("P001", 1)).post("/cart/items").then().statusCode(201);
            given().spec(api).body(new AddCartItemRequest("P003", 1)).post("/cart/items").then().statusCode(201);

            given().spec(api).body(new UpdateQuantityRequest(3))
            .when().patch("/cart/items/{id}", "P001")
            .then().spec(okJson)
                    .body("items", hasSize(2))
                    .body("items.find { it.productId == 'P001' }.quantity", equalTo(3))
                    // 3 × 7990 + 12990 = 36960
                    .body("total", comparesEqualTo(new BigDecimal("36960")))
                    .body("itemCount", equalTo(4));
        }

        /** Törlés után a GET /cart üres kosarat mutat. */
        @Test
        void removeItem() {
            given().spec(api).body(new AddCartItemRequest("P004", 1)).post("/cart/items");

            given().spec(api).when().delete("/cart/items/P004").then().statusCode(204);

            given().spec(api).when().get("/cart")
                    .then().spec(okJson)
                    .body("items", empty())
                    .body("total", comparesEqualTo(BigDecimal.ZERO));
        }

        /** A válaszidő is ellenőrizhető (egyszerű nem-funkcionális elvárás). */
        @Test
        void getCartIsFast() {
            given().spec(api).when().get("/cart")
                    .then().spec(okJson)
                    .time(org.hamcrest.Matchers.lessThan(2000L));    // ms
        }
    }

    /** Negatív esetek: hibás bemenetek és szabálysértések. */
    @Nested
    class Negativ {

        /** Határérték: 0 és 100 → 400 INVALID_QUANTITY. */
        @Test
        void quantityOutOfRange() {
            given().spec(api).body(new AddCartItemRequest("P001", 0))
                    .when().post("/cart/items")
                    .then().spec(problem(400, "INVALID_QUANTITY"));
            given().spec(api).body(new AddCartItemRequest("P001", 100))
                    .when().post("/cart/items")
                    .then().spec(problem(400, "INVALID_QUANTITY"))
                    .body("detail", containsString("1..99"));
        }

        /** Ismeretlen termék → 404 UNKNOWN_PRODUCT. */
        @Test
        void unknownProduct() {
            given().spec(api).body(new AddCartItemRequest("P999", 1))
                    .when().post("/cart/items")
                    .then().spec(problem(404, "UNKNOWN_PRODUCT"));
        }

        /** Hiányzó mező → 400 VALIDATION_ERROR (a törzs itt nyers JSON szöveg). */
        @Test
        void missingQuantity() {
            given().spec(api).body("{\"productId\":\"P001\"}")
                    .when().post("/cart/items")
                    .then().spec(problem(400, "VALIDATION_ERROR"))
                    .body("detail", equalTo("quantity: a quantity kötelező"));
        }

        /** Rossz típus (szöveg szám helyett) és ismeretlen mező → 400 MALFORMED_REQUEST. */
        @Test
        void malformedBodies() {
            given().spec(api).body("{\"productId\":\"P001\",\"quantity\":\"sok\"}")
                    .when().post("/cart/items")
                    .then().spec(problem(400, "MALFORMED_REQUEST"));
            given().spec(api).body("{\"productId\":\"P001\",\"quantity\":1,\"discount\":50}")
                    .when().post("/cart/items")
                    .then().spec(problem(400, "MALFORMED_REQUEST"));
        }

        /** Nem kosárban lévő tétel módosítása és törlése → 404 ITEM_NOT_IN_CART. */
        @Test
        void itemNotInCart() {
            given().spec(api).body(new UpdateQuantityRequest(2))
                    .when().patch("/cart/items/P001")
                    .then().spec(problem(404, "ITEM_NOT_IN_CART"));
            given().spec(api).when().delete("/cart/items/P001")
                    .then().spec(problem(404, "ITEM_NOT_IN_CART"));
        }
    }
}
