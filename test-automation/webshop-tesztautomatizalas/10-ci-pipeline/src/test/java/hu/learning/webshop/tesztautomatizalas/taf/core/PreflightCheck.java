package hu.learning.webshop.tesztautomatizalas.taf.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Előzetes környezet-ellenőrzés (preflight) – a X. feladat A része automatizálva.
 *
 * <p>Futtatás <em>előtt</em> ellenőrzi, hogy a tesztkörnyezet egyáltalán alkalmas-e a tesztelésre.
 * Ha nem, a tesztek nem futnak le „pirosan” (ami hamis SUT-hibának látszana), hanem
 * <b>Inconclusive</b> (nem eldönthető) eredményt kapnak – VI. fejezet: „Hamis hibák elkerülése:
 * infrastruktúra-problémák kizárása”.</p>
 *
 * <p>Ellenőrzések:</p>
 * <ol>
 *   <li>a SUT elérhető, és a health végpont {@code {"status":"UP"}}-ot ad;</li>
 *   <li>a tesztadat-előfeltétel teljesül: a termékkatalógus nem üres.</li>
 * </ol>
 * (A konfiguráció formai helyességét már a {@link Config#load(int)} ellenőrzi.)
 *
 * <p>Szándékosan a JDK saját {@link HttpClient}-jét használja, nem a TAF {@link ApiClient}-jét:
 * így a környezet ellenőrzése a TAF esetleges hibáitól független.</p>
 */
public final class PreflightCheck {

    /** Eredmény-gyorsítótár URL-enként: egy futásban egy környezetet csak egyszer ellenőrzünk. */
    private static final Map<String, Report> CACHE = new ConcurrentHashMap<>();
    /** JSON olvasó. */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /** Segédosztály. */
    private PreflightCheck() {
    }

    /**
     * Az ellenőrzés eredménye.
     *
     * @param baseUrl  az ellenőrzött környezet
     * @param passed   a sikeres ellenőrzések leírása
     * @param failures a sikertelen ellenőrzések leírása
     */
    public record Report(String baseUrl, List<String> passed, List<String> failures) {

        /** @return minden ellenőrzés sikeres-e */
        public boolean ok() {
            return failures.isEmpty();
        }

        /** @return egysoros, naplózható összefoglaló */
        public String summary() {
            return "PREFLIGHT " + (ok() ? "OK" : "SIKERTELEN") + " [" + baseUrl + "] ok=" + passed
                    + (ok() ? "" : " hibák=" + failures);
        }
    }

    /**
     * Ellenőrzés gyorsítótárazással (a teljes futás alatt környezetenként egyszer).
     *
     * @param baseUrl a SUT címe
     * @param timeout kérésenkénti időkorlát
     * @return az eredmény
     */
    public static Report runOnce(String baseUrl, Duration timeout) {
        return CACHE.computeIfAbsent(baseUrl, url -> run(url, timeout));
    }

    /**
     * Ellenőrzés gyorsítótár nélkül.
     *
     * @param baseUrl a SUT címe
     * @param timeout kérésenkénti időkorlát
     * @return az eredmény
     */
    public static Report run(String baseUrl, Duration timeout) {
        List<String> passed = new ArrayList<>();
        List<String> failures = new ArrayList<>();
        HttpClient http = HttpClient.newBuilder().connectTimeout(timeout)
                .version(HttpClient.Version.HTTP_1_1).build();

        // 1. SUT elérhetőség és állapot
        try {
            JsonNode health = getJson(http, baseUrl + "/actuator/health", timeout);
            if ("UP".equals(health.path("status").asText())) {
                passed.add("health=UP");
            } else {
                failures.add("health státusz: " + health.path("status").asText("ismeretlen"));
            }
        } catch (Exception e) {
            // Kapcsolódási hiba, időtúllépés, nem JSON válasz – a környezet nem használható
            failures.add("health nem elérhető: " + e.getClass().getSimpleName()
                    + (e.getMessage() == null ? "" : ": " + e.getMessage()));
            return new Report(baseUrl, passed, failures); // további ellenőrzésnek nincs értelme
        }

        // 2. Tesztadat-előfeltétel: van termék a katalógusban
        try {
            JsonNode products = getJson(http, baseUrl + "/products", timeout);
            if (products.isArray() && !products.isEmpty()) {
                passed.add("katalógus=" + products.size() + " termék");
            } else {
                failures.add("a termékkatalógus üres – a tesztadat-előfeltétel nem teljesül");
            }
        } catch (Exception e) {
            failures.add("termékek nem lekérdezhetők: " + e.getMessage());
        }
        return new Report(baseUrl, passed, failures);
    }

    /**
     * GET kérés, és a válasz JSON-ként (a 503-as health válasz is JSON, ezért a törzset nézzük).
     */
    private static JsonNode getJson(HttpClient http, String url, Duration timeout) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).timeout(timeout).GET().build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        return MAPPER.readTree(response.body());
    }
}
