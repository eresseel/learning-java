/**
 * Téglalap – a Sikidom egyik megvalósítása.
 */
public class Teglalap implements Sikidom {

    private final double a;
    private final double b;

    /**
     * @param a az egyik oldal
     * @param b a másik oldal
     */
    public Teglalap(double a, double b) {
        this.a = a;
        this.b = b;
    }

    /** @return a * b */
    @Override
    public double terulet() {
        return a * b;
    }

    /** @return "téglalap" */
    @Override
    public String nev() {
        return "téglalap " + a + " x " + b;
    }
}
