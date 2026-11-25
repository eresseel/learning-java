package hu.learning.webshop.tesztautomatizalas.web.dto;

import jakarta.validation.constraints.NotNull;

/**
 * A {@code PATCH /cart/items/{productId}} kérés törzse.
 *
 * @param quantity az új mennyiség (felülírja a régit)
 */
public record UpdateQuantityRequest(@NotNull(message = "a quantity kötelező") Integer quantity) {
}
