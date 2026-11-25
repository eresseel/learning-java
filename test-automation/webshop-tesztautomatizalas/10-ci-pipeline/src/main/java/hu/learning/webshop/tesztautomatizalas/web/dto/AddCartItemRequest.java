package hu.learning.webshop.tesztautomatizalas.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * A {@code POST /cart/items} kérés törzse.
 *
 * <p>A Bean Validation csak a <em>formai</em> hibát szűri (hiányzó mező → 400);
 * a mennyiség tartományát (1..99) a domain ellenőrzi – egy helyen él az üzleti szabály.</p>
 *
 * @param productId a termék azonosítója
 * @param quantity  a hozzáadandó mennyiség
 */
public record AddCartItemRequest(
        @NotBlank(message = "a productId kötelező") String productId,
        @NotNull(message = "a quantity kötelező") Integer quantity) {
}
