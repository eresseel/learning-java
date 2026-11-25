/**
 * Kutya – a final kulcsszó miatt NEM származtatható tovább.
 * Plusz adata a marmagasság (minimum 10 cm).
 */
public final class Kutya extends Allat {

    /** A megengedett legkisebb marmagasság centiméterben. */
    public static final int MIN_MARMAGASSAG_CM = 10;

    private final int marmagassagCm;

    /**
     * @param nev           a kutya neve
     * @param sulyKg        a súly kilogrammban (legalább 2)
     * @param marmagassagCm a marmagasság centiméterben (legalább 10)
     * @throws IllegalArgumentException ha bármelyik adat érvénytelen
     */
    public Kutya(String nev, double sulyKg, int marmagassagCm) {
        // Az ős ellenőrzi a nevet és a súlyt (hiba esetén már itt kivétel keletkezik).
        super(nev, sulyKg);
        if (marmagassagCm < MIN_MARMAGASSAG_CM) {
            throw new IllegalArgumentException("A marmagasság legalább " + MIN_MARMAGASSAG_CM + " cm kell legyen!");
        }
        this.marmagassagCm = marmagassagCm;
    }

    /** @return a marmagasság centiméterben */
    public int getMarmagassagCm() {
        return marmagassagCm;
    }

    /**
     * Kutya napi ételadagja: súly / 50.
     *
     * @return a napi ételadag kilogrammban
     */
    @Override
    public double napiEtelAdag() {
        return getSulyKg() / 50;
    }

    /** @return a marmagasság szövegesen */
    @Override
    protected String sajatAdatok() {
        return "marmagasság: " + marmagassagCm + " cm";
    }
}
