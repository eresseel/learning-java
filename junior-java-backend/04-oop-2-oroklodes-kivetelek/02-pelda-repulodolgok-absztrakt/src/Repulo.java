/**
 * Repülőgép: a RepuloDolgok közvetlen leszármazottja, saját repul() megvalósítással.
 */
public class Repulo extends RepuloDolgok {

    /** Utazómagasság méterben. */
    private final int utazomagassagM;

    /**
     * @param nev            a repülőgép neve
     * @param utazomagassagM az utazómagasság méterben
     */
    public Repulo(String nev, int utazomagassagM) {
        super(nev);
        this.utazomagassagM = utazomagassagM;
    }

    /** A repülőgép hajtóművel repül. */
    @Override
    public void repul() {
        System.out.println(nev + " hajtóművekkel repül " + utazomagassagM + " m magasan.");
    }
}
