package hu.learning.webshop.tesztautomatizalas.web.dto;

import hu.learning.webshop.tesztautomatizalas.domain.CartItem;
import java.math.BigDecimal;

/**
 * Egy kosártétel JSON-ábrázolása.
 *
 * @param productId termékazonosító
 * @param name      terméknév
 * @param unitPrice egységár
 * @param quantity  mennyiség
 * @param lineTotal sorösszeg
 */
public record CartItemResponse(String productId, String name, BigDecimal unitPrice,
                               int quantity, BigDecimal lineTotal) {

    /**
     * Domain-objektumból API-objektum (a domain így nem függ a JSON formától).
     *
     * @param item a kosártétel
     * @return a válasz-DTO
     */
    public static CartItemResponse from(CartItem item) {
        return new CartItemResponse(item.product().id(), item.product().name(),
                item.product().price(), item.quantity(), item.lineTotal());
    }
}
