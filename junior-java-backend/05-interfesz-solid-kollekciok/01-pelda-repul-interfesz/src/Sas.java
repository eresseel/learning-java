/**
 * Sas: madár, amely repül – ezért implementálja a RepulInterface-t.
 * Egy osztály egyszerre örökölhet (extends, csak egyből) és implementálhat
 * (implements, akár több interfészt is).
 */
public class Sas extends Madarak implements RepulInterface {

    /**
     * @param nev a sas neve
     */
    public Sas(String nev) {
        super(nev);
    }

    /** A sas vitorlázva repül. */
    @Override
    public void repul() {
        System.out.println(nev + " (sas) kiterjesztett szárnyakkal vitorlázik.");
    }
}
