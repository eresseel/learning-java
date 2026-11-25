import java.util.List;

/**
 * S – egy felelősség: CSAK a megjelenítésért felel. Ha a kiírás formája
 * változik, csak ezt az osztályt kell módosítani, a számolót nem.
 */
public class EredmenyKiiro {

    /**
     * @param sikidomok a síkidomok
     * @param osszeg    a már kiszámolt összterület
     */
    public void kiir(List<Sikidom> sikidomok, double osszeg) {
        for (Sikidom s : sikidomok) {
            System.out.printf("  %-24s %10.2f%n", s.nev(), s.terulet());
        }
        System.out.printf("  %-24s %10.2f%n", "Összesen:", osszeg);
    }
}
