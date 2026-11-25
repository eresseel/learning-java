package hu.learning.webshop.tesztautomatizalas.service;

import hu.learning.webshop.tesztautomatizalas.domain.EmptyCartException;
import hu.learning.webshop.tesztautomatizalas.payment.PaymentClient;
import hu.learning.webshop.tesztautomatizalas.payment.PaypalOrderResponse;
import hu.learning.webshop.tesztautomatizalas.web.dto.CartResponse;
import hu.learning.webshop.tesztautomatizalas.web.dto.CheckoutResponse;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * A fizetési folyamat: kosár végösszege → terhelés a szolgáltatónál → sikernél kosár ürítése.
 *
 * <p>Szabály: ha a fizetés bármilyen okból nem sikerül, a kosár <b>változatlan</b> marad,
 * hogy a vásárló újra próbálkozhasson. (Egyszerűsítés: a külső hívás a kosár zárolása alatt
 * történik – éles rendszerben ezt rendelés-állapotgéppel oldanánk meg.)</p>
 */
@Service
public class CheckoutService {

    /** A közös kosár. */
    private final CartService cartService;
    /** A külső fizetési kliens. */
    private final PaymentClient paymentClient;

    /**
     * @param cartService   kosár szolgáltatás
     * @param paymentClient PayPal kliens
     */
    public CheckoutService(CartService cartService, PaymentClient paymentClient) {
        this.cartService = cartService;
        this.paymentClient = paymentClient;
    }

    /**
     * Kifizeti a kosár tartalmát.
     *
     * @return a rendelés adatai
     * @throws EmptyCartException ha a kosár üres
     * @throws hu.learning.webshop.tesztautomatizalas.payment.PaymentException ha a fizetés nem sikerült
     */
    public CheckoutResponse checkout() {
        return cartService.withCart(cart -> {
            // 1. Előfeltétel: van mit kifizetni
            if (cart.isEmpty()) {
                throw new EmptyCartException();
            }
            // 2. Saját rendelésazonosító (egyben a PayPal idempotencia-kulcsa)
            String orderId = "ORD-" + UUID.randomUUID();
            BigDecimal total = cart.total();
            // 3. Terhelés – kivétel esetén ide már nem jutunk el, a kosár megmarad
            PaypalOrderResponse payment = paymentClient.charge(orderId, total, CartResponse.CURRENCY);
            // 4. Siker: kosár ürítése, válasz összeállítása
            cart.clear();
            return new CheckoutResponse(orderId, payment.id(), "PAID", total, CartResponse.CURRENCY);
        });
    }
}
