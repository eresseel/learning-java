/**
 * Száztól hármasával kiírja a számokat egészen az első olyan számig
 * (azt is beleértve), amely 16-tal maradék nélkül osztható.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // Itt előre nem tudjuk, hány kört kell menni, ezért feltételes ciklus kell.
        // Hátultesztelős (do-while) ciklust választunk: a 100-at mindenképp ki kell
        // írni, és a feltételt csak a kiírás UTÁN vizsgáljuk – így a 16-tal osztható
        // szám is kiíródik, majd a ciklus megáll.
        int szam = 100;
        do {
            System.out.print(szam + " ");
            szam += 3;
            // A (szam - 3) az imént kiírt szám: addig folytatjuk, amíg az NEM
            // osztható 16-tal (a % maradékos osztás eredménye 0, ha osztható).
        } while ((szam - 3) % 16 != 0);
        System.out.println();

        // Ugyanez elöltesztelős (while) ciklussal is megoldható, ha a feltételt
        // a kiírandó számra fogalmazzuk meg, és az utolsó számot a ciklus után írjuk ki:
        int aktualis = 100;
        while (aktualis % 16 != 0) {
            System.out.print(aktualis + " ");
            aktualis += 3;
        }
        System.out.println(aktualis + "  <- az első 16-tal osztható szám");
    }
}
