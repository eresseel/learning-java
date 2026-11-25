package hu.learning.webshop.tesztautomatizalas.keyword;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Egyszerű kulcsszóvezérelt (KDT) futtató a webshop API-hoz.
 *
 * <p>Minden kulcsszó egy üzleti szintű lépés, amit a futtató HTTP hívás(ok)ra fordít le.
 * Két fajta kulcsszó van:</p>
 * <ul>
 *   <li><b>akció</b> (pl. {@code ADD_TO_CART}): elküldi a kérést, és eltárolja a választ – NEM ellenőriz;</li>
 *   <li><b>ellenőrzés</b> (pl. {@code EXPECT_STATUS}, {@code CHECK_TOTAL}): az utolsó választ
 *       vagy a kosár aktuális állapotát veti össze az elvárt értékkel.</li>
 * </ul>
 *
 * <p>Ismeretlen kulcsszó → {@link IllegalArgumentException}: ez a tesztkörnyezet (TAS) hibája,
 * nem a SUT-é – a 09. lépés ezt külön kategóriaként („TAS error”) fogja jelenteni.</p>
 *
 * <h2>Kulcsszavak</h2>
 * <pre>
 * CLEAR_CART                     kosár ürítése
 * ADD_TO_CART;termék;db          termék kosárba
 * UPDATE_QTY;termék;db           mennyiség felülírása
 * REMOVE_FROM_CART;termék        tétel törlése
 * SEARCH;szöveg                  termékkeresés
 * PAYPAL_RESPONDS;COMPLETED|DECLINED|ERROR   a szimulált PayPal következő válasza
 * CHECKOUT                       fizetés
 * EXPECT_STATUS;kód              az utolsó válasz HTTP státusza
 * EXPECT_ERROR;kód;errorCode     az utolsó válasz hibás, a megadott hibakóddal
 * CHECK_TOTAL;összeg             a kosár végösszege
 * CHECK_ITEM_COUNT;db            a kosárban lévő darabok száma
 * CHECK_QTY;termék;db            egy tétel mennyisége
 * CHECK_RESULT_COUNT;db          az utolsó keresés találatainak száma
 * </pre>
 */
public class KeywordRunner {

    /** Napló: minden lépés kiírásra kerül, így a futás utólag visszakövethető. */
    private static final Logger LOG = LoggerFactory.getLogger(KeywordRunner.class);

    /** A SUT-ra mutató kérés-specifikáció. */
    private final RequestSpecification api;
    /** A szimulált PayPal viselkedésének beállítója (a teszt adja, pl. WireMock stub). */
    private final Consumer<String> paypalBehaviour;
    /** Az utolsó akció válasza – az ellenőrző kulcsszavak ezt vizsgálják. */
    private Response lastResponse;

    /**
     * @param api             a SUT kérés-specifikációja
     * @param paypalBehaviour a {@code PAYPAL_RESPONDS} kulcsszó megvalósítása
     */
    public KeywordRunner(RequestSpecification api, Consumer<String> paypalBehaviour) {
        this.api = api;
        this.paypalBehaviour = paypalBehaviour;
    }

    /**
     * Sorban végrehajtja a lépéseket. Az első sikertelen lépésnél megáll (a későbbi lépések
     * értelmetlenek lennének), és a hibaüzenet tartalmazza a fájlt és a sorszámot.
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
     * Egy lépés végrehajtása: kulcsszó → művelet leképezés (switch kifejezéssel).
     *
     * @param s a lépés
     */
    private void execute(KeywordStep s) {
        switch (s.keyword()) {
            // ---- Akciók ----
            case "CLEAR_CART" -> lastResponse = given().spec(api).delete("/cart");
            case "ADD_TO_CART" -> lastResponse = given().spec(api)
                    .body(Map.of("productId", s.arg(0), "quantity", Integer.parseInt(s.arg(1))))
                    .post("/cart/items");
            case "UPDATE_QTY" -> lastResponse = given().spec(api)
                    .body(Map.of("quantity", Integer.parseInt(s.arg(1))))
                    .patch("/cart/items/{id}", s.arg(0));
            case "REMOVE_FROM_CART" -> lastResponse = given().spec(api).delete("/cart/items/{id}", s.arg(0));
            case "SEARCH" -> lastResponse = given().spec(api).queryParam("q", s.arg(0)).get("/products");
            case "PAYPAL_RESPONDS" -> paypalBehaviour.accept(s.arg(0));
            case "CHECKOUT" -> lastResponse = given().spec(api).post("/checkout");
            // ---- Ellenőrzések ----
            case "EXPECT_STATUS" -> assertThat(last(s).statusCode())
                    .as("%s – HTTP státusz; válasz: %s", s, last(s).asString())
                    .isEqualTo(Integer.parseInt(s.arg(0)));
            case "EXPECT_ERROR" -> {
                assertThat(last(s).statusCode()).as("%s – HTTP státusz", s).isEqualTo(Integer.parseInt(s.arg(0)));
                assertThat(last(s).jsonPath().getString("errorCode")).as("%s – errorCode", s).isEqualTo(s.arg(1));
            }
            case "CHECK_TOTAL" -> assertThat(cart().getObject("total", BigDecimal.class))
                    .as("%s – végösszeg", s).isEqualByComparingTo(s.arg(0));
            case "CHECK_ITEM_COUNT" -> assertThat(cart().getInt("itemCount"))
                    .as("%s – darabszám", s).isEqualTo(Integer.parseInt(s.arg(0)));
            case "CHECK_QTY" -> assertThat(cart().getInt("items.find { it.productId == '" + s.arg(0) + "' }.quantity"))
                    .as("%s – tétel mennyisége", s).isEqualTo(Integer.parseInt(s.arg(1)));
            case "CHECK_RESULT_COUNT" -> assertThat(last(s).jsonPath().getList("$"))
                    .as("%s – találatok száma", s).hasSize(Integer.parseInt(s.arg(0)));
            // ---- Ismeretlen kulcsszó: a táblázat vagy a futtató hibás (TAS-hiba) ----
            default -> throw new IllegalArgumentException(s.source() + ": ismeretlen kulcsszó: " + s.keyword());
        }
    }

    /** A kosár aktuális állapota (ellenőrzésekhez; nem írja felül az utolsó akció válaszát). */
    private JsonPath cart() {
        return given().spec(api).get("/cart").then().statusCode(200).extract().jsonPath();
    }

    /** Az utolsó válasz; ha még nem volt akció, az a forgatókönyv (TAS) hibája. */
    private Response last(KeywordStep s) {
        if (lastResponse == null) {
            throw new IllegalStateException(s.source() + ": ellenőrzés előtt nem volt akció");
        }
        return lastResponse;
    }
}
