package hu.laci.learning.service;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A {@link PostService} tesztjei WireMock-kal: a külső szolgáltatást egy helyi, programozható
 * HTTP szerver játssza el. Így tetszőleges válaszokat (hibákat, lassúságot) szimulálhatunk.
 */
class PostServiceTest {

    // A WireMock JUnit 5 kiegészítője: a teszt előtt elindítja a mock szervert, utána leállítja,
    // és minden teszt után törli a stubokat, így a tesztek egymástól függetlenek maradnak.
    // dynamicPort(): szabad portot választ, így nem ütközik más futó alkalmazással.
    // A mező statikus, hogy a szerver az osztály összes tesztje között egyszer induljon el.
    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    // A szolgáltatás a mock szerver címére mutat, 1 másodperces időkorláttal.
    private PostService createService() {
        return new PostService(wireMock.baseUrl(), Duration.ofSeconds(1));
    }

    // Boldog ág: a szerver 200-zal és JSON-nal válaszol, a szolgáltatás a címet adja vissza.
    @Test
    void shouldReturnTitleWhenServerRespondsWithPost() {
        // stubFor: megmondjuk a mock szervernek, mit válaszoljon egy adott kérésre.
        wireMock.stubFor(get(urlEqualTo("/posts/1"))
                .willReturn(okJson("""
                        {"userId": 1, "id": 1, "title": "Hello WireMock", "body": "text"}
                        """)));

        String title = createService().fetchTitle(1);

        assertEquals("Hello WireMock", title);
        // verify: ellenőrizzük, hogy a szolgáltatás tényleg pontosan egyszer hívta meg a végpontot.
        wireMock.verify(1, getRequestedFor(urlEqualTo("/posts/1")));
    }

    // Hiba: a külső szolgáltatás 404-et ad, a szolgáltatásnak érthető kivételt kell dobnia.
    @Test
    void shouldFailWhenPostIsNotFound() {
        wireMock.stubFor(get(urlEqualTo("/posts/42"))
                .willReturn(aResponse().withStatus(404)));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> createService().fetchTitle(42));

        assertTrue(exception.getMessage().contains("404"));
    }

    // Hiba: a külső szolgáltatás összeomlik (500). Ezt éles API-val nem lehetne megbízhatóan kiváltani.
    @Test
    void shouldFailWhenServerReturnsError() {
        wireMock.stubFor(get(urlEqualTo("/posts/1"))
                .willReturn(aResponse().withStatus(500)));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> createService().fetchTitle(1));

        assertTrue(exception.getMessage().contains("500"));
    }

    // Hiba: a válasz hibás JSON, a szolgáltatás nem szállhat el váratlan kivétellel.
    @Test
    void shouldFailWhenResponseBodyIsNotJson() {
        wireMock.stubFor(get(urlEqualTo("/posts/1"))
                .willReturn(aResponse().withStatus(200).withBody("this is not json")));

        assertThrows(IllegalStateException.class, () -> createService().fetchTitle(1));
    }

    // Hiba: a válaszból hiányzik a title mező.
    @Test
    void shouldFailWhenTitleIsMissing() {
        wireMock.stubFor(get(urlEqualTo("/posts/1"))
                .willReturn(okJson("{\"id\": 1}")));

        assertThrows(IllegalStateException.class, () -> createService().fetchTitle(1));
    }

    // Lassú szolgáltatás: a válasz 3 másodperc késéssel jönne, de az időkorlát 1 másodperc,
    // ezért időtúllépést kell kapnunk.
    @Test
    void shouldFailWhenServerIsTooSlow() {
        wireMock.stubFor(get(urlEqualTo("/posts/1"))
                .willReturn(okJson("{\"title\": \"slow\"}").withFixedDelay(3000)));

        assertThrows(IllegalStateException.class, () -> createService().fetchTitle(1));
    }

    // Hálózati hiba szimulálása: a szerver a válasz közben megszakítja a kapcsolatot.
    @Test
    void shouldFailWhenConnectionIsReset() {
        wireMock.stubFor(get(urlEqualTo("/posts/1"))
                .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

        assertThrows(IllegalStateException.class, () -> createService().fetchTitle(1));
    }
}
