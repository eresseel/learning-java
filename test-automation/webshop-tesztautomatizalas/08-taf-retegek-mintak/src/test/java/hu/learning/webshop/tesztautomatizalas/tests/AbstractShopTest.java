package hu.learning.webshop.tesztautomatizalas.tests;

import hu.learning.webshop.tesztautomatizalas.taf.business.ShopFacade;
import hu.learning.webshop.tesztautomatizalas.taf.core.ApiClient;
import hu.learning.webshop.tesztautomatizalas.taf.core.Config;
import hu.learning.webshop.tesztautomatizalas.taf.core.PaypalSimulator;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * Az összes rendszerszintű tesztszkript közös őse.
 *
 * <p>Feladata csak a „bekötés”: elindítja a SUT-ot (RANDOM_PORT), a PayPal szimulátor címét
 * átadja neki, beállítja a core réteg {@link ApiClient}-jét, és minden teszt előtt tiszta
 * állapotot teremt. Mivel minden tesztosztály <b>ugyanazt</b> a konfigurációt használja, a Spring
 * egyetlen kontextust indít az összeshez (a 07. lépésben még három külön kontextus indult).</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractShopTest {

    /** A teljes futás alatt közös PayPal szimulátor (core réteg). */
    protected static final PaypalSimulator PAYPAL = PaypalSimulator.shared();

    /** A SUT véletlen portja. */
    @LocalServerPort
    private int port;

    /** Az üzleti réteg belépési pontja (Facade) – a tesztszkriptek ezen keresztül dolgoznak. */
    protected final ShopFacade shop = new ShopFacade();

    /**
     * A SUT konfigurációja: a fizetés a szimulátorhoz menjen, rövid időkorláttal.
     *
     * @param registry dinamikus property-regiszter
     */
    @DynamicPropertySource
    static void sutProperties(DynamicPropertyRegistry registry) {
        registry.add("paypal.base-url", PAYPAL::baseUrl);
        registry.add("paypal.timeout", () -> "500ms");
    }

    /** Minden teszt előtt: a core réteg a SUT-ra mutat, a szimulátor és a kosár tiszta. */
    @BeforeEach
    void connectAndReset() {
        ApiClient.getInstance().configure(Config.local(port));
        PAYPAL.reset();
        shop.startWithEmptyCart();
    }
}
