package hu.learning.webshop.tesztautomatizalas.tests;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import hu.learning.webshop.tesztautomatizalas.taf.keyword.KeywordRunner;
import hu.learning.webshop.tesztautomatizalas.taf.keyword.KeywordScriptParser;
import hu.learning.webshop.tesztautomatizalas.taf.keyword.KeywordStep;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Kulcsszóvezérelt forgatókönyvek – a futtató már az üzleti réteget használja. */
@DisplayName("Tesztszkript – kulcsszóvezérelt")
class KeywordDrivenTest extends AbstractShopTest {

    /**
     * @param script a forgatókönyv fájlneve ({@code src/test/resources/keywords/})
     * @throws Exception ha a fájl nem olvasható
     */
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"kosar-alap.csv", "kosar-hibak.csv", "vasarlas-fizetessel.csv"})
    void runScenario(String script) throws Exception {
        new KeywordRunner(shop, PAYPAL).run(KeywordScriptParser.parse("/keywords/" + script));
    }

    /** Ismeretlen kulcsszó = TAS-hiba, érthető üzenettel. */
    @Test
    void unknownKeywordIsTasError() {
        List<KeywordStep> steps = List.of(new KeywordStep("teszt.csv:1", "ADD_TO_BASKET", List.of("P001", "1")));
        assertThatThrownBy(() -> new KeywordRunner(shop, PAYPAL).run(steps))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("teszt.csv:1: ismeretlen kulcsszó: ADD_TO_BASKET");
    }
}
