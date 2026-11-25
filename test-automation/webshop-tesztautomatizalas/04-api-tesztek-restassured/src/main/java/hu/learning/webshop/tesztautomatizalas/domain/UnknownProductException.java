package hu.learning.webshop.tesztautomatizalas.domain;

/** Akkor dobjuk, ha a megadott termékazonosító nem szerepel a katalógusban. */
public class UnknownProductException extends WebshopException {

    /**
     * @param productId a nem létező termék azonosítója
     */
    public UnknownProductException(String productId) {
        super("Ismeretlen termék: " + productId);
    }
}
