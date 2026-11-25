package hu.learning.webshop.tesztautomatizalas.taf.business;

import hu.learning.webshop.tesztautomatizalas.web.dto.CartResponse;
import hu.learning.webshop.tesztautomatizalas.web.dto.CheckoutResponse;
import java.util.Map;

/**
 * A webshop tesztelési „homlokzata” – <b>Facade</b> tervezési minta.
 *
 * <p>Egyetlen, egyszerű belépési pont a tesztszkripteknek a több lépésosztályból (kosár, termék,
 * felhasználónév, fizetés) álló alrendszerhez. A gyakori, többlépéses üzleti folyamatokat
 * (pl. „vegyél meg ezeket a termékeket”) egy hívásba sűríti; a részletes lépések továbbra is
 * elérhetők az accessorokon át (pl. {@code shop.cart().tryAdd(...)}).</p>
 *
 * <p>SOLID: a facade csak delegál (egyetlen felelősség: összefogás); a lépésosztályok
 * egymástól függetlenek és külön is használhatók.</p>
 */
public class ShopFacade {

    /** Kosár lépései. */
    private final CartSteps cart = new CartSteps();
    /** Termék lépései. */
    private final ProductSteps products = new ProductSteps();
    /** Felhasználónév lépései. */
    private final UsernameSteps usernames = new UsernameSteps();
    /** Fizetés lépései. */
    private final CheckoutSteps checkout = new CheckoutSteps();

    /** @return a kosár lépései */
    public CartSteps cart() {
        return cart;
    }

    /** @return a termék lépései */
    public ProductSteps products() {
        return products;
    }

    /** @return a felhasználónév lépései */
    public UsernameSteps usernames() {
        return usernames;
    }

    /** @return a fizetés lépései */
    public CheckoutSteps checkout() {
        return checkout;
    }

    /** Tiszta kiinduló állapot minden teszt előtt: üres kosár. */
    public void startWithEmptyCart() {
        cart.clear();
    }

    /**
     * Üres kosárból indulva feltölti a kosarat a megadott tételekkel.
     *
     * @param items termékazonosító → mennyiség (a beszúrási sorrend számít, ha {@link java.util.LinkedHashMap})
     * @return a kosár állapota a feltöltés után
     */
    public CartResponse fillCart(Map<String, Integer> items) {
        cart.clear();
        items.forEach(cart::add);
        return cart.current();
    }

    /**
     * A teljes vásárlási folyamat egy hívásban: kosár feltöltése és fizetés.
     * (A PayPal viselkedését előtte a tesztnek kell beállítania.)
     *
     * @param items termékazonosító → mennyiség
     * @return a sikeres fizetés eredménye
     */
    public CheckoutResponse buy(Map<String, Integer> items) {
        fillCart(items);
        return checkout.pay();
    }
}
