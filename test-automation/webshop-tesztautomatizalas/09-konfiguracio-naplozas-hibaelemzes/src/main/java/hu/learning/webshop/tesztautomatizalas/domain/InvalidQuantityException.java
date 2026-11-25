package hu.learning.webshop.tesztautomatizalas.domain;

/** Akkor dobjuk, ha a kért mennyiség kívül esik a megengedett tartományon. */
public class InvalidQuantityException extends WebshopException {

    /**
     * @param quantity a hibás (vagy összeadás utáni) mennyiség
     */
    public InvalidQuantityException(int quantity) {
        super("Érvénytelen mennyiség: " + quantity + " (megengedett: "
                + Cart.MIN_QUANTITY + ".." + Cart.MAX_QUANTITY + ")");
    }
}
