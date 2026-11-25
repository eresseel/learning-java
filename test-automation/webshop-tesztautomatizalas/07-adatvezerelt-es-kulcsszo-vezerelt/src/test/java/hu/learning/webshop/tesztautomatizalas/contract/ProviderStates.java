package hu.learning.webshop.tesztautomatizalas.contract;

import static io.restassured.RestAssured.given;

import io.restassured.specification.RequestSpecification;
import java.util.Map;

/**
 * A szerződésben hivatkozott „szolgáltatói állapotok” (provider states) beállítása.
 *
 * <p>A fogyasztó azt írja: <i>„a kosárban 1 db P001 van”</i> – a szolgáltató oldali tesztnek kell
 * ezt az állapotot előállítania az interakció lejátszása előtt. A Pact is pontosan így működik.
 * Itt a SUT saját API-ját használjuk (irányíthatóság).</p>
 */
final class ProviderStates {

    /** Segédosztály. */
    private ProviderStates() {
    }

    /**
     * Beállítja a megnevezett állapotot.
     *
     * @param state a szerződésben szereplő állapotnév
     * @param api   a SUT-ra mutató kérés-specifikáció
     * @throws IllegalArgumentException ha az állapot ismeretlen – ez TAS-hiba (a szerződés és a
     *                                  verifikáló kód nincs szinkronban), nem SUT-hiba
     */
    static void setUp(String state, RequestSpecification api) {
        // Állapotnév → előkészítő lépés. A switch kifejezés minden ismert állapotot felsorol.
        switch (state) {
            case "nincs előfeltétel", "a katalógus a mintatermékeket tartalmazza" -> {
                // A katalógus fix mintaadat, nincs teendő
            }
            case "a kosár üres" -> given().spec(api).delete("/cart").then().statusCode(204);
            case "a kosárban 1 db P001 van" -> {
                given().spec(api).delete("/cart").then().statusCode(204);
                given().spec(api).body(Map.of("productId", "P001", "quantity", 1))
                        .post("/cart/items").then().statusCode(201);
            }
            default -> throw new IllegalArgumentException("Ismeretlen provider state: " + state);
        }
    }
}
