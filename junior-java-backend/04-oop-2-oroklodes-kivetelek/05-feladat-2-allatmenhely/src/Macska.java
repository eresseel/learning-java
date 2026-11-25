/**
 * Macska – final, tehát nem származtatható tovább.
 * Plusz adata, hogy rövid- vagy hosszúszőrű.
 */
public final class Macska extends Allat {

    private final boolean rovidszoru;

    /**
     * @param nev        a macska neve
     * @param sulyKg     a súly kilogrammban (legalább 2)
     * @param rovidszoru true: rövidszőrű, false: hosszúszőrű
     * @throws IllegalArgumentException ha a név vagy a súly érvénytelen
     */
    public Macska(String nev, double sulyKg, boolean rovidszoru) {
        super(nev, sulyKg);
        this.rovidszoru = rovidszoru;
    }

    /** @return true, ha rövidszőrű */
    public boolean isRovidszoru() {
        return rovidszoru;
    }

    /**
     * Macska napi ételadagja: súly / 70.
     *
     * @return a napi ételadag kilogrammban
     */
    @Override
    public double napiEtelAdag() {
        return getSulyKg() / 70;
    }

    /** @return a szőrhossz szövegesen */
    @Override
    protected String sajatAdatok() {
        return rovidszoru ? "rövidszőrű" : "hosszúszőrű";
    }
}
