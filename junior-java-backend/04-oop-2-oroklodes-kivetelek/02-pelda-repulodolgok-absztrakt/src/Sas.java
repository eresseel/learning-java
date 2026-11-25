/**
 * Sas: a leszármazottban implementált (az ősben eredetileg absztrakt)
 * metódus a további öröklődésben felülírható – a Sas ezt meg is teszi.
 */
public class Sas extends Madar {

    /**
     * @param nev a sas neve
     */
    public Sas(String nev) {
        super(nev);
    }

    /** A sas vitorlázva repül. */
    @Override
    public void repul() {
        System.out.println(nev + " kiterjesztett szárnyakkal vitorlázik a magasban.");
    }
}
