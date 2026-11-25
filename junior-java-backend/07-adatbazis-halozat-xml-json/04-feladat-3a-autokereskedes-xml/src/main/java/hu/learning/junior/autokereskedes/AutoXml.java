package hu.learning.junior.autokereskedes;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XML olvasás és írás a JDK beépített DOM API-jával (külső könyvtár nélkül).
 *
 * <p>A DOM (Document Object Model) a teljes XML-t egy fába olvassa a
 * memóriába: a fa csomópontjai az elemek ({@link Element}), attribútumok és
 * szövegek. Kis és közepes fájlokhoz kényelmes; nagyon nagy fájlokhoz a
 * folyamatos (SAX/StAX) feldolgozás való.</p>
 */
public final class AutoXml {

    /** Csak statikus metódusok vannak. */
    private AutoXml() {
    }

    /**
     * Beolvassa a kereskedés XML fájlját.
     *
     * @param fajl az XML fájl
     * @return a kereskedés a nevével és az autók listájával
     * @throws IOException ha a fájl nem olvasható vagy nem érvényes XML
     */
    public static Autokereskedes beolvas(Path fajl) throws IOException {
        try {
            // 1. Biztonságos parser: DTD tiltása (XXE támadás ellen), ez jó szokás
            //    minden külső forrásból jövő XML esetén.
            DocumentBuilderFactory gyar = DocumentBuilderFactory.newInstance();
            gyar.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            gyar.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder epito = gyar.newDocumentBuilder();
            // Az alapértelmezett hibakezelő a konzolra is kiírná a hibát ("[Fatal Error] ...");
            // null esetén csak a kivétel jön, amit lent mi kezelünk.
            epito.setErrorHandler(null);
            // 2. A fájl teljes beolvasása DOM fába.
            Document dok = epito.parse(fajl.toFile());
            Element gyoker = dok.getDocumentElement();
            // 3. Az összes <auto> elem bejárása.
            List<Auto> autok = new ArrayList<>();
            NodeList elemek = gyoker.getElementsByTagName("auto");
            for (int i = 0; i < elemek.getLength(); i++) {
                Element e = (Element) elemek.item(i);
                // 4. Attribútum és gyerekelemek szövegének kiolvasása, típuskonverzió.
                autok.add(new Auto(
                        Integer.parseInt(e.getAttribute("id")),
                        szoveg(e, "marka"),
                        szoveg(e, "tipus"),
                        Integer.parseInt(szoveg(e, "evjarat")),
                        Integer.parseInt(szoveg(e, "ar")),
                        szoveg(e, "szin")));
            }
            return new Autokereskedes(gyoker.getAttribute("nev"), autok);
        } catch (ParserConfigurationException | SAXException | NumberFormatException e) {
            // A különféle hibákat egyetlen, a hívó számára értelmes kivételbe csomagoljuk.
            throw new IOException("Hibás XML: " + fajl + " – " + e.getMessage(), e);
        }
    }

    /**
     * Kiírja a kereskedést új XML fájlba, tagolt (behúzott) formában.
     *
     * @param kereskedes a kiírandó adatok
     * @param fajl       a cél fájl (felülíródik)
     * @throws IOException ha az írás nem sikerül
     */
    public static void kiir(Autokereskedes kereskedes, Path fajl) throws IOException {
        try {
            // 1. Üres DOM dokumentum létrehozása.
            Document dok = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element gyoker = dok.createElement("autokereskedes");
            gyoker.setAttribute("nev", kereskedes.nev());
            dok.appendChild(gyoker);
            // 2. Autónként egy <auto id="..."> elem a gyerekelemeivel.
            for (Auto a : kereskedes.autok()) {
                Element auto = dok.createElement("auto");
                auto.setAttribute("id", String.valueOf(a.id()));
                gyerek(dok, auto, "marka", a.marka());
                gyerek(dok, auto, "tipus", a.tipus());
                gyerek(dok, auto, "evjarat", String.valueOf(a.evjarat()));
                gyerek(dok, auto, "ar", String.valueOf(a.ar()));
                gyerek(dok, auto, "szin", a.szin());
                gyoker.appendChild(auto);
            }
            // 3. A DOM fa fájlba írása Transformerrel (behúzással, UTF-8 kódolással).
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            // A JDK Transformer az XML deklaráció után nem tesz sortörést, ezért a
            // deklarációt kihagyatjuk vele, és mi írjuk ki külön sorba.
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            try (Writer iro = Files.newBufferedWriter(fajl, StandardCharsets.UTF_8)) {
                iro.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator());
                t.transform(new DOMSource(dok), new StreamResult(iro));
            }
        } catch (ParserConfigurationException | TransformerException e) {
            throw new IOException("Az XML írása nem sikerült: " + e.getMessage(), e);
        }
    }

    /**
     * Egy gyerekelem szövegtartalma.
     *
     * @param szulo  a szülő elem
     * @param nev    a gyerekelem neve
     * @return a szöveg, levágva
     * @throws IllegalArgumentException ha nincs ilyen gyerekelem
     */
    private static String szoveg(Element szulo, String nev) {
        NodeList lista = szulo.getElementsByTagName(nev);
        if (lista.getLength() == 0) {
            throw new IllegalArgumentException("Hiányzó <" + nev + "> elem");
        }
        return lista.item(0).getTextContent().strip();
    }

    /**
     * Létrehoz egy szöveges gyerekelemet és hozzáfűzi a szülőhöz.
     *
     * @param dok    a dokumentum (ez a "gyár" az új csomópontokhoz)
     * @param szulo  a szülő elem
     * @param nev    az új elem neve
     * @param ertek  a szövegtartalom
     */
    private static void gyerek(Document dok, Element szulo, String nev, String ertek) {
        Element e = dok.createElement(nev);
        e.setTextContent(ertek);
        szulo.appendChild(e);
    }
}
