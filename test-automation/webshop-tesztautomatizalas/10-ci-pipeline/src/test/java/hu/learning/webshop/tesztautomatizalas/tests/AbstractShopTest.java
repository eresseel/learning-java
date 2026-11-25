package hu.learning.webshop.tesztautomatizalas.tests;

import hu.learning.webshop.tesztautomatizalas.taf.business.ShopFacade;
import hu.learning.webshop.tesztautomatizalas.taf.core.ApiClient;
import hu.learning.webshop.tesztautomatizalas.taf.core.Config;
import hu.learning.webshop.tesztautomatizalas.taf.core.PaypalSimulator;
import hu.learning.webshop.tesztautomatizalas.taf.core.PreflightCheck;
import hu.learning.webshop.tesztautomatizalas.taf.core.ResultClassifierExtension;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * Az összes rendszerszintű tesztszkript közös őse – a 09. lépésben konfigurációval,
 * előzetes környezet-ellenőrzéssel és eredmény-osztályozással bővítve.
 *
 * <p>Életciklus:</p>
 * <ol>
 *   <li>Spring elindítja a SUT-ot véletlen porton (a PayPal a szimulátorra mutat);</li>
 *   <li>{@code @BeforeAll}: konfiguráció betöltése ({@link Config#load(int)}), naplózása, majd
 *       <b>preflight</b> (a SUT health végpontja, tesztadat-előfeltétel);</li>
 *   <li>{@code @BeforeEach}: ha a preflight sikertelen, a teszt megszakad → <b>Inconclusive</b>;
 *       egyébként tiszta állapot (szimulátor, kosár);</li>
 *   <li>a {@link ResultClassifierExtension} minden eredményt Passed / Failed / TAS error /
 *       Inconclusive kategóriába sorol és naplóz.</li>
 * </ol>
 *
 * <p>A {@code PER_CLASS} életciklus miatt a {@code @BeforeAll} nem statikus, így már látja a
 * Spring által injektált portot.</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ResultClassifierExtension.class)
public abstract class AbstractShopTest {

    /** A TAS konfigurációs és előkészítési naplója. */
    private static final Logger LOG = LoggerFactory.getLogger("TAS.SETUP");

    /** A teljes futás alatt közös PayPal szimulátor (core réteg). */
    protected static final PaypalSimulator PAYPAL = PaypalSimulator.shared();

    /** A lokálisan indított SUT véletlen portja. */
    @LocalServerPort
    private int port;

    /** Az üzleti réteg belépési pontja (Facade). */
    protected final ShopFacade shop = new ShopFacade();

    /** A preflight eredménye ({@code null}, ha ki van kapcsolva). */
    private PreflightCheck.Report preflight;

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

    /**
     * Konfiguráció és előzetes környezet-ellenőrzés tesztosztályonként egyszer
     * (a preflight eredménye környezetenként gyorsítótárazott).
     */
    @BeforeAll
    void configureAndPreflight() {
        // 1. Konfiguráció: -Dwebshop.* → WEBSHOP_* → alapértelmezés (lokális SUT)
        Config config = Config.load(port);
        LOG.info("event=TAS_CONFIG {}", config.describe());
        ApiClient.getInstance().configure(config);
        // 2. Preflight: ha a környezet nem jó, a teszt nem „piros”, hanem nem eldönthető
        if (config.preflight()) {
            preflight = PreflightCheck.runOnce(config.baseUrl(), config.requestTimeout());
            LOG.info("event=TAS_PREFLIGHT {}", preflight.summary());
        }
    }

    /**
     * Minden teszt előtt: ha a preflight sikertelen volt, a tesztet megszakítjuk (→ Inconclusive);
     * egyébként tiszta állapotot teremtünk (szimulátor, kosár).
     * (A megszakítás tesztenként történik, hogy az eredmény-osztályozó minden tesztet lásson.)
     */
    @BeforeEach
    void resetState() {
        if (preflight != null) {
            Assumptions.assumeTrue(preflight.ok(), preflight::summary);
        }
        PAYPAL.reset();
        shop.startWithEmptyCart();
    }
}
