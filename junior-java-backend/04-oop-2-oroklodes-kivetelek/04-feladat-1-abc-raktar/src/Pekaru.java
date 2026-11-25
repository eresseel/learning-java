/**
 * Pékáru: a Termek leszármazottja, plusz adata, hogy teljes kiőrlésű-e.
 */
public class Pekaru extends Termek {

    private final boolean teljesKiorlesu;

    /**
     * @param megnevezes     a termék neve
     * @param cikkszam       a cikkszám
     * @param ar             az egységár forintban
     * @param darabszam      a darabszám
     * @param teljesKiorlesu teljes kiőrlésű lisztből készült-e
     * @throws IllegalArgumentException ha bármelyik közös adat érvénytelen
     */
    public Pekaru(String megnevezes, String cikkszam, int ar, int darabszam, boolean teljesKiorlesu) {
        super(megnevezes, cikkszam, ar, darabszam);
        this.teljesKiorlesu = teljesKiorlesu;
    }

    /**
     * Logikai lekérdező – a tiszta kód ajánlása szerint "is" előtaggal.
     *
     * @return true, ha teljes kiőrlésű
     */
    public boolean isTeljesKiorlesu() {
        return teljesKiorlesu;
    }

    /** @return "Pékáru" */
    @Override
    public String kategoria() {
        return "Pékáru";
    }

    /** @return a kiőrlés típusa szövegesen */
    @Override
    protected String extraInfo() {
        return teljesKiorlesu ? "teljes kiőrlésű" : "fehér liszt";
    }
}
