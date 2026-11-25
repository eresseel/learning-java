package hu.learning.feladatlapok.ujsagok;

/**
 * Napilap: olyan újság, amely egy héten legalább négyszer megjelenik.
 * <p>
 * Az ősosztály adatain túl ismert a heti megjelenések száma (4..7).
 */
public class Napilap extends Ujsag {

    /** A napilaphoz szükséges minimális heti megjelenés. */
    public static final int MIN_HETI_MEGJELENES = 4;
    /** Egy hétben legfeljebb 7 nap van, így ennél többször nem jelenhet meg. */
    public static final int MAX_HETI_MEGJELENES = 7;

    private int hetiMegjelenes;

    /**
     * Létrehoz egy ellenőrzött napilapot.
     *
     * @param megnevezes     a lap neve
     * @param kiado          a kiadó neve
     * @param ar             egy példány ára forintban
     * @param hetiMegjelenes hányszor jelenik meg egy héten (4..7)
     * @throws IllegalArgumentException ha valamelyik adat érvénytelen
     */
    public Napilap(String megnevezes, String kiado, int ar, int hetiMegjelenes) {
        // Először az ős konstruktora ellenőrzi a közös mezőket...
        super(megnevezes, kiado, ar);
        // ...majd a saját setter a napilap-specifikus szabályt.
        setHetiMegjelenes(hetiMegjelenes);
    }

    /**
     * Osztályszintű ellenőrzés: napilap az, ami legalább négyszer jelenik meg egy héten.
     *
     * @param hetiMegjelenes a vizsgált érték
     * @return ugyanaz az érték, ha érvényes
     * @throws IllegalArgumentException ha nem 4 és 7 közé esik
     */
    public static int ellenorizHetiMegjelenes(int hetiMegjelenes) {
        if (hetiMegjelenes < MIN_HETI_MEGJELENES || hetiMegjelenes > MAX_HETI_MEGJELENES) {
            throw new IllegalArgumentException("Napilap legalább " + MIN_HETI_MEGJELENES
                    + "-szer, legfeljebb " + MAX_HETI_MEGJELENES
                    + "-szer jelenik meg egy héten! (megadott: " + hetiMegjelenes + ")");
        }
        return hetiMegjelenes;
    }

    /**
     * Napilapból minden megjelenéskor veszünk egy példányt, így a heti
     * példányszám a heti megjelenések száma. Ezzel „hangoljuk” az ősosztály
     * final {@link #hetiArkalkulacio()} metódusát anélkül, hogy felülírnánk.
     *
     * @return a heti megjelenések száma
     */
    @Override
    protected int hetiPeldanyszam() {
        return hetiMegjelenes;
    }

    /** @return „Napilap” */
    @Override
    public String tipus() {
        return "Napilap";
    }

    /** @return a heti megjelenések száma */
    public int getHetiMegjelenes() {
        return hetiMegjelenes;
    }

    /**
     * Beállítja a heti megjelenések számát.
     *
     * @param hetiMegjelenes az új érték (4..7)
     * @throws IllegalArgumentException ha érvénytelen
     */
    public void setHetiMegjelenes(int hetiMegjelenes) {
        this.hetiMegjelenes = ellenorizHetiMegjelenes(hetiMegjelenes);
    }

    /** @return az ős adatai kiegészítve a heti megjelenéssel */
    @Override
    protected String adatok() {
        return super.adatok() + ", heti megjelenés: " + hetiMegjelenes;
    }
}
