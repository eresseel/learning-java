/**
 * Labda: ő is "repül", ha eldobják – de egészen másképp, mint a madár vagy a repülőgép.
 */
public class Labda extends RepuloDolgok {

    /**
     * @param nev a labda megnevezése
     */
    public Labda(String nev) {
        super(nev);
    }

    /** A labda ívben repül, majd leesik. */
    @Override
    public void repul() {
        System.out.println(nev + " ívben repül, aztán leesik.");
    }
}
