/**
 * Veréb: a Madar leszármazottja, amely NEM írja felül a repul() metódust,
 * ezért a Madar által felülírt változat fut le rajta.
 */
public class Vereb extends Madar {

    /**
     * @param nev a veréb neve
     */
    public Vereb(String nev) {
        super(nev, 23);
    }
}
