/**
 * A számla csak XML-be kell menthető legyen, így csak az XmlMentheto-t implementálja –
 * nem kell fölösleges htmlbe() metódust írnia.
 */
public class Szamla implements XmlMentheto {

    private final String sorszam;
    private final int osszeg;

    /**
     * @param sorszam a számla sorszáma
     * @param osszeg  a végösszeg forintban
     */
    public Szamla(String sorszam, int osszeg) {
        this.sorszam = sorszam;
        this.osszeg = osszeg;
    }

    /** @return XML alak */
    @Override
    public String xmlbe() {
        return "<szamla sorszam=\"" + sorszam + "\" osszeg=\"" + osszeg + "\"/>";
    }
}
