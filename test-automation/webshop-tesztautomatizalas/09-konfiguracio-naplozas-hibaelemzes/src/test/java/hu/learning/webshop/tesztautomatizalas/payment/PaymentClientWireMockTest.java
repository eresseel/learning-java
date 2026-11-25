package hu.learning.webshop.tesztautomatizalas.payment;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.math.BigDecimal;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

/**
 * A {@link PaymentClient} komponensintegrációs tesztje WireMock-kal – <b>Spring nélkül</b>.
 *
 * <p>A WireMock egy valódi HTTP szerver a teszt JVM-jében, amelynek „mi írjuk elő” a válaszait
 * (stub), és utólag ellenőrizhetjük, milyen kéréseket kapott (verify). Így a valódi PayPal nélkül,
 * determinisztikusan tesztelhetők a ritka hibaágak is.</p>
 *
 * <p>Fontos: a JDK {@code HttpClient} titkosítatlan kapcsolaton is megpróbálhat HTTP/2-re váltani
 * („h2c upgrade”), ami a WireMock Jetty 12 szerverével gondot okozhat – ezért
 * {@code http2PlainDisabled(true)}.</p>
 */
@DisplayName("PaymentClient – PayPal szimuláció WireMock-kal")
class PaymentClientWireMockTest {

    /** WireMock szerver véletlen porton; a JUnit extension indítja és állítja le, tesztenként törli a stubokat. */
    @RegisterExtension
    static WireMockExtension paypal = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().http2PlainDisabled(true))
            .build();

    /** A tesztelt kliens. */
    private PaymentClient client;

    /** A kliens a WireMock címére mutat, rövid (300 ms) időkorláttal, hogy a timeout-teszt gyors legyen. */
    @BeforeEach
    void setUp() {
        client = PaymentClient.create(paypal.baseUrl(), Duration.ofMillis(300));
    }

    /** Siker: a válasz feldolgozása ÉS a kimenő kérés tartalmának ellenőrzése. */
    @Test
    void successfulCharge() {
        // Arrange – stub: a szolgáltató COMPLETED állapotot ad, egy általunk nem ismert mezővel együtt
        paypal.stubFor(post(PaymentClient.ORDERS_PATH)
                .willReturn(okJson("{\"id\":\"PAY-123\",\"status\":\"COMPLETED\",\"links\":[]}")));

        // Act
        PaypalOrderResponse response = client.charge("ORD-1", new BigDecimal("15980"), "HUF");

        // Assert – válasz
        assertThat(response.id()).isEqualTo("PAY-123");
        // Assert – a kérés: fejléc (idempotencia-kulcs) és törzs (snake_case, 2 tizedes, szövegként)
        paypal.verify(1, postRequestedFor(urlEqualTo(PaymentClient.ORDERS_PATH))
                .withHeader("PayPal-Request-Id", equalTo("ORD-1"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(equalToJson("""
                        {"intent":"CAPTURE",
                         "purchase_units":[{"reference_id":"ORD-1",
                                            "amount":{"currency_code":"HUF","value":"15980.00"}}]}
                        """)));
    }

    /** 4xx (pl. 422 elutasított kártya) → PaymentDeclinedException, a szolgáltató indoklásával. */
    @Test
    void declinedIs4xx() {
        paypal.stubFor(post(PaymentClient.ORDERS_PATH).willReturn(aResponse().withStatus(422)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"name\":\"UNPROCESSABLE_ENTITY\",\"details\":[{\"issue\":\"INSTRUMENT_DECLINED\"}]}")));

        assertThatThrownBy(() -> client.charge("ORD-2", BigDecimal.TEN, "HUF"))
                .isInstanceOf(PaymentDeclinedException.class)
                .hasMessageContaining("HTTP 422")
                .hasMessageContaining("INSTRUMENT_DECLINED");
    }

    /** 5xx → PaymentProviderException. */
    @Test
    void serverErrorIs5xx() {
        paypal.stubFor(post(PaymentClient.ORDERS_PATH).willReturn(aResponse().withStatus(503)));

        assertThatThrownBy(() -> client.charge("ORD-3", BigDecimal.TEN, "HUF"))
                .isInstanceOf(PaymentProviderException.class)
                .hasMessageContaining("HTTP 503");
    }

    /** Lassú válasz (1 s késleltetés > 300 ms korlát) → PaymentTimeoutException. */
    @Test
    void slowResponseIsTimeout() {
        paypal.stubFor(post(PaymentClient.ORDERS_PATH)
                .willReturn(okJson("{\"id\":\"PAY-late\",\"status\":\"COMPLETED\"}").withFixedDelay(1000)));

        assertThatThrownBy(() -> client.charge("ORD-4", BigDecimal.TEN, "HUF"))
                .isInstanceOf(PaymentTimeoutException.class);
    }

    /** Hálózati hiba (a szerver bontja a kapcsolatot) → PaymentProviderException. */
    @Test
    void connectionResetIsProviderError() {
        paypal.stubFor(post(PaymentClient.ORDERS_PATH)
                .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

        assertThatThrownBy(() -> client.charge("ORD-5", BigDecimal.TEN, "HUF"))
                .isInstanceOf(PaymentProviderException.class)
                .hasMessageContaining("nem érhető el");
    }

    /** 200 OK, de üzletileg nem sikeres állapot → szintén hiba (nem hiszünk vakon a 200-nak). */
    @Test
    void unexpectedStatusIsProviderError() {
        paypal.stubFor(post(PaymentClient.ORDERS_PATH)
                .willReturn(okJson("{\"id\":\"PAY-6\",\"status\":\"PENDING\"}")));

        assertThatThrownBy(() -> client.charge("ORD-6", BigDecimal.TEN, "HUF"))
                .isInstanceOf(PaymentProviderException.class)
                .hasMessage("Váratlan fizetési állapot: PENDING");
    }
}
