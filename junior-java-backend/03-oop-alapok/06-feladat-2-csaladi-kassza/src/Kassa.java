/**
 * Egyszerűsített családi kassza: nyilvántartja az egyenleget,
 * és elvégzi a bevétel rögzítését, illetve a kiadás könyvelését.
 * <p>
 * A menü csak bekér és kiír; minden kasszaművelet ebben az osztályban
 * történik (egységbezárás). Az egyenleg kívülről nem írható közvetlenül.
 */
public class Kassa {

    /** A kassza kezdő egyenlege forintban (a feladat szerint 300 ezer Ft). */
    public static final int KEZDO_EGYENLEG = 300_000;

    /** Az aktuális egyenleg; csak a bevetel/kiadas metódusok módosítják. */
    private int egyenleg;

    /**
     * Létrehoz egy kasszát a kezdő egyenleggel.
     */
    public Kassa() {
        this.egyenleg = KEZDO_EGYENLEG;
    }

    /**
     * Bevétel rögzítése.
     *
     * @param osszeg a bevétel összege forintban (pozitív)
     * @return true, ha a rögzítés megtörtént; false érvénytelen összegnél
     */
    public boolean bevetel(int osszeg) {
        if (osszeg <= 0) {
            return false;
        }
        egyenleg += osszeg;
        return true;
    }

    /**
     * Kiadás könyvelése. A kassza nem mehet mínuszba, ezért a fedezetet ellenőrizzük.
     *
     * @param osszeg a kiadás összege forintban (pozitív)
     * @return true, ha a könyvelés megtörtént; false, ha az összeg érvénytelen
     *         vagy nincs rá fedezet
     */
    public boolean kiadas(int osszeg) {
        // 1. Érvénytelen összeg vagy fedezethiány esetén nem könyvelünk.
        if (osszeg <= 0 || osszeg > egyenleg) {
            return false;
        }
        // 2. Egyébként levonjuk az egyenlegből.
        egyenleg -= osszeg;
        return true;
    }

    /**
     * Egyenleg lekérdezése (csak getter, setter nincs!).
     *
     * @return az aktuális egyenleg forintban
     */
    public int getEgyenleg() {
        return egyenleg;
    }
}
