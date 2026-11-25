package hu.learning.feladatlapok.mockito;

/**
 * Üzleti szintű hiba: a számlaművelet technikai okból nem sikerült.
 * <p>
 * A {@link SzamlaService} ebbe csomagolja az {@link AdatbazisHibaException}-t,
 * így a hívó nem függ a tárolási réteg részleteitől; az eredeti hiba a
 * {@link #getCause()}-ból érhető el.
 */
public class SzamlaMuveletException extends RuntimeException {

    /**
     * Létrehozza a kivételt az eredeti okkal.
     *
     * @param uzenet a hiba leírása
     * @param ok     az eredeti (technikai) kivétel
     */
    public SzamlaMuveletException(String uzenet, Throwable ok) {
        super(uzenet, ok);
    }
}
