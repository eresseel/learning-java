/**
 * I – interfész elkülönítés: csak az XML mentés képessége.
 * Egy közös "HtmlXmlMentheto" interfész olyan osztályra is ráerőltetné a HTML-t,
 * amelynek arra nincs szüksége – ezért két külön interfész van.
 */
public interface XmlMentheto {

    /** @return az objektum XML alakja */
    String xmlbe();
}
