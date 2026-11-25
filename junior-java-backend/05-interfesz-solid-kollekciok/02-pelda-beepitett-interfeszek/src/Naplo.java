import java.io.Closeable;

/**
 * Egy egyszerű napló "erőforrás", amely implementálja a Closeable interfészt:
 * a close() metódusban szabadítja fel a használt erőforrást.
 * A try-with-resources szerkezet a blokk végén automatikusan meghívja a close()-t.
 */
public class Naplo implements Closeable {

    private final String nev;
    private int bejegyzesek;

    /**
     * Megnyitja a naplót.
     *
     * @param nev a napló neve
     */
    public Naplo(String nev) {
        this.nev = nev;
        System.out.println("[" + nev + "] megnyitva");
    }

    /**
     * Új bejegyzés írása.
     *
     * @param szoveg a bejegyzés szövege
     */
    public void ir(String szoveg) {
        bejegyzesek++;
        System.out.println("[" + nev + "] " + bejegyzesek + ". " + szoveg);
    }

    /** Lezárja a naplót (felszabadítja az erőforrást). */
    @Override
    public void close() {
        System.out.println("[" + nev + "] lezárva, " + bejegyzesek + " bejegyzés");
    }
}
