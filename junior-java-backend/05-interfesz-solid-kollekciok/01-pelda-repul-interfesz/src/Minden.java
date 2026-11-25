/**
 * A hierarchia legfelső, absztrakt őse: "minden" dolog a világban.
 * <p>
 * Nem írhatja elő a repul() metódust, mert nem minden dolog tud repülni
 * (pl. az autó vagy a kutya nem). Ezért kell a RepulInterface.
 */
public abstract class Minden {

    protected final String nev;

    /**
     * @param nev a dolog megnevezése
     */
    protected Minden(String nev) {
        this.nev = nev;
    }

    /** @return a megnevezés */
    public String getNev() {
        return nev;
    }

    /**
     * Minden dolog be tud mutatkozni – a hierarchiából fakadó közös viselkedés.
     *
     * @return a dolog rövid leírása (osztálynév és megnevezés)
     */
    public String bemutatkozik() {
        return getClass().getSimpleName() + " (" + nev + ")";
    }
}
