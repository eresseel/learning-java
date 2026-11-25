package hu.learning.webshop.tesztautomatizalas.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * A „PayPal” felé küldött rendelés (a PayPal Orders v2 API leegyszerűsített mása).
 *
 * <p>A PayPal snake_case mezőneveket használ – a {@code @JsonProperty} köti össze a Java
 * (camelCase) neveket a JSON nevekkel.</p>
 *
 * @param intent        a szándék, nálunk mindig {@code CAPTURE} (azonnali terhelés)
 * @param purchaseUnits a vásárlási egységek (nálunk egy)
 */
public record PaypalOrderRequest(
        String intent,
        @JsonProperty("purchase_units") List<PurchaseUnit> purchaseUnits) {

    /**
     * @param referenceId a webshop saját rendelésazonosítója
     * @param amount      összeg
     */
    public record PurchaseUnit(@JsonProperty("reference_id") String referenceId, Amount amount) {
    }

    /**
     * @param currencyCode ISO 4217 pénznemkód
     * @param value        összeg szövegként, 2 tizedesjeggyel (a PayPal így várja)
     */
    public record Amount(@JsonProperty("currency_code") String currencyCode, String value) {
    }
}
