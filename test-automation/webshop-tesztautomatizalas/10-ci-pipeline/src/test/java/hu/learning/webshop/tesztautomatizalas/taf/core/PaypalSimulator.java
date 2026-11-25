package hu.learning.webshop.tesztautomatizalas.taf.core;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;

/**
 * A külső „PayPal” szolgáltatás szimulátora – a WireMock részleteit elrejtő adapter.
 *
 * <p>A tesztszkriptek így nem WireMock-nyelven („stubFor(post(...).willReturn(...))”), hanem
 * szándék szerint fogalmaznak: {@code paypal.willDecline()}. Ha holnap WireMock helyett másik
 * mock eszközt választunk, csak ezt az osztályt kell átírni (TAF: tesztadaptáció képesség).</p>
 *
 * <p>Egyetlen, a teljes tesztfuttatás alatt élő szervert használunk ({@link #shared()}): a Spring
 * tesztkontextus a címét egyszer kapja meg és gyorsítótárazza – ha tesztosztályonként új porton
 * indulna a mock, a gyorsítótárazott kontextus rossz címre küldené a kéréseket.</p>
 */
public final class PaypalSimulator {

    /** A szimulált végpont útvonala. */
    private static final String ORDERS = "/v2/checkout/orders";

    /** A WireMock szerver. */
    private final WireMockServer server;

    /**
     * @param server a már elindított WireMock szerver
     */
    private PaypalSimulator(WireMockServer server) {
        this.server = server;
    }

    /** Lusta, szálbiztos „holder”: az első {@link #shared()} hívás indítja a szervert. */
    private static final class Holder {
        private static final PaypalSimulator SHARED = start();
    }

    /**
     * @return a teljes futás alatt közös szimulátor
     */
    public static PaypalSimulator shared() {
        return Holder.SHARED;
    }

    /**
     * Elindít egy WireMock szervert véletlen porton, és a JVM leállásakor leállítja.
     *
     * @return az elindított szimulátor
     */
    private static PaypalSimulator start() {
        // A JDK HttpClient ne próbáljon h2c-re váltani (WireMock 3 / Jetty 12)
        WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort().http2PlainDisabled(true));
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        return new PaypalSimulator(server);
    }

    /**
     * @return a szimulátor alap URL-je (a SUT {@code paypal.base-url} beállításához)
     */
    public String baseUrl() {
        return server.baseUrl();
    }

    /** Törli a stubokat és a rögzített kéréseket – minden teszt előtt hívandó (tesztizoláció). */
    public void reset() {
        server.resetAll();
    }

    /**
     * A következő fizetés sikeres lesz.
     *
     * @param paymentId a visszaadott fizetésazonosító
     */
    public void willComplete(String paymentId) {
        server.stubFor(post(ORDERS).willReturn(okJson("{\"id\":\"" + paymentId + "\",\"status\":\"COMPLETED\"}")));
    }

    /** A következő fizetést a szolgáltató elutasítja (422). */
    public void willDecline() {
        server.stubFor(post(ORDERS).willReturn(aResponse().withStatus(422)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"name\":\"UNPROCESSABLE_ENTITY\",\"details\":[{\"issue\":\"INSTRUMENT_DECLINED\"}]}")));
    }

    /**
     * A szolgáltató szerverhibát ad.
     *
     * @param status 5xx státuszkód
     */
    public void willFail(int status) {
        server.stubFor(post(ORDERS).willReturn(aResponse().withStatus(status)));
    }

    /**
     * A szolgáltató késve, de sikeresen válaszol.
     *
     * @param delayMs késleltetés ezredmásodpercben
     */
    public void willRespondSlowly(int delayMs) {
        server.stubFor(post(ORDERS).willReturn(
                okJson("{\"id\":\"PAY-late\",\"status\":\"COMPLETED\"}").withFixedDelay(delayMs)));
    }

    /**
     * Ellenőrzi, hogy pontosan egy terhelés érkezett a megadott összeggel.
     *
     * @param amount az elvárt összeg szövegként, 2 tizedessel (pl. {@code "15980.00"})
     */
    public void verifyChargedOnce(String amount) {
        server.verify(1, postRequestedFor(urlEqualTo(ORDERS))
                .withRequestBody(matchingJsonPath("$.purchase_units[0].amount.value", equalTo(amount))));
    }

    /** Ellenőrzi, hogy a szolgáltatót egyáltalán nem hívták. */
    public void verifyNotCalled() {
        server.verify(0, anyRequestedFor(anyUrl()));
    }
}
