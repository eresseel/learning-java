/**
 * Madár: a RepuloDolgok specializált leszármazottja (class Madar extends RepuloDolgok).
 */
public class Madar extends RepuloDolgok {

    /** A szárnyfesztávolság centiméterben – csak a madarakra jellemző adat. */
    protected int szarnyfesztavCm;

    /**
     * @param nev             a madár neve
     * @param szarnyfesztavCm a szárnyfesztávolság cm-ben
     */
    public Madar(String nev, int szarnyfesztavCm) {
        // 1. Először az ős konstruktorát kell meghívni (ez mindig az első utasítás).
        super(nev);
        // 2. Utána a saját mezők beállítása.
        this.szarnyfesztavCm = szarnyfesztavCm;
    }

    /**
     * Felülbírálja (override) az ős repul() metódusát.
     * A Sas és a Vereb is ezt örökli, hacsak újra felül nem írják.
     */
    @Override
    public void repul() {
        System.out.println(nev + " csapkod a szárnyaival (" + szarnyfesztavCm + " cm fesztáv).");
    }
}
