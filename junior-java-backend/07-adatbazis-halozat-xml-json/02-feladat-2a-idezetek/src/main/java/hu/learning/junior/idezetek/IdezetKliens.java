package hu.learning.junior.idezetek;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/**
 * HTTP kliens az idézetforrásokhoz, a JDK beépített
 * {@link java.net.http.HttpClient}-jével (Java 11 óta, külső könyvtár nélkül).
 *
 * <ul>
 *   <li>Elsődleges forrás: Quotable API (kulcs nélküli, nyilvános tükör)</li>
 *   <li>Tartalék: a szerző Wikiquote oldala a MediaWiki API-n keresztül</li>
 * </ul>
 */
public class IdezetKliens {

    /** A Quotable API egy nyilvános, kulcs nélkül használható példánya. */
    static final String QUOTABLE_URL = "https://api.quotable.kurokeita.dev/api/quotes";

    /** Az angol Wikiquote MediaWiki API-ja. */
    static final String WIKIQUOTE_URL = "https://en.wikiquote.org/w/api.php";

    /**
     * A Wikimedia oldalak kérik, hogy a kliens beszédes User-Agent-et küldjön.
     * (Személyes adatot nem teszünk bele.)
     */
    static final String USER_AGENT = "learning-java-demo/1.0 (oktatasi peldaprogram; java.net.http)";

    /**
     * Egyetlen, újrahasznosított HttpClient példány: belül kapcsolat-készletet
     * tart fenn, ezért nem érdemes kérésenként újat létrehozni.
     */
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    /**
     * Idézetek a Quotable API-ból.
     *
     * @param szerzoAzonosito a szerző "slug" azonosítója, pl. {@code charles-dickens}
     * @param maximum         legfeljebb ennyi idézet
     * @return az idézetek
     * @throws IOException          hálózati hiba vagy nem 200-as válasz esetén
     * @throws InterruptedException ha a várakozó szálat megszakítják
     */
    public List<Idezet> quotable(String szerzoAzonosito, int maximum) throws IOException, InterruptedException {
        // A limit paraméter csak 10/25/50/100 lehet ennél az API-nál.
        String url = QUOTABLE_URL + "?limit=10&author=" + kodol(szerzoAzonosito);
        return QuotableParser.feldolgoz(get(url), maximum);
    }

    /**
     * Idézetek a szerző Wikiquote oldaláról.
     *
     * @param oldal   a Wikiquote oldal címe, pl. {@code Charles_Dickens}
     * @param szerzo  a szerző megjelenítendő neve
     * @param maximum legfeljebb ennyi idézet
     * @return az idézetek
     * @throws IOException          hálózati hiba vagy nem 200-as válasz esetén
     * @throws InterruptedException ha a várakozó szálat megszakítják
     */
    public List<Idezet> wikiquote(String oldal, String szerzo, int maximum)
            throws IOException, InterruptedException {
        String url = WIKIQUOTE_URL + "?action=parse&prop=wikitext&format=json&formatversion=2&page="
                + kodol(oldal);
        return WikiquoteParser.feldolgoz(get(url), szerzo, maximum);
    }

    /**
     * Egy GET kérés elküldése és a válasz törzsének visszaadása szövegként.
     *
     * @param url a lekérendő cím
     * @return a válasz törzse
     * @throws IOException          hálózati hiba vagy nem 200-as státusz esetén
     * @throws InterruptedException ha a szálat megszakítják
     */
    private String get(String url) throws IOException, InterruptedException {
        // 1. A kérés összeállítása (builder minta): cím, fejlécek, időkorlát.
        HttpRequest keres = HttpRequest.newBuilder(URI.create(url))
                .header("User-Agent", USER_AGENT)
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();
        // 2. Szinkron küldés; a BodyHandlers.ofString() a törzset String-gé alakítja.
        HttpResponse<String> valasz = http.send(keres, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        // 3. Státuszkód ellenőrzése: csak a 200 OK-t fogadjuk el.
        if (valasz.statusCode() != 200) {
            throw new IOException("HTTP " + valasz.statusCode() + " – " + url);
        }
        return valasz.body();
    }

    /**
     * URL-kódolás a lekérdezési paraméterekhez (szóköz, ékezet stb.).
     *
     * @param ertek a kódolandó érték
     * @return a kódolt érték
     */
    private static String kodol(String ertek) {
        return URLEncoder.encode(ertek, StandardCharsets.UTF_8);
    }
}
