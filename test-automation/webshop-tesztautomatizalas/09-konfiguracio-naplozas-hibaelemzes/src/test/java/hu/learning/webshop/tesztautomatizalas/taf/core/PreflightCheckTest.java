package hu.learning.webshop.tesztautomatizalas.taf.core;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

/**
 * A preflight ellenőrzés tesztje: a „SUT”-ot itt WireMock játssza, így a hibás környezeti
 * állapotok (DOWN, elérhetetlen, üres katalógus) is előállíthatók.
 */
class PreflightCheckTest {

    /** A szimulált SUT. */
    @RegisterExtension
    static WireMockExtension sut = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().http2PlainDisabled(true)).build();

    /** Rövid időkorlát a gyors tesztekért. */
    private static final Duration TIMEOUT = Duration.ofMillis(500);

    /** Egészséges környezet: minden ellenőrzés sikeres. */
    @Test
    void healthyEnvironment() {
        sut.stubFor(get("/actuator/health").willReturn(okJson("{\"status\":\"UP\"}")));
        sut.stubFor(get("/products").willReturn(okJson("[{\"id\":\"P001\"}]")));

        PreflightCheck.Report report = PreflightCheck.run(sut.baseUrl(), TIMEOUT);

        assertThat(report.ok()).isTrue();
        assertThat(report.passed()).containsExactly("health=UP", "katalógus=1 termék");
    }

    /** A SUT fut, de DOWN állapotot jelez (pl. nem elérhető az adatbázisa). */
    @Test
    void sutReportsDown() {
        sut.stubFor(get("/actuator/health").willReturn(aResponse().withStatus(503)
                .withHeader("Content-Type", "application/json").withBody("{\"status\":\"DOWN\"}")));
        sut.stubFor(get("/products").willReturn(okJson("[{\"id\":\"P001\"}]")));

        assertThat(PreflightCheck.run(sut.baseUrl(), TIMEOUT).failures()).containsExactly("health státusz: DOWN");
    }

    /** Hiányzó tesztadat: a katalógus üres. */
    @Test
    void emptyCatalog() {
        sut.stubFor(get("/actuator/health").willReturn(okJson("{\"status\":\"UP\"}")));
        sut.stubFor(get("/products").willReturn(okJson("[]")));

        assertThat(PreflightCheck.run(sut.baseUrl(), TIMEOUT).failures())
                .singleElement().asString().contains("katalógus üres");
    }

    /** Elérhetetlen SUT (nincs, ami figyeljen a porton). */
    @Test
    void unreachableSut() {
        PreflightCheck.Report report = PreflightCheck.run("http://localhost:1", TIMEOUT);
        assertThat(report.ok()).isFalse();
        assertThat(report.failures()).singleElement().asString().startsWith("health nem elérhető");
        assertThat(report.summary()).startsWith("PREFLIGHT SIKERTELEN");
    }
}
