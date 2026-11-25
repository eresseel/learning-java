package hu.learning.webshop.tesztautomatizalas.payment;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.net.http.HttpClient;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

/**
 * HTTP kliens a külső „PayPal” fizetési API-hoz.
 *
 * <p>Ez a SUT egyik <b>külső függősége</b> (I. feladat: „Külső API (PayPal)”). Tesztben nem a
 * valódi szolgáltatót hívjuk, hanem WireMock-kal emuláljuk (II. fejezet: „harmadik féltől
 * származó alkalmazások emulálása”) – így a hibaágak (4xx, 5xx, timeout) is determinisztikusan
 * kipróbálhatók.</p>
 *
 * <p>Hibaleképezés:</p>
 * <ul>
 *   <li>4xx → {@link PaymentDeclinedException} (a fizetés elutasítva, újrapróbálni nincs értelme);</li>
 *   <li>5xx, kapcsolódási hiba, váratlan állapot → {@link PaymentProviderException};</li>
 *   <li>időtúllépés → {@link PaymentTimeoutException}.</li>
 * </ul>
 */
public class PaymentClient {

    /** A rendelés-létrehozás útvonala a szolgáltatónál. */
    static final String ORDERS_PATH = "/v2/checkout/orders";

    /** A Spring szinkron HTTP kliense. */
    private final RestClient restClient;

    /**
     * @param restClient előre beállított (alap URL, timeout) RestClient
     */
    public PaymentClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Gyártó metódus: JDK HttpClient alapú RestClient-et épít a megadott címmel és időkorláttal.
     *
     * @param baseUrl a szolgáltató alap URL-je
     * @param timeout kapcsolódási és válaszidő-korlát
     * @return a kész kliens
     */
    public static PaymentClient create(String baseUrl, Duration timeout) {
        // 1. JDK HttpClient: kapcsolódási időkorlát; HTTP/1.1 (a WireMock-kal így a legegyszerűbb)
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(timeout)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        // 2. A Spring adapter, ami a válaszra várás időkorlátját (read timeout) is beállítja
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(timeout);
        // 3. RestClient az alap URL-lel
        return new PaymentClient(RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build());
    }

    /**
     * Terhelést kér a szolgáltatótól.
     *
     * @param orderId  a webshop rendelésazonosítója (egyben idempotencia-kulcs)
     * @param amount   a terhelendő összeg
     * @param currency pénznem (pl. {@code HUF})
     * @return a szolgáltató válasza ({@code status = COMPLETED})
     * @throws PaymentDeclinedException  4xx válasznál
     * @throws PaymentProviderException  5xx, váratlan válasz vagy kapcsolódási hiba esetén
     * @throws PaymentTimeoutException   időtúllépésnél
     */
    public PaypalOrderResponse charge(String orderId, BigDecimal amount, String currency) {
        // 1. A kérés-törzs összeállítása (összeg 2 tizedesjeggyel, szövegként)
        PaypalOrderRequest body = new PaypalOrderRequest("CAPTURE", List.of(
                new PaypalOrderRequest.PurchaseUnit(orderId,
                        new PaypalOrderRequest.Amount(currency, amount.setScale(2).toPlainString()))));
        try {
            // 2. Hívás; a státuszkód-alapú hibákat onStatus kezelőkkel saját kivételekké alakítjuk
            PaypalOrderResponse response = restClient.post()
                    .uri(ORDERS_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    // A PayPal-Request-Id fejléc idempotenciát biztosít: ismételt kérés nem terhel kétszer
                    .header("PayPal-Request-Id", orderId)
                    .body(body)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        throw new PaymentDeclinedException(res.getStatusCode().value(),
                                new String(res.getBody().readAllBytes()));
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new PaymentProviderException(
                                "A fizetési szolgáltató hibát jelzett (HTTP " + res.getStatusCode().value() + ").", null);
                    })
                    .body(PaypalOrderResponse.class);
            // 3. Sikeres HTTP válasz, de az üzleti állapotot is ellenőrizni kell
            if (response == null || !"COMPLETED".equals(response.status())) {
                throw new PaymentProviderException("Váratlan fizetési állapot: "
                        + (response == null ? "üres válasz" : response.status()), null);
            }
            return response;
        } catch (ResourceAccessException e) {
            // 4. I/O hiba: időtúllépés vagy kapcsolódási hiba – a kiváltó ok alapján döntünk
            if (isTimeout(e)) {
                throw new PaymentTimeoutException(e);
            }
            throw new PaymentProviderException("A fizetési szolgáltató nem érhető el: " + e.getMessage(), e);
        }
    }

    /**
     * Végigjárja a kiváltó okok láncát, és időtúllépést keres.
     *
     * @param e a Spring I/O kivétele
     * @return {@code true}, ha a hiba oka időtúllépés
     */
    private static boolean isTimeout(Throwable e) {
        for (Throwable t = e; t != null; t = t.getCause()) {
            if (t instanceof HttpTimeoutException || t instanceof SocketTimeoutException) {
                return true;
            }
        }
        return false;
    }
}
