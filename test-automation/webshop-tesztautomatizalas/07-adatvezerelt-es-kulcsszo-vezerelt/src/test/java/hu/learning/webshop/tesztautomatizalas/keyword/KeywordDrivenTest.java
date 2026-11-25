package hu.learning.webshop.tesztautomatizalas.keyword;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import hu.learning.webshop.tesztautomatizalas.api.AbstractApiTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * Kulcsszóvezérelt (KDT) tesztek: a forgatókönyvek a {@code src/test/resources/keywords/}
 * mappában vannak, ez az osztály csak „lejátssza” őket a {@link KeywordRunner}-rel.
 *
 * <p>Új forgatókönyvhöz Java kódot sem kell írni: elég egy új CSV fájl és a neve a listába.</p>
 */
@DisplayName("KDT – kulcsszóvezérelt forgatókönyvek")
class KeywordDrivenTest extends AbstractApiTest {

    /** A szimulált PayPal a {@code PAYPAL_RESPONDS} és {@code CHECKOUT} kulcsszavakhoz. */
    @RegisterExtension
    static WireMockExtension paypal = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().http2PlainDisabled(true))
            .build();

    /**
     * A webshop a WireMock címére küldi a fizetési kéréseket.
     *
     * @param registry dinamikus property-regiszter
     */
    @DynamicPropertySource
    static void paypalProperties(DynamicPropertyRegistry registry) {
        registry.add("paypal.base-url", paypal::baseUrl);
    }

    /**
     * Minden forgatókönyv egy tesztfuttatás; a név a fájlnév.
     *
     * @param script a forgatókönyv fájlneve
     * @throws Exception ha a fájl nem olvasható
     */
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"kosar-alap.csv", "kosar-hibak.csv", "vasarlas-fizetessel.csv"})
    void runScenario(String script) throws Exception {
        // 1. Arrange – a forgatókönyv beolvasása és a futtató felépítése
        List<KeywordStep> steps = KeywordScriptParser.parse("/keywords/" + script);
        KeywordRunner runner = new KeywordRunner(api, this::stubPaypal);
        // 2. Act + Assert – az ellenőrzések a kulcsszavakban vannak
        runner.run(steps);
    }

    /** A futtató maga is TAF-kód: ismeretlen kulcsszóra érthető hibát kell adnia. */
    @Test
    void unknownKeywordIsTasError() {
        KeywordRunner runner = new KeywordRunner(api, this::stubPaypal);
        List<KeywordStep> steps = List.of(new KeywordStep("teszt.csv:1", "ADD_TO_BASKET", List.of("P001", "1")));
        assertThatThrownBy(() -> runner.run(steps))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("teszt.csv:1: ismeretlen kulcsszó: ADD_TO_BASKET");
    }

    /**
     * A {@code PAYPAL_RESPONDS} kulcsszó megvalósítása: WireMock stub a kért viselkedéssel.
     *
     * @param behaviour {@code COMPLETED}, {@code DECLINED} vagy {@code ERROR}
     */
    private void stubPaypal(String behaviour) {
        ResponseDefinitionBuilder response = switch (behaviour) {
            case "COMPLETED" -> okJson("{\"id\":\"PAY-KDT\",\"status\":\"COMPLETED\"}");
            case "DECLINED" -> aResponse().withStatus(422);
            case "ERROR" -> aResponse().withStatus(500);
            default -> throw new IllegalArgumentException("Ismeretlen PayPal viselkedés: " + behaviour);
        };
        paypal.stubFor(post("/v2/checkout/orders").willReturn(response));
    }
}
