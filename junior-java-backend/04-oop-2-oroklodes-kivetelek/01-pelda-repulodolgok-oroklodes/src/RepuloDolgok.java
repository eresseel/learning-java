/**
 * Repülő dolgok – egy általános, még NEM absztrakt ősosztály.
 * <p>
 * Az öröklődés egy specializálódás: a RepuloDolgok-ból származik a Madar,
 * a Madar-ból pedig a Sas és a Vereb. A leszármazott objektum magában
 * hordoz egy ős objektumot is, amelyet a super kulcsszóval érünk el.
 */
public class RepuloDolgok {

    /** protected: a leszármazottak (más csomagból is) elérik. */
    protected String nev;

    /**
     * Az ősnek csak paraméteres konstruktora van, ezért a leszármazottaknak
     * kötelező a super(nev) hívás – és NEM megoldás erre egy fölösleges
     * paraméter nélküli konstruktor írása (a diasor szerinti gyakori hiba).
     *
     * @param nev a repülő dolog megnevezése
     */
    public RepuloDolgok(String nev) {
        this.nev = nev;
    }

    /** @return a megnevezés */
    public String getNev() {
        return nev;
    }

    /**
     * Általános repülés – a leszármazottak felülbírálhatják.
     */
    public void repul() {
        System.out.println(nev + " valahogyan repül.");
    }
}
