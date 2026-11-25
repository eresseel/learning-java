package hu.learning.webshop.tesztautomatizalas.domain;

/** Üres kosárral nem lehet fizetni. */
public class EmptyCartException extends WebshopException {

    /** Létrehozza a kivételt a rögzített üzenettel. */
    public EmptyCartException() {
        super("Üres kosárral nem lehet fizetni.");
    }
}
