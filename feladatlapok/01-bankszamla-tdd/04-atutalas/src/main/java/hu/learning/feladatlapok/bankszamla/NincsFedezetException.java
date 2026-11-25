package hu.learning.feladatlapok.bankszamla;

/**
 * Akkor dobjuk, ha egy kifizetéshez vagy átutaláshoz nincs elég pénz a számlán.
 * Nem ellenőrzött (RuntimeException) kivétel, így a hívónak nem kötelező
 * elkapnia, de a tesztben {@code assertThrows}-zal ellenőrizhető.
 */
public class NincsFedezetException extends RuntimeException {

    /**
     * @param uzenet a hiba leírása (az egyenleggel és a kért összeggel)
     */
    public NincsFedezetException(String uzenet) {
        super(uzenet);
    }
}
