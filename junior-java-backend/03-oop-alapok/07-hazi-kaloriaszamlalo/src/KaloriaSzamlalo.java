/**
 * Napi kalóriaszámláló egy személy számára.
 * <p>
 * Adattagok: név, napi kalória. A táplálkozás növeli, a sportolás és a
 * pihenés csökkenti (elégeti) a napi kalóriaértéket.
 */
public class KaloriaSzamlalo {

    /** Egy sportolással töltött óra alatt elégetett kalória. */
    public static final int SPORT_KALORIA_ORANKENT = 600;
    /** Egy pihenéssel töltött óra alatt elégetett kalória. */
    public static final int PIHENES_KALORIA_ORANKENT = 40;

    private final String nev;
    private double napiKaloria;

    /**
     * Új számláló a nap elejére, 0 kalóriával.
     *
     * @param nev a személy neve
     */
    public KaloriaSzamlalo(String nev) {
        this.nev = nev;
        this.napiKaloria = 0;
    }

    /** @return a személy neve */
    public String getNev() {
        return nev;
    }

    /** @return az eddig összesített napi kalóriaérték */
    public double getNapiKaloria() {
        return napiKaloria;
    }

    /**
     * Táplálkozás: a megadott kalóriamennyiséggel nő a napi érték.
     *
     * @param kaloria az elfogyasztott étel kalóriatartalma (nemnegatív)
     */
    public void taplalkozas(double kaloria) {
        if (kaloria > 0) {
            napiKaloria += kaloria;
        }
    }

    /**
     * Sportolás: minden sportolással töltött óra 600 kalóriát éget el.
     *
     * @param ora a sporttal töltött órák száma (lehet tört is, pl. 1,5)
     */
    public void sportolas(double ora) {
        if (ora > 0) {
            napiKaloria -= ora * SPORT_KALORIA_ORANKENT;
        }
    }

    /**
     * Pihenés: minden pihenéssel töltött óra 40 kalóriát éget el.
     *
     * @param ora a pihenéssel töltött órák száma
     */
    public void pihenes(double ora) {
        if (ora > 0) {
            napiKaloria -= ora * PIHENES_KALORIA_ORANKENT;
        }
    }
}
