package hu.learning.webshop.tesztautomatizalas.tests;

import static org.assertj.core.api.Assertions.assertThat;

import hu.learning.webshop.tesztautomatizalas.taf.core.ApiClient;
import hu.learning.webshop.tesztautomatizalas.taf.core.StructuredLoggingFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A naplózási és nyomonkövetési képességek tesztje (VI. fejezet: korrelációs ID, TAS és SUT
 * naplók összekapcsolása). Technikai teszt, ezért a core réteget közvetlenül használja.
 */
@DisplayName("Megfigyelhetőség – korrelációs ID, health")
class ObservabilityTest extends AbstractShopTest {

    /** A TAS által küldött korrelációs ID-t a SUT visszaküldi (és a saját naplójába írja). */
    @Test
    void correlationIdIsEchoedBySut() {
        Response response = ApiClient.getInstance().request()
                .header(StructuredLoggingFilter.CORRELATION_HEADER, "teszt-x-001")
                .get("/products");
        assertThat(response.header(StructuredLoggingFilter.CORRELATION_HEADER)).isEqualTo("teszt-x-001");
        assertThat(StructuredLoggingFilter.lastExchange()).contains("correlationId=teszt-x-001", "status=200");
    }

    /** Ha a teszt nem ad meg ID-t, a TAS szűrője generál egyet – minden hívás visszakereshető. */
    @Test
    void filterGeneratesCorrelationId() {
        Response response = ApiClient.getInstance().get("/cart");
        assertThat(response.header(StructuredLoggingFilter.CORRELATION_HEADER)).startsWith("tas-");
    }

    /** A health végpont elérhető (ezt használja a preflight). */
    @Test
    void healthIsUp() {
        assertThat(ApiClient.getInstance().get("/actuator/health").jsonPath().getString("status")).isEqualTo("UP");
    }
}
