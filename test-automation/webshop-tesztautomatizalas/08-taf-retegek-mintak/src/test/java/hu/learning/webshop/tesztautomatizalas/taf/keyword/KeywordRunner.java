package hu.learning.webshop.tesztautomatizalas.taf.keyword;

import static org.assertj.core.api.Assertions.assertThat;

import hu.learning.webshop.tesztautomatizalas.taf.business.ShopFacade;
import hu.learning.webshop.tesztautomatizalas.taf.core.PaypalSimulator;
import hu.learning.webshop.tesztautomatizalas.web.dto.CartItemResponse;
import io.restassured.response.Response;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kulcsszóvezérelt (KDT) futtató – a 07. lépés futtatója, átírva a TAF rétegekre.
 *
 * <p>Változás a 07. lépéshez képest: a kulcsszavak már nem maguk építik a HTTP kéréseket, hanem
 * az üzleti réteg ({@link ShopFacade}) lépéseit hívják, a PayPal-t pedig a core réteg
 * {@link PaypalSimulator}-a vezérli. A kulcsszó így valóban „üzleti szó” lett, a futtató rövidebb,
 * és egy API-változás miatt nem kell módosítani.</p>
 *
 * <p>Kulcsszavak: lásd a README táblázatát (ugyanazok, mint a 07. lépésben).</p>
 */
public class KeywordRunner {

    /** Lépésnapló. */
    private static final Logger LOG = LoggerFactory.getLogger(KeywordRunner.class);

    /** Az üzleti réteg belépési pontja. */
    private final ShopFacade shop;
    /** A szimulált fizetési szolgáltató. */
    private final PaypalSimulator paypal;
    /** Az utolsó akció válasza. */
    private Response lastResponse;
    /** Az utolsó keresés találatainak száma. */
    private int lastResultCount = -1;

    /**
     * @param shop   a webshop facade
     * @param paypal a PayPal szimulátor
     */
    public KeywordRunner(ShopFacade shop, PaypalSimulator paypal) {
        this.shop = shop;
        this.paypal = paypal;
    }

    /**
     * Sorban végrehajtja a lépéseket; az első hibánál megáll.
     *
     * @param steps a forgatókönyv lépései
     */
    public void run(List<KeywordStep> steps) {
        for (KeywordStep step : steps) {
            LOG.info("KDT lépés: {}", step);
            execute(step);
        }
    }

    /**
     * Kulcsszó → üzleti lépés leképezés.
     *
     * @param s a lépés
     */
    private void execute(KeywordStep s) {
        switch (s.keyword()) {
            // ---- Akciók: az üzleti réteg try* metódusai (nem ellenőriznek) ----
            case "CLEAR_CART" -> shop.startWithEmptyCart();
            case "ADD_TO_CART" -> lastResponse = shop.cart().tryAdd(s.arg(0), Integer.parseInt(s.arg(1)));
            case "UPDATE_QTY" -> lastResponse = shop.cart().tryUpdateQuantity(s.arg(0), Integer.parseInt(s.arg(1)));
            case "REMOVE_FROM_CART" -> lastResponse = shop.cart().tryRemove(s.arg(0));
            case "SEARCH" -> lastResultCount = shop.products().search(s.arg(0)).size();
            case "PAYPAL_RESPONDS" -> configurePaypal(s);
            case "CHECKOUT" -> lastResponse = shop.checkout().tryPay();
            // ---- Ellenőrzések ----
            case "EXPECT_STATUS" -> assertThat(last(s).statusCode())
                    .as("%s – HTTP státusz; válasz: %s", s, last(s).asString())
                    .isEqualTo(Integer.parseInt(s.arg(0)));
            case "EXPECT_ERROR" -> {
                assertThat(last(s).statusCode()).as("%s – HTTP státusz", s).isEqualTo(Integer.parseInt(s.arg(0)));
                assertThat(last(s).jsonPath().getString("errorCode")).as("%s – errorCode", s).isEqualTo(s.arg(1));
            }
            case "CHECK_TOTAL" -> assertThat(shop.cart().current().total())
                    .as("%s – végösszeg", s).isEqualByComparingTo(s.arg(0));
            case "CHECK_ITEM_COUNT" -> assertThat(shop.cart().current().itemCount())
                    .as("%s – darabszám", s).isEqualTo(Integer.parseInt(s.arg(0)));
            case "CHECK_QTY" -> assertThat(shop.cart().current().items())
                    .as("%s – tétel mennyisége", s)
                    .filteredOn(i -> i.productId().equals(s.arg(0)))
                    .singleElement()
                    .extracting(CartItemResponse::quantity)
                    .isEqualTo(Integer.parseInt(s.arg(1)));
            case "CHECK_RESULT_COUNT" -> assertThat(lastResultCount)
                    .as("%s – találatok száma", s).isEqualTo(Integer.parseInt(s.arg(0)));
            default -> throw new IllegalArgumentException(s.source() + ": ismeretlen kulcsszó: " + s.keyword());
        }
    }

    /** A {@code PAYPAL_RESPONDS} kulcsszó: a szimulátor szándék-alapú metódusaira képez le. */
    private void configurePaypal(KeywordStep s) {
        switch (s.arg(0)) {
            case "COMPLETED" -> paypal.willComplete("PAY-KDT");
            case "DECLINED" -> paypal.willDecline();
            case "ERROR" -> paypal.willFail(500);
            default -> throw new IllegalArgumentException(s.source() + ": ismeretlen PayPal viselkedés: " + s.arg(0));
        }
    }

    /** Az utolsó válasz; ha még nem volt akció, az a forgatókönyv (TAS) hibája. */
    private Response last(KeywordStep s) {
        if (lastResponse == null) {
            throw new IllegalStateException(s.source() + ": ellenőrzés előtt nem volt akció");
        }
        return lastResponse;
    }
}
