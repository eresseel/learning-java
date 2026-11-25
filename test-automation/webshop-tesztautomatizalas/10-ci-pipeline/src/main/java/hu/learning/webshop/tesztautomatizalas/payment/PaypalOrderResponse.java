package hu.learning.webshop.tesztautomatizalas.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A „PayPal” válasza – csak a számunkra fontos mezőkkel.
 *
 * <p>A valódi szolgáltató sok további mezőt küld (pl. {@code links}); az
 * {@code ignoreUnknown} miatt ezek nem okoznak hibát – egy külső API bővülése ne törje
 * el a kliensünket (ugyanaz az elv, mint a 05. lépés szerződéstesztjében).</p>
 *
 * @param id     a fizetés azonosítója a szolgáltatónál
 * @param status a fizetés állapota; siker: {@code COMPLETED}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PaypalOrderResponse(String id, String status) {
}
