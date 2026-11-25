package hu.learning.webshop.tesztautomatizalas.web.dto;

import hu.learning.webshop.tesztautomatizalas.domain.Cart;
import java.math.BigDecimal;
import java.util.List;

/**
 * A teljes kosár JSON-ábrázolása.
 *
 * @param items     a tételek
 * @param itemCount a darabok száma összesen
 * @param total     végösszeg
 * @param currency  pénznem (mindig {@code HUF})
 */
public record CartResponse(List<CartItemResponse> items, int itemCount, BigDecimal total, String currency) {

    /** A webáruház pénzneme. */
    public static final String CURRENCY = "HUF";

    /**
     * @param cart a domain kosár
     * @return a pillanatnyi állapot API-ábrázolása
     */
    public static CartResponse from(Cart cart) {
        // A tételeket stream-mel alakítjuk DTO-vá
        List<CartItemResponse> items = cart.getItems().stream().map(CartItemResponse::from).toList();
        return new CartResponse(items, cart.itemCount(), cart.total(), CURRENCY);
    }
}
