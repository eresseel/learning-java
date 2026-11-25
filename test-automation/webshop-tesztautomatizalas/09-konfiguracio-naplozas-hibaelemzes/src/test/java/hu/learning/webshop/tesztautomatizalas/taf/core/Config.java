package hu.learning.webshop.tesztautomatizalas.taf.core;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

/**
 * A tesztkörnyezet konfigurációja (V. fejezet: „Tesztver konfiguráció – URL-ek, hitelesítő adatok”).
 *
 * <p><b>Forrássorrend</b> minden beállításnál (az első talált érték nyer):</p>
 * <ol>
 *   <li>JVM system property, pl. {@code -Dwebshop.baseUrl=https://staging.pelda.hu}</li>
 *   <li>környezeti változó, pl. {@code WEBSHOP_BASE_URL=https://staging.pelda.hu} (CI-ban ez a szokásos)</li>
 *   <li>alapértelmezés (lokálisan indított SUT)</li>
 * </ol>
 *
 * <p>Így ugyanaz a tesztkészlet kódváltoztatás nélkül futtatható a fejlesztői gépen, a CI build
 * környezetben és egy telepített tesztkörnyezet ellen (II. fejezet: végrehajtási környezetek).
 * A beállítások nincsenek beégetve (Clean Code: hardcode kerülése).</p>
 *
 * @param baseUrl        a SUT alap URL-je (séma + hoszt + port)
 * @param requestTimeout HTTP kérés időkorlátja – egy lefagyott SUT ne akassza meg a futást
 * @param logBodies      a sikeres kérések törzsét is naplózzuk-e (hibánál mindig naplózunk)
 * @param preflight      fusson-e az előzetes környezet-ellenőrzés
 * @param external       a SUT külső (már telepített) rendszer-e, nem a teszt JVM-je indította
 */
public record Config(String baseUrl, Duration requestTimeout, boolean logBodies, boolean preflight, boolean external) {

    /** Kulcsok: system property név → környezeti változó név. */
    static final String BASE_URL = "webshop.baseUrl";
    static final String TIMEOUT_MS = "webshop.timeoutMs";
    static final String LOG_BODIES = "webshop.logBodies";
    static final String PREFLIGHT = "webshop.preflight";

    /**
     * Betölti a konfigurációt a JVM system property-kből és a környezeti változókból.
     *
     * @param localPort a lokálisan indított SUT portja (ha nincs külső URL megadva); 0 = nincs lokális SUT
     * @return a konfiguráció
     */
    public static Config load(int localPort) {
        return load(localPort, System::getProperty, System.getenv()::get);
    }

    /**
     * Tesztelhető változat: a property- és környezetiváltozó-forrás kívülről adható (lásd {@code ConfigTest}).
     *
     * @param localPort  lokális port (0 = nincs)
     * @param properties system property olvasó
     * @param env        környezeti változó olvasó
     * @return a konfiguráció
     * @throws IllegalStateException ha nincs se külső URL, se lokális port, vagy az URL hibás (TAS-hiba)
     */
    static Config load(int localPort, Function<String, String> properties, Function<String, String> env) {
        // 1. Alap URL: külső, ha meg van adva; különben a lokális port
        String external = lookup(BASE_URL, properties, env);
        String baseUrl;
        if (external != null) {
            baseUrl = stripTrailingSlash(external);
        } else if (localPort > 0) {
            baseUrl = "http://localhost:" + localPort;
        } else {
            throw new IllegalStateException("Nincs megadva a SUT címe: állítsd be a -D" + BASE_URL
                    + " property-t vagy a " + envName(BASE_URL) + " környezeti változót.");
        }
        // 2. Formai ellenőrzés már betöltéskor – a hibás konfiguráció korán és érthetően derüljön ki
        URI uri = URI.create(baseUrl);
        if (uri.getScheme() == null || uri.getHost() == null) {
            throw new IllegalStateException("Érvénytelen " + BASE_URL + ": " + baseUrl);
        }
        // 3. A többi beállítás alapértelmezéssel
        Duration timeout = Duration.ofMillis(Long.parseLong(orDefault(lookup(TIMEOUT_MS, properties, env), "5000")));
        boolean logBodies = Boolean.parseBoolean(orDefault(lookup(LOG_BODIES, properties, env), "false"));
        boolean preflight = Boolean.parseBoolean(orDefault(lookup(PREFLIGHT, properties, env), "true"));
        return new Config(baseUrl, timeout, logBodies, preflight, external != null);
    }

    /**
     * @return a konfiguráció naplózható összefoglalója (titkot nem tartalmaz)
     */
    public Map<String, Object> describe() {
        return Map.of("baseUrl", baseUrl, "timeoutMs", requestTimeout.toMillis(),
                "logBodies", logBodies, "preflight", preflight, "external", external);
    }

    /** Előbb a system property, aztán a környezeti változó. */
    private static String lookup(String key, Function<String, String> properties, Function<String, String> env) {
        String value = properties.apply(key);
        if (value == null || value.isBlank()) {
            value = env.apply(envName(key));
        }
        return value == null || value.isBlank() ? null : value.trim();
    }

    /** {@code webshop.baseUrl} → {@code WEBSHOP_BASE_URL} (camelCase → SNAKE_CASE). */
    static String envName(String key) {
        return key.replaceAll("([a-z])([A-Z])", "$1_$2").replace('.', '_').toUpperCase();
    }

    /** Alapértelmezés, ha nincs érték. */
    private static String orDefault(String value, String def) {
        return value == null ? def : value;
    }

    /** A záró perjel elhagyása (a kérésútvonalak „/”-rel kezdődnek). */
    private static String stripTrailingSlash(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}
