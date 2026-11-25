package hu.learning.junior.idezetek;

import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * A Wikiquote (MediaWiki API, {@code action=parse&prop=wikitext}) válaszának
 * feldolgozása: a wiki-forrásszövegből kiválogatja a felső szintű
 * felsorolás-elemeket (a {@code "* "} kezdetű sorokat – ezek az idézetek),
 * és eltávolítja belőlük a wiki-jelölést.
 *
 * <p>A wikiszöveg nem szabályos adatformátum, ezért ez csak közelítő
 * tisztítás – oktatási célra elég, de valódi alkalmazásban a strukturált
 * adatforrás (API) a jobb választás.</p>
 */
public final class WikiquoteParser {

    /** Ennél hosszabb idézeteket kihagyunk, hogy a konzolon olvasható maradjon. */
    static final int MAX_HOSSZ = 250;

    /** Csak statikus metódusok vannak. */
    private WikiquoteParser() {
    }

    /**
     * Kinyeri az első néhány idézetet a JSON válaszból.
     *
     * @param json    a MediaWiki API válasza ({@code formatversion=2})
     * @param szerzo  a szerző neve (a wikiszöveg nem tartalmazza soronként)
     * @param maximum legfeljebb ennyi idézet
     * @return a megtisztított idézetek
     */
    public static List<Idezet> feldolgoz(String json, String szerzo, int maximum) {
        // 1. A wikiszöveg kiemelése: {"parse": {"wikitext": "..."}}
        String wikiszoveg = JsonParser.parseString(json).getAsJsonObject()
                .getAsJsonObject("parse").get("wikitext").getAsString();
        List<Idezet> idezetek = new ArrayList<>();
        // 2. Soronkénti feldolgozás: csak a "* " kezdetű (felső szintű) elemek
        //    kellenek; a "** " kezdetűek a forrásmegjelölések.
        for (String sor : wikiszoveg.split("\n")) {
            if (idezetek.size() >= maximum) {
                break;
            }
            if (!sor.startsWith("*") || sor.startsWith("**")) {
                continue;
            }
            String tiszta = tisztit(sor.substring(1));
            // 3. Túl rövid (üres) vagy túl hosszú sorok kihagyása.
            if (tiszta.length() >= 10 && tiszta.length() <= MAX_HOSSZ) {
                idezetek.add(new Idezet(szerzo, tiszta, "wikiquote"));
            }
        }
        return idezetek;
    }

    /**
     * Eltávolítja a leggyakoribb wiki-jelöléseket egy sorból.
     *
     * @param sor wikiszöveg
     * @return sima szöveg
     */
    static String tisztit(String sor) {
        return sor
                // <ref>...</ref> lábjegyzetek és önzáró <ref .../> elemek
                .replaceAll("<ref[^>]*/>", "")
                .replaceAll("(?s)<ref[^>]*>.*?</ref>", "")
                // sablonok: {{...}}
                .replaceAll("\\{\\{[^}]*}}", "")
                // belső linkek: [[cél|felirat]] -> felirat, [[cél]] -> cél
                .replaceAll("\\[\\[[^]|]*\\|([^]]*)]]", "$1")
                .replaceAll("\\[\\[([^]]*)]]", "$1")
                // félkövér/dőlt jelölés: ''' és ''
                .replace("'''", "")
                .replace("''", "")
                // maradék HTML-elemek (pl. <br/>)
                .replaceAll("<[^>]+>", "")
                .strip();
    }
}
