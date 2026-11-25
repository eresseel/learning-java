package hu.learning.webshop.tesztautomatizalas.domain;

import java.math.BigDecimal;

/**
 * Egy kosártétel: termék és darabszám.
 *
 * @param product  a termék
 * @param quantity darabszám ({@link Cart#MIN_QUANTITY}..{@link Cart#MAX_QUANTITY})
 */
public record CartItem(Product product, int quantity) {

    /**
     * @return a tétel sorösszege: egységár × darabszám
     */
    public BigDecimal lineTotal() {
        return product.price().multiply(BigDecimal.valueOf(quantity));
    }
}
