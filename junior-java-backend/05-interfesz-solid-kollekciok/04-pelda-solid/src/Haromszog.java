/**
 * Háromszög – UTÓLAG hozzáadott síkidom. A TeruletOsszegzo-t ehhez
 * egyáltalán nem kellett módosítani (nyitottság és zártság elve).
 */
public class Haromszog implements Sikidom {

    private final double a;
    private final double ma;

    /**
     * @param a  az alap
     * @param ma az alaphoz tartozó magasság
     */
    public Haromszog(double a, double ma) {
        this.a = a;
        this.ma = ma;
    }

    /** @return (a * ma) / 2 */
    @Override
    public double terulet() {
        return a * ma / 2;
    }

    /** @return "háromszög" */
    @Override
    public String nev() {
        return "háromszög a=" + a + ", ma=" + ma;
    }
}
