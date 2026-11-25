/**
 * Repülőgép: a Jarmu másik specializált leszármazottja.
 */
public class Repulo extends Jarmu {

    /**
     * @param nev a repülőgép megnevezése
     */
    public Repulo(String nev) {
        super(nev);
    }

    /**
     * A repülő is indul, de egészen másképp, mint az autó.
     */
    @Override
    public void inditas() {
        System.out.println(nev + ": hajtóművek indítása, gurulás a kifutóra, felszállás.");
    }
}
