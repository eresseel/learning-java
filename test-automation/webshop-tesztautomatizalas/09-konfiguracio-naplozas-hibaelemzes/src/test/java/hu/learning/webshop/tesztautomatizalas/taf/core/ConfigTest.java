package hu.learning.webshop.tesztautomatizalas.taf.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * A {@link Config} betöltési szabályainak tesztje – a TAF konfiguráció-kezelése is kód, tehát
 * tesztelni kell (VII. fejezet). A valódi system property-ket és környezeti változókat Map-ekkel
 * helyettesítjük, így a teszt determinisztikus.
 */
class ConfigTest {

    /** Semmi nincs beállítva: lokális SUT, alapértelmezések. */
    @Test
    void defaultsToLocalSut() {
        Config c = Config.load(8123, Map.<String, String>of()::get, Map.<String, String>of()::get);
        assertThat(c.baseUrl()).isEqualTo("http://localhost:8123");
        assertThat(c.external()).isFalse();
        assertThat(c.requestTimeout()).isEqualTo(Duration.ofSeconds(5));
        assertThat(c.preflight()).isTrue();
        assertThat(c.logBodies()).isFalse();
    }

    /** A környezeti változó felülírja az alapértelmezést, a system property a környezeti változót. */
    @Test
    void systemPropertyBeatsEnvironmentVariable() {
        Map<String, String> env = Map.of("WEBSHOP_BASE_URL", "https://staging.pelda.hu/", "WEBSHOP_TIMEOUT_MS", "800");
        Map<String, String> props = Map.of("webshop.baseUrl", "https://qa.pelda.hu");

        assertThat(Config.load(8123, Map.<String, String>of()::get, env::get).baseUrl())
                .isEqualTo("https://staging.pelda.hu");                 // env, záró perjel nélkül
        Config c = Config.load(8123, props::get, env::get);
        assertThat(c.baseUrl()).isEqualTo("https://qa.pelda.hu");        // a property nyer
        assertThat(c.external()).isTrue();
        assertThat(c.requestTimeout()).isEqualTo(Duration.ofMillis(800)); // ez env-ből jön
    }

    /** Hiányzó vagy hibás cím: érthető hiba már betöltéskor (TAS konfigurációs hiba). */
    @Test
    void missingOrInvalidUrlFailsFast() {
        assertThatThrownBy(() -> Config.load(0, Map.<String, String>of()::get, Map.<String, String>of()::get))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("WEBSHOP_BASE_URL");
        assertThatThrownBy(() -> Config.load(0, Map.of("webshop.baseUrl", "localhost-8080")::get,
                Map.<String, String>of()::get))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Érvénytelen");
    }

    /** A környezeti változó neve a property nevéből képződik. */
    @Test
    void envNameConvention() {
        assertThat(Config.envName("webshop.baseUrl")).isEqualTo("WEBSHOP_BASE_URL");
        assertThat(Config.envName("webshop.logBodies")).isEqualTo("WEBSHOP_LOG_BODIES");
    }
}
