/**
 * A riport HTML-be és XML-be is menthető, ezért mindkét interfészt implementálja.
 */
public class Riport implements HtmlMentheto, XmlMentheto {

    private final String cim;

    /** @param cim a riport címe */
    public Riport(String cim) {
        this.cim = cim;
    }

    /** @return HTML alak */
    @Override
    public String htmlbe() {
        return "<h1>" + cim + "</h1>";
    }

    /** @return XML alak */
    @Override
    public String xmlbe() {
        return "<riport cim=\"" + cim + "\"/>";
    }
}
