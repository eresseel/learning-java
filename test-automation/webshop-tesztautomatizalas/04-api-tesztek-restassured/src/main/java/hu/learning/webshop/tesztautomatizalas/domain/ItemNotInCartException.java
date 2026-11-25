package hu.learning.webshop.tesztautomatizalas.domain;

/** Akkor dobjuk, ha olyan tételt módosítanánk/törölnénk, ami nincs a kosárban. */
public class ItemNotInCartException extends WebshopException {

    /**
     * @param productId a kosárban nem található termék azonosítója
     */
    public ItemNotInCartException(String productId) {
        super("A termék nincs a kosárban: " + productId);
    }
}
