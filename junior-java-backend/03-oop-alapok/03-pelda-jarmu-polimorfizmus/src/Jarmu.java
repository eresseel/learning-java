/**
 * Általános jármű – az öröklési hierarchia őse.
 * <p>
 * A diasor példája: van egy megírt Jarmu osztályunk, és ebből származtatjuk
 * az Auto és a Repulo osztályt. A Jarmu előírja, hogy minden járműnek legyen
 * inditas() funkciója, de a leszármazottak ezt másképp valósítják meg.
 */
public class Jarmu {

    /** A jármű megnevezése; protected, hogy a leszármazottak is elérjék. */
    protected String nev;

    /**
     * @param nev a jármű megnevezése
     */
    public Jarmu(String nev) {
        this.nev = nev;
    }

    /**
     * Elindítja a járművet. Az ős általános megvalósítást ad,
     * amelyet a leszármazottak felülírhatnak.
     */
    public void inditas() {
        System.out.println(nev + ": a jármű elindul.");
    }
}
