/**
 * Repülőgép: jármű, amely repül.
 */
public class Repulo extends Jarmuvek implements RepulInterface {

    /**
     * @param nev a repülőgép neve
     */
    public Repulo(String nev) {
        super(nev);
    }

    /** A repülőgép hajtóművel repül. */
    @Override
    public void repul() {
        System.out.println(nev + " (repülőgép) hajtóművekkel száll a felhők felett.");
    }
}
