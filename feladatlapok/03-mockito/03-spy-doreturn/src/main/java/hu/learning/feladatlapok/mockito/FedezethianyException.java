package hu.learning.feladatlapok.mockito;

/**
 * Akkor dobjuk, ha a kifizetéshez nincs elég fedezet a számlán.
 */
public class FedezethianyException extends RuntimeException {

    /**
     * Létrehozza a kivételt a megadott üzenettel.
     *
     * @param uzenet a hiba leírása
     */
    public FedezethianyException(String uzenet) {
        super(uzenet);
    }
}
