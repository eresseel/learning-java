package hu.learning.webshop.tesztautomatizalas.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.learning.webshop.tesztautomatizalas.taf.business.ProviderStates;
import hu.learning.webshop.tesztautomatizalas.taf.core.ApiClient;
import hu.learning.webshop.tesztautomatizalas.taf.core.JsonShapeMatcher;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

/**
 * <b>Szolgáltató oldali szerződés-verifikáció</b> (consumer-driven contract testing).
 *
 * <p>A fogyasztó (frontend) csapata megírja, milyen kéréseket küld és a válaszból mire támaszkodik
 * ({@code contracts/webshop-frontend--webshop-api.json}). A szolgáltató (ez a backend) buildje
 * minden interakciót lejátszik a saját, valóban futó API-ja ellen. Ha a backend olyan változást
 * vezet be, ami a fogyasztót eltörné (pl. átnevez egy mezőt), <b>itt, a build-ben</b> bukik el –
 * nem a közös integrációs környezetben (V. fejezet: korai hibaazonosítás).</p>
 *
 * <p>Minden interakció külön dinamikus teszt ({@code @TestFactory}), a neve az interakció leírása.</p>
 *
 * <p>TAF-rétegek: ez a tesztszkript <em>adatvezérelt</em> – a HTTP kéréseket a szerződésfájl írja le,
 * ezért kivételesen a core réteg {@link ApiClient}-jét használja közvetlenül; a provider state-eket
 * viszont az üzleti réteg ({@link ProviderStates} → {@code ShopFacade}) állítja be.</p>
 */
class ContractVerificationTest extends AbstractShopTest {

    /** A szerződésfájl helye a classpath-on. */
    private static final String CONTRACT = "/contracts/webshop-frontend--webshop-api.json";

    /** JSON olvasó (Jackson). */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Beolvassa a szerződést, és interakciónként egy dinamikus tesztet készít.
     *
     * @return a dinamikus tesztek folyama
     * @throws IOException ha a szerződésfájl nem olvasható
     */
    @TestFactory
    Stream<DynamicTest> providerHonoursConsumerContract() throws IOException {
        JsonNode contract = readContract();
        String title = contract.get("consumer").asText() + " → " + contract.get("provider").asText();
        // Minden interakcióból egy DynamicTest: név + végrehajtandó lambda
        return StreamSupport.stream(contract.get("interactions").spliterator(), false)
                .map(interaction -> DynamicTest.dynamicTest(
                        title + ": " + interaction.get("description").asText(),
                        () -> verify(interaction)));
    }

    /**
     * Egyetlen interakció lejátszása és ellenőrzése.
     *
     * @param interaction a szerződés egy eleme
     */
    private void verify(JsonNode interaction) {
        // 1. Arrange – a szolgáltatói állapot előállítása
        ProviderStates.setUp(interaction.get("providerState").asText(), shop);

        // 2. Act – a kérés összeállítása a szerződésből és elküldése
        JsonNode req = interaction.get("request");
        RequestSpecification request = ApiClient.getInstance().request();
        if (req.has("query")) {
            req.get("query").fields().forEachRemaining(e -> request.queryParam(e.getKey(), e.getValue().asText()));
        }
        if (req.has("body")) {
            request.body(req.get("body").toString());
        }
        Response response = request.request(req.get("method").asText(), req.get("path").asText());

        // 3. Assert – státusz, fejlécek, törzs szerkezete és pontos értékei
        JsonNode expected = interaction.get("response");
        assertThat(response.statusCode()).as("HTTP státusz").isEqualTo(expected.get("status").asInt());
        if (expected.has("headers")) {
            expected.get("headers").fields().forEachRemaining(h ->
                    assertThat(response.header(h.getKey())).as("fejléc: " + h.getKey())
                            .startsWith(h.getValue().asText()));
        }
        if (expected.has("body")) {
            JsonNode actualBody = parse(response.asString());
            List<String> errors = JsonShapeMatcher.mismatches(expected.get("body"), actualBody, exactRules(expected));
            assertThat(errors).as("szerződéssértések; valódi válasz: " + actualBody).isEmpty();
        }
    }

    /** Az {@code exact} szabályok beolvasása Map-be (JSON Pointer → érték). */
    private static Map<String, JsonNode> exactRules(JsonNode expectedResponse) {
        Map<String, JsonNode> rules = new LinkedHashMap<>();
        if (expectedResponse.has("exact")) {
            expectedResponse.get("exact").fields().forEachRemaining(e -> rules.put(e.getKey(), e.getValue()));
        }
        return rules;
    }

    /** A szerződésfájl beolvasása a classpath-ról. */
    private static JsonNode readContract() throws IOException {
        try (InputStream in = ContractVerificationTest.class.getResourceAsStream(CONTRACT)) {
            if (in == null) {
                // TAS-hiba: a tesztkörnyezet hiányos (VII. fejezet: TAS konfiguráció ellenőrzése)
                throw new IllegalStateException("A szerződésfájl nem található: " + CONTRACT);
            }
            return MAPPER.readTree(in);
        }
    }

    /** Szöveg → JSON fa; ellenőrzött kivétel helyett futásidejűt dob. */
    private static JsonNode parse(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (IOException e) {
            throw new IllegalStateException("A válasz nem JSON: " + json, e);
        }
    }
}
