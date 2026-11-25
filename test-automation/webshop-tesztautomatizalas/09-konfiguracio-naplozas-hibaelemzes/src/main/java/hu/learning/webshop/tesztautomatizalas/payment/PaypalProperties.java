package hu.learning.webshop.tesztautomatizalas.payment;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * A PayPal kapcsolat konfigurációja ({@code paypal.*} beállítások).
 *
 * <p>A cím és az időkorlát <b>nincs beégetve</b> a kódba (Clean Code: hardcode kerülése,
 * IV. fejezet) – így a tesztek a WireMock címére, az éles környezet a valódi sandboxra
 * irányíthatja (II. fejezet: konfigurációs megoldások a tesztelhetőségért).</p>
 *
 * @param baseUrl a szolgáltató alap URL-je
 * @param timeout válaszidő-korlát
 */
@ConfigurationProperties(prefix = "paypal")
public record PaypalProperties(String baseUrl, Duration timeout) {
}
