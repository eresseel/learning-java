/**
 * Madár: megvalósítja az ősben absztrakt repul() metódust.
 */
public class Madar extends RepuloDolgok {

    /**
     * @param nev a madár neve
     */
    public Madar(String nev) {
        super(nev);
    }

    /** A madár szárnycsapással repül. */
    @Override
    public void repul() {
        System.out.println(nev + " csapkod a szárnyaival.");
    }
}
