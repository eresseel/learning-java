package hu.learning.webshop.tesztautomatizalas.systemtests;

import hu.learning.webshop.tesztautomatizalas.taf.business.ShopFacade;
import hu.learning.webshop.tesztautomatizalas.taf.core.ApiClient;
import hu.learning.webshop.tesztautomatizalas.taf.core.Config;
import hu.learning.webshop.tesztautomatizalas.taf.core.PreflightCheck;
import hu.learning.webshop.tesztautomatizalas.taf.core.ResultClassifierExtension;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A telepítés utáni rendszertesztek közös őse – Spring nélkül, külső SUT ellen.
 *
 * <p>A SUT címét kötelező megadni ({@code -Dwebshop.baseUrl} vagy {@code WEBSHOP_BASE_URL}); a
 * Maven a {@code pom.xml}-ben ezt automatikusan beállítja (helyi futásnál a saját maga által
 * elindított alkalmazás címére, a {@code deploy-gate} profilban a környezeti változóra).</p>
 *
 * <p>Telepítés utáni környezetben a preflight különösen fontos: ha a telepítés nem sikerült
 * (a SUT nem UP), az ne „SUT-hibaként”, hanem Inconclusive-ként jelenjen meg.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ResultClassifierExtension.class)
abstract class AbstractSystemIT {

    /** Előkészítési napló. */
    private static final Logger LOG = LoggerFactory.getLogger("TAS.SETUP");

    /** Az üzleti réteg belépési pontja. */
    protected final ShopFacade shop = new ShopFacade();

    /** A preflight eredménye. */
    private PreflightCheck.Report preflight;

    /** Konfiguráció (külső SUT kötelező: lokális port 0) és preflight. */
    @BeforeAll
    void configureAndPreflight() {
        Config config = Config.load(0);
        LOG.info("event=TAS_CONFIG {}", config.describe());
        ApiClient.getInstance().configure(config);
        if (config.preflight()) {
            preflight = PreflightCheck.runOnce(config.baseUrl(), config.requestTimeout());
            LOG.info("event=TAS_PREFLIGHT {}", preflight.summary());
        }
    }

    /** Sikertelen preflight → Inconclusive; egyébként üres kosár. */
    @BeforeEach
    void resetState() {
        if (preflight != null) {
            Assumptions.assumeTrue(preflight.ok(), preflight::summary);
        }
        shop.startWithEmptyCart();
    }
}
