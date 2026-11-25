package hu.learning.webshop.tesztautomatizalas.tests;

import static org.assertj.core.api.Assertions.assertThat;

import hu.learning.webshop.tesztautomatizalas.taf.core.Problem;
import hu.learning.webshop.tesztautomatizalas.web.dto.CheckoutResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** A fizetés tesztszkriptjei – a PayPal-t a core réteg szimulátora vezérli, szándék szerint. */
@DisplayName("Tesztszkript – fizetés")
class CheckoutTest extends AbstractShopTest {

    /** Teljes vásárlás egy facade-hívással. */
    @Test
    void buyTwoMice() {
        // Arrange
        PAYPAL.willComplete("PAY-OK");
        // Act – Facade: kosár feltöltése + fizetés egy lépésben
        CheckoutResponse order = shop.buy(Map.of("P001", 2));
        // Assert
        assertThat(order.status()).isEqualTo("PAID");
        assertThat(order.total()).isEqualByComparingTo("15980");
        assertThat(shop.cart().current().items()).isEmpty();
        PAYPAL.verifyChargedOnce("15980.00");
    }

    /** Elutasított fizetés: 402, a kosár megmarad. */
    @Test
    void declinedPaymentKeepsCart() {
        shop.cart().add("P001", 2);
        PAYPAL.willDecline();
        assertThat(shop.checkout().tryPay().then().statusCode(402).extract().as(Problem.class).errorCode())
                .isEqualTo("PAYMENT_DECLINED");
        assertThat(shop.cart().current().itemCount()).isEqualTo(2);
    }

    /** Szolgáltatói hiba és időtúllépés. */
    @Test
    void providerErrorAndTimeout() {
        shop.cart().add("P001", 1);
        PAYPAL.willFail(503);
        shop.checkout().tryPay().then().statusCode(502);
        PAYPAL.willRespondSlowly(1500);
        shop.checkout().tryPay().then().statusCode(504);
    }

    /** Üres kosár: 409, a PayPal-t nem hívjuk. */
    @Test
    void emptyCartIsRejected() {
        shop.checkout().tryPay().then().statusCode(409);
        PAYPAL.verifyNotCalled();
    }
}
