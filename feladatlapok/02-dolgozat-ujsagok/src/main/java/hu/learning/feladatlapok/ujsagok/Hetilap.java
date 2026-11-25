package hu.learning.feladatlapok.ujsagok;

/**
 * Hetilap: hetente egyszer, a hét egy adott napján megjelenő újság.
 * <p>
 * Az ősosztály adatain túl ismert, hogy a hét hányadik napján jelenik meg (1..7,
 * ahol 1 = hétfő, 7 = vasárnap).
 */
public class Hetilap extends Ujsag {

    /** A hét napjainak neve, a megjelenés napjának olvasható kiírásához. */
    private static final String[] NAPOK = {
            "hétfő", "kedd", "szerda", "csütörtök", "péntek", "szombat", "vasárnap"
    };

    private int hetNapja;

    /**
     * Létrehoz egy ellenőrzött hetilapot.
     *
     * @param megnevezes a lap neve
     * @param kiado      a kiadó neve
     * @param ar         egy példány ára forintban
     * @param hetNapja   a hét hányadik napján jelenik meg (1..7)
     * @throws IllegalArgumentException ha valamelyik adat érvénytelen
     */
    public Hetilap(String megnevezes, String kiado, int ar, int hetNapja) {
        super(megnevezes, kiado, ar);
        setHetNapja(hetNapja);
    }

    /**
     * Osztályszintű ellenőrzés: a hét napja 1 és 7 közötti szám.
     *
     * @param hetNapja a vizsgált érték
     * @return ugyanaz az érték, ha érvényes
     * @throws IllegalArgumentException ha nem 1..7
     */
    public static int ellenorizHetNapja(int hetNapja) {
        if (hetNapja < 1 || hetNapja > 7) {
            throw new IllegalArgumentException(
                    "A hét napja 1 és 7 közötti szám lehet! (megadott: " + hetNapja + ")");
        }
        return hetNapja;
    }

    /** @return „Hetilap” */
    @Override
    public String tipus() {
        return "Hetilap";
    }

    /** @return a megjelenés napja (1..7) */
    public int getHetNapja() {
        return hetNapja;
    }

    /**
     * Beállítja a megjelenés napját.
     *
     * @param hetNapja az új érték (1..7)
     * @throws IllegalArgumentException ha érvénytelen
     */
    public void setHetNapja(int hetNapja) {
        this.hetNapja = ellenorizHetNapja(hetNapja);
    }

    /** @return az ős adatai kiegészítve a megjelenés napjával */
    @Override
    protected String adatok() {
        // A tömb 0-tól indexel, a hét napja 1-től – ezért a -1.
        return super.adatok() + ", megjelenés: a hét " + hetNapja + ". napja (" + NAPOK[hetNapja - 1] + ")";
    }
}
