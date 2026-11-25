package hu.learning.webshop.tesztautomatizalas.taf.core;

import static io.restassured.RestAssured.given;
import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.config.LogConfig.logConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Objects;

/**
 * Az egyetlen HTTP belépési pont a SUT felé – <b>Singleton</b> tervezési minta.
 *
 * <p><b>Miért Singleton?</b> A HTTP beállítás (alap URL, port, JSON-kezelés, naplózás) a teljes
 * tesztfuttatásra közös; ha minden teszt maga építené, ugyanaz a kód sokszor ismétlődne, és egy
 * változtatás (pl. új fejléc) sok helyen kellene. Egy példány = egy hely a változtatásra.</p>
 *
 * <p><b>Hátránya</b> (ezt is tudni kell!): globális, változtatható állapot. Párhuzamos futtatásnál
 * vagy több SUT-környezet egyidejű tesztelésénél ezért óvatosan kell bánni vele – nálunk a
 * konfiguráció csak egyszer, a tesztosztály előkészítésekor változik.</p>
 *
 * <p>Megvalósítás: „initialization-on-demand holder” idióma – lusta és szálbiztos, {@code synchronized} nélkül.</p>
 */
public final class ApiClient {

    /** Az aktuális konfiguráció ({@code volatile}: a szálak mindig a legfrissebb értéket látják). */
    private volatile Config config;

    /** Privát konstruktor: kívülről nem példányosítható (a Singleton lényege). */
    private ApiClient() {
    }

    /** A JVM csak az első {@link #getInstance()} hívásnál tölti be ezt az osztályt → lusta inicializálás. */
    private static final class Holder {
        private static final ApiClient INSTANCE = new ApiClient();
    }

    /**
     * @return az egyetlen példány
     */
    public static ApiClient getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Beállítja, melyik SUT-ot hívjuk.
     *
     * @param config a környezet konfigurációja
     * @return ez a példány (láncolható)
     */
    public ApiClient configure(Config config) {
        this.config = Objects.requireNonNull(config);
        return this;
    }

    /**
     * @return az aktuális konfiguráció
     * @throws IllegalStateException ha még nem konfigurálták (TAS-hiba, nem SUT-hiba)
     */
    public Config config() {
        Config c = config;
        if (c == null) {
            throw new IllegalStateException("Az ApiClient nincs konfigurálva – hívd meg a configure(...)-t!");
        }
        return c;
    }

    /**
     * Új kérés a közös beállításokkal. Minden hívás <em>új</em> specifikációt ad, így a tesztek
     * nem „szennyezik” egymás kéréseit (pl. egy query paraméter nem marad bent).
     *
     * @return előkészített REST Assured kérés
     */
    public RequestSpecification request() {
        Config c = config();
        int timeoutMs = (int) c.requestTimeout().toMillis();
        return given().spec(new RequestSpecBuilder()
                .setBaseUri(c.baseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                // Strukturált naplózás + korrelációs ID minden kéréshez (09. lépés)
                .addFilter(new StructuredLoggingFilter(c.logBodies()))
                .setConfig(RestAssuredConfig.config()
                        .jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL))
                        .logConfig(logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL))
                        // Időkorlát (kapcsolódás + válaszra várás): lefagyott SUT ne akassza meg a futást
                        .httpClient(httpClientConfig()
                                .setParam("http.connection.timeout", timeoutMs)
                                .setParam("http.socket.timeout", timeoutMs)))
                .build());
    }

    /**
     * {@code GET} kérés.
     *
     * @param path       útvonal, {@code {}} helyőrzőkkel
     * @param pathParams a helyőrzők értékei
     * @return a válasz (még nem ellenőrzött)
     */
    public Response get(String path, Object... pathParams) {
        return request().get(path, pathParams);
    }

    /**
     * {@code POST} kérés JSON törzzsel.
     *
     * @param path útvonal
     * @param body törzs (POJO, Map vagy JSON szöveg); {@code null} esetén üres
     * @return a válasz
     */
    public Response post(String path, Object body) {
        RequestSpecification req = request();
        if (body != null) {
            req.body(body);
        }
        return req.post(path);
    }

    /**
     * {@code PATCH} kérés JSON törzzsel.
     *
     * @param path       útvonal
     * @param body       törzs
     * @param pathParams útvonal-paraméterek
     * @return a válasz
     */
    public Response patch(String path, Object body, Object... pathParams) {
        return request().body(body).patch(path, pathParams);
    }

    /**
     * {@code DELETE} kérés.
     *
     * @param path       útvonal
     * @param pathParams útvonal-paraméterek
     * @return a válasz
     */
    public Response delete(String path, Object... pathParams) {
        return request().delete(path, pathParams);
    }
}
