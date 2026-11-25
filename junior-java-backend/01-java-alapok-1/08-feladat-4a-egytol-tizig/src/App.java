/**
 * Kiírja a számokat egytől tízig számlálós (for) ciklussal.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // A for ciklus három része:
        //   inicializálás (int i = 1) – egyszer, a ciklus előtt fut le,
        //   feltétel (i <= 10)        – minden kör ELŐTT kiértékelődik,
        //   növekmény (i++)           – minden kör UTÁN végrehajtódik.
        for (int i = 1; i <= 10; i++) {
            // print + szóköz: a számok egy sorban, szóközzel elválasztva jelennek meg
            System.out.print(i + " ");
        }
        // A végén egy sortörés, hogy a konzol promptja új sorba kerüljön.
        System.out.println();
    }
}
