package hu.learning.webshop.tesztautomatizalas.tests;

import static org.assertj.core.api.Assertions.assertThat;

import hu.learning.webshop.tesztautomatizalas.taf.core.Problem;
import hu.learning.webshop.tesztautomatizalas.web.dto.CartResponse;
import io.restassured.response.Response;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** A kosár tesztszkriptjei (VIII. feladat) – üzleti nyelven. */
@DisplayName("Tesztszkript – kosár")
class CartTest extends AbstractShopTest {

    /** Hozzáadás + mennyiség frissítés: a végösszeg követi. */
    @Test
    void addThenUpdateQuantity() {
        // Arrange – a facade egy hívással feltölti a kosarat
        Map<String, Integer> items = new LinkedHashMap<>();
        items.put("P001", 1);
        items.put("P003", 1);
        shop.fillCart(items);

        // Act
        CartResponse cart = shop.cart().updateQuantity("P001", 3);

        // Assert – 3 × 7990 + 12990 = 36960
        assertThat(cart.total()).isEqualByComparingTo("36960");
        assertThat(cart.itemCount()).isEqualTo(4);
    }

    /** Törlés után üres kosár. */
    @Test
    void removeItem() {
        shop.cart().add("P004", 1);
        shop.cart().remove("P004");
        assertThat(shop.cart().current().items()).isEmpty();
    }

    /** Határértékek: 0 és 100 elutasítva. */
    @Test
    void quantityOutOfRangeIsRejected() {
        for (int qty : new int[] {0, 100}) {
            assertError(shop.cart().tryAdd("P001", qty), 400, "INVALID_QUANTITY");
        }
    }

    /** Ismeretlen termék és nem kosárban lévő tétel. */
    @Test
    void unknownAndMissingItems() {
        assertError(shop.cart().tryAdd("P999", 1), 404, "UNKNOWN_PRODUCT");
        assertError(shop.cart().tryUpdateQuantity("P001", 2), 404, "ITEM_NOT_IN_CART");
        assertError(shop.cart().tryRemove("P001"), 404, "ITEM_NOT_IN_CART");
    }

    /**
     * Közös ellenőrző segédmetódus a hibaválaszokhoz (a tesztszkript rétegen belüli újrahasznosítás).
     *
     * @param response  a nyers válasz
     * @param status    elvárt HTTP státusz
     * @param errorCode elvárt hibakód
     */
    private static void assertError(Response response, int status, String errorCode) {
        Problem problem = response.then().statusCode(status).extract().as(Problem.class);
        assertThat(problem.errorCode()).isEqualTo(errorCode);
    }
}
