/**
 * Veréb: madár, amely repül.
 */
public class Vereb extends Madarak implements RepulInterface {

    /**
     * @param nev a veréb neve
     */
    public Vereb(String nev) {
        super(nev);
    }

    /** A veréb gyors szárnycsapásokkal repül. */
    @Override
    public void repul() {
        System.out.println(nev + " (veréb) szaporán csapkod a szárnyaival.");
    }
}
