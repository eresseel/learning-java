/**
 * Labda: játék, amely (eldobva) repül.
 */
public class Labda extends Jatekok implements RepulInterface {

    /**
     * @param nev a labda megnevezése
     */
    public Labda(String nev) {
        super(nev);
    }

    /** A labda ívben repül. */
    @Override
    public void repul() {
        System.out.println(nev + " (labda) ívben repül, majd leesik.");
    }
}
