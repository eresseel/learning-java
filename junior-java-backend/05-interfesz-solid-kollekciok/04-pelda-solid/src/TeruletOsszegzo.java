import java.util.List;

/**
 * S – egy felelősség: CSAK számol (a megjelenítés az EredmenyKiiro dolga).
 * O – új síkidomtípus esetén nem kell módosítani, mert a Sikidom absztrakcióval dolgozik.
 * L – bármely Sikidom megvalósítás behelyettesíthető, mindegyikkel helyesen működik.
 */
public class TeruletOsszegzo {

    /**
     * @param sikidomok a síkidomok listája
     * @return a területek összege
     */
    public double osszeg(List<Sikidom> sikidomok) {
        double osszeg = 0;
        for (Sikidom s : sikidomok) {
            osszeg += s.terulet();
        }
        return osszeg;
    }
}
