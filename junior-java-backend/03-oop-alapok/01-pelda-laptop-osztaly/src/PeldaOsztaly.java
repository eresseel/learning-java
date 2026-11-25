/**
 * Egy szándékosan "üres" osztály a diasor példája alapján (class PeldaOsztaly { }).
 * <p>
 * Nem írtunk bele konstruktort, mégis példányosítható: a Java ilyenkor
 * automatikusan biztosít egy láthatatlan, paraméter nélküli (default) konstruktort.
 * <p>
 * Emellett bemutatja a négy láthatósági szintet is.
 */
public class PeldaOsztaly {

    /** private: csak ezen az osztályon belül látható. */
    private int csakBelul = 1;

    /** Nincs módosító (package-private): a csomagon belül mindenki látja. */
    int csomagonBelul = 2;

    /** protected: a csomagon belül mindenki, más csomagból csak a leszármazottak látják. */
    protected int leszarmazottaknak = 3;

    /** public: mindenkinek látható. */
    public int mindenkinek = 4;

    /**
     * Publikus metódus, amely a privát mezőt is eléri (osztályon belül szabad).
     *
     * @return a négy mező összege
     */
    public int osszeg() {
        return csakBelul + csomagonBelul + leszarmazottaknak + mindenkinek;
    }
}
