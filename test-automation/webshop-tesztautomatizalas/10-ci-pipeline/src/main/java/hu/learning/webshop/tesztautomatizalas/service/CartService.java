package hu.learning.webshop.tesztautomatizalas.service;

import hu.learning.webshop.tesztautomatizalas.domain.Cart;
import hu.learning.webshop.tesztautomatizalas.domain.ProductCatalog;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 * A kosár szolgáltatásréteg: egyetlen, közös, memóriabeli kosarat kezel.
 *
 * <p>Egyszerűsítés: a demóban nincs bejelentkezés, ezért egy kosár van. A {@link Cart}
 * nem szálbiztos, a webszerver viszont párhuzamos kéréseket szolgál ki – ezért minden
 * hozzáférés {@code synchronized}.</p>
 */
@Service
public class CartService {

    /** Az egyetlen kosár. */
    private final Cart cart;

    /**
     * @param catalog a termékkatalógus (Spring konstruktor-injektálással adja)
     */
    public CartService(ProductCatalog catalog) {
        this.cart = new Cart(catalog);
    }

    /**
     * Kölcsönös kizárással végrehajt egy műveletet a kosáron, és visszaadja az eredményét.
     *
     * @param action a kosáron végzett művelet (pl. {@code c -> c.addItem("P001", 1)})
     * @param <T>    az eredmény típusa
     * @return a művelet eredménye
     */
    public synchronized <T> T withCart(Function<Cart, T> action) {
        return action.apply(cart);
    }

    /** Kiüríti a kosarat (a tesztek előkészítő lépése is ezt használja). */
    public synchronized void clear() {
        cart.clear();
    }
}
