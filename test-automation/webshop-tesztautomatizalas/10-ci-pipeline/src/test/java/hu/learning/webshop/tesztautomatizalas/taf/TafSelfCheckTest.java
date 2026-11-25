package hu.learning.webshop.tesztautomatizalas.taf;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.learning.webshop.tesztautomatizalas.taf.keyword.KeywordScriptParser;
import hu.learning.webshop.tesztautomatizalas.taf.keyword.KeywordStep;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * <b>TAF/TAS konfigurációs teszt</b> (V. fejezet, 36. dia: „build során futnak, fájlok/elérési
 * utak ellenőrzése”).
 *
 * <p>Még a SUT elindítása előtt ellenőrzi, hogy a TAS saját erőforrásai megvannak és formailag
 * helyesek: sémák, szerződés, tesztadatok, kulcsszó-forgatókönyvek. Így egy hiányzó vagy elgépelt
 * fájl a build elején, egyértelmű üzenettel derül ki – nem egy rendszerteszt közepén, félrevezető
 * hibaként.</p>
 */
class TafSelfCheckTest {

    /** A JSON erőforrásoknak létezniük kell és érvényes JSON-nak kell lenniük. */
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
            "/schemas/product.json", "/schemas/product-list.json", "/schemas/cart.json",
            "/schemas/problem.json", "/schemas/username-validation.json", "/schemas/checkout.json",
            "/contracts/webshop-frontend--webshop-api.json"})
    void jsonResourceIsValid(String resource) throws Exception {
        try (InputStream in = getClass().getResourceAsStream(resource)) {
            assertThat(in).as("hiányzó erőforrás: %s", resource).isNotNull();
            assertThat(new ObjectMapper().readTree(in).isObject()).isTrue();
        }
    }

    /** A CSV tesztadatok léteznek és van fejlécük + legalább egy adatsoruk. */
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"/testdata/usernames.csv", "/testdata/cart-quantities.csv", "/testdata/search.csv"})
    void testDataFileHasRows(String resource) throws Exception {
        try (InputStream in = getClass().getResourceAsStream(resource)) {
            assertThat(in).as("hiányzó tesztadat: %s", resource).isNotNull();
            assertThat(new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8).lines()
                    .filter(l -> !l.isBlank()).count()).isGreaterThanOrEqualTo(2);
        }
    }

    /** A kulcsszó-forgatókönyvek csak ismert kulcsszavakat használnak (elgépelés a build elején kiderül). */
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"kosar-alap.csv", "kosar-hibak.csv", "vasarlas-fizetessel.csv"})
    void keywordScriptsUseKnownKeywords(String script) throws Exception {
        Set<String> known = Set.of("CLEAR_CART", "ADD_TO_CART", "UPDATE_QTY", "REMOVE_FROM_CART", "SEARCH",
                "PAYPAL_RESPONDS", "CHECKOUT", "EXPECT_STATUS", "EXPECT_ERROR", "CHECK_TOTAL",
                "CHECK_ITEM_COUNT", "CHECK_QTY", "CHECK_RESULT_COUNT");
        List<KeywordStep> steps = KeywordScriptParser.parse("/keywords/" + script);
        assertThat(steps).isNotEmpty().extracting(KeywordStep::keyword).allMatch(known::contains);
    }
}
