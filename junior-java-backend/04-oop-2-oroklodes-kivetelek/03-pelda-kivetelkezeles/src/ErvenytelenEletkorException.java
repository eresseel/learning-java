/**
 * Saját (egyedi) kivételosztály: érvénytelen életkor esetén dobja a Szemely osztály.
 * <p>
 * Az Exception-ből származik, ezért ELLENŐRZÖTT kivétel: a fordító kikényszeríti,
 * hogy a hívó vagy elkapja (try-catch), vagy továbbadja (throws).
 */
public class ErvenytelenEletkorException extends Exception {

    /** A kivételek szerializálhatók; a verziószámot illik megadni. */
    private static final long serialVersionUID = 1L;

    /** Az elutasított érték – a hibakezelő is lekérdezheti. */
    private final int eletkor;

    /**
     * @param eletkor az érvénytelen életkor
     */
    public ErvenytelenEletkorException(int eletkor) {
        // Beszédes hibaüzenetet adunk át az ős (Exception) konstruktorának.
        super("Érvénytelen életkor: " + eletkor + " (0 és 130 között kell lennie)");
        this.eletkor = eletkor;
    }

    /** @return az elutasított életkor */
    public int getEletkor() {
        return eletkor;
    }
}
