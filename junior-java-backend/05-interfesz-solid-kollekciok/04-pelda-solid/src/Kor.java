/**
 * Kör – a Sikidom másik megvalósítása.
 */
public class Kor implements Sikidom {

    private final double r;

    /**
     * @param r a sugár
     */
    public Kor(double r) {
        this.r = r;
    }

    /** @return r^2 * π */
    @Override
    public double terulet() {
        return r * r * Math.PI;
    }

    /** @return "kör" */
    @Override
    public String nev() {
        return "kör r=" + r;
    }
}
