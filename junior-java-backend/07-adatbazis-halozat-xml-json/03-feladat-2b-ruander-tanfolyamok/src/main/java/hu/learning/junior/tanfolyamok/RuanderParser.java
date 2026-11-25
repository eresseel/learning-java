package hu.learning.junior.tanfolyamok;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A ruander.hu HTML oldalainak feldolgozása: kigyűjti a tanfolyamok nevét
 * és árát.
 *
 * <p><b>Hogyan?</b> A kurzuskártyákon a név egy
 * {@code <span class="course-name">} elemben van, az ár pedig ugyanabban a
 * {@code <li>} elemben utána következik, kétféle formában:</p>
 * <ul>
 *   <li>főoldal: {@code 990.000 <span class="currency">Ft</span>} – akciónál
 *       előtte az áthúzott eredeti ár is szerepel, ilyenkor az utolsó (aktuális) ár kell</li>
 *   <li>kategóriaoldal: {@code 93.701 Ft+áfa (119.000 Ft)} vagy {@code 690.000 Ft}
 *       – ilyenkor a zárójeles bruttó árat vesszük</li>
 * </ul>
 *
 * <p><b>Figyelem:</b> ez reguláris kifejezésekre épülő „képernyőkaparás”
 * (web scraping). Ha az oldal HTML szerkezete megváltozik, a feldolgozó
 * elromolhat – ezért a tesztek egy mentett mintaoldalon futnak. Komolyabb
 * feladathoz HTML-elemző könyvtár (pl. jsoup) vagy hivatalos API való.</p>
 */
public final class RuanderParser {

    /**
     * Egy kurzuskártya: a név ({@code course-name} span tartalma) és az utána
     * következő HTML a {@code </li>} lezárásig. (?s): a pont a sortörésre is
     * illeszkedik; {@code .*?}: „lusta” illesztés, a LEGKÖZELEBBI {@code </li>}-ig.
     */
    private static final Pattern KARTYA = Pattern.compile(
            "(?s)<span class=\"course-name[^\"]*\"[^>]*>([^<]*)</span>(.*?)</li>");

    /** Főoldali ár: szám, majd egy currency span „Ft” tartalommal. */
    private static final Pattern AR_CURRENCY = Pattern.compile(
            "([\\d.]+)\\s*<span class=\"currency\"[^>]*>\\s*Ft");

    /** Kategóriaoldali bruttó ár zárójelben: (119.000 Ft). */
    private static final Pattern AR_BRUTTO = Pattern.compile("\\(([\\d.]+) Ft\\)");

    /** Egyszerű ár: 690.000 Ft. */
    private static final Pattern AR_EGYSZERU = Pattern.compile("([\\d.]+)\\s*Ft");

    /** Csak statikus metódusok vannak. */
    private RuanderParser() {
    }

    /**
     * Kigyűjti a tanfolyamokat egy HTML oldalból. Az ár nélküli kártyákat
     * kihagyja, a többször szereplő (pl. több fülön is megjelenő) tanfolyamokat
     * csak egyszer veszi fel, az első előfordulás sorrendjében.
     *
     * @param html az oldal HTML forrása
     * @return a tanfolyamok listája
     */
    public static List<Tanfolyam> feldolgoz(String html) {
        // A LinkedHashMap megőrzi a beszúrási sorrendet, és a kulcs (név)
        // egyedisége miatt kiszűri az ismétlődéseket.
        Map<String, Tanfolyam> tanfolyamok = new LinkedHashMap<>();
        Matcher kartya = KARTYA.matcher(html);
        // 1. Végigmegyünk az összes kártyán (find() a következő találatra lép).
        while (kartya.find()) {
            String nev = htmlDekodol(kartya.group(1)).strip();
            String maradek = kartya.group(2);
            // 2. Ár keresése a kártya további részében.
            Integer ar = arKereses(maradek);
            // 3. Csak névvel és árral rendelkező, még nem látott tanfolyam kerül be.
            if (!nev.isEmpty() && ar != null) {
                tanfolyamok.putIfAbsent(nev, new Tanfolyam(nev, ar));
            }
        }
        return new ArrayList<>(tanfolyamok.values());
    }

    /**
     * Megkeresi az árat a kártya HTML-jében a három ismert formátum szerint.
     *
     * @param html a kártya neve utáni HTML részlet
     * @return az ár forintban, vagy {@code null}, ha nincs ár
     */
    static Integer arKereses(String html) {
        // 1. Főoldali formátum: az UTOLSÓ találat kell, mert akciónál előbb az
        //    áthúzott eredeti ár, utána az aktuális ár szerepel.
        Matcher currency = AR_CURRENCY.matcher(html);
        Integer utolso = null;
        while (currency.find()) {
            utolso = arSzam(currency.group(1));
        }
        if (utolso != null) {
            return utolso;
        }
        // 2. Kategóriaoldali formátumok; a sorrend számít: előbb a bruttó (zárójeles).
        for (Pattern minta : List.of(AR_BRUTTO, AR_EGYSZERU)) {
            Matcher m = minta.matcher(html);
            if (m.find()) {
                return arSzam(m.group(1));
            }
        }
        return null;
    }

    /**
     * Magyar ezres tagolású számot alakít egésszé: {@code "1.280.000"} → 1280000.
     *
     * @param szoveg a szám szövegként, pontokkal
     * @return az egész érték
     */
    static int arSzam(String szoveg) {
        return Integer.parseInt(szoveg.replace(".", ""));
    }

    /**
     * A leggyakoribb HTML-entitások visszaalakítása.
     *
     * @param szoveg HTML-szöveg
     * @return dekódolt szöveg
     */
    static String htmlDekodol(String szoveg) {
        return szoveg.replace("&quot;", "\"")
                .replace("&#039;", "'")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&nbsp;", " ")
                .replace("&amp;", "&");
    }
}
