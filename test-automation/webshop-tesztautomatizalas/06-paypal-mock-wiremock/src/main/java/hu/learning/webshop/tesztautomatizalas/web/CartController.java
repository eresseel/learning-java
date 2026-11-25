package hu.learning.webshop.tesztautomatizalas.web;

import hu.learning.webshop.tesztautomatizalas.service.CartService;
import hu.learning.webshop.tesztautomatizalas.web.dto.AddCartItemRequest;
import hu.learning.webshop.tesztautomatizalas.web.dto.CartResponse;
import hu.learning.webshop.tesztautomatizalas.web.dto.UpdateQuantityRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * A kosár REST végpontjai (VIII. feladat: hozzáadás, mennyiség frissítése).
 *
 * <p>Minden módosító végpont a kosár <em>új állapotát</em> adja vissza, így a kliensnek
 * (és a tesztnek) nem kell külön {@code GET /cart}-ot hívnia.</p>
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    /** A kosár szolgáltatás. */
    private final CartService cartService;

    /**
     * @param cartService a kosár szolgáltatás bean
     */
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * {@code GET /cart} – a kosár aktuális állapota.
     *
     * @return a kosár
     */
    @GetMapping
    public CartResponse get() {
        return cartService.withCart(CartResponse::from);
    }

    /**
     * {@code POST /cart/items} – termék kosárba helyezése. Sikernél 201 Created.
     *
     * @param request termékazonosító és mennyiség ({@code @Valid}: formai ellenőrzés → 400)
     * @return a kosár új állapota
     */
    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse addItem(@Valid @RequestBody AddCartItemRequest request) {
        // A módosítás és a válasz összeállítása egy zárolt blokkban történik → konzisztens állapot
        return cartService.withCart(cart -> {
            cart.addItem(request.productId(), request.quantity());
            return CartResponse.from(cart);
        });
    }

    /**
     * {@code PATCH /cart/items/{productId}} – mennyiség felülírása.
     *
     * @param productId a tétel (termék) azonosítója
     * @param request   az új mennyiség
     * @return a kosár új állapota
     */
    @PatchMapping("/items/{productId}")
    public CartResponse updateQuantity(@PathVariable String productId,
                                       @Valid @RequestBody UpdateQuantityRequest request) {
        return cartService.withCart(cart -> {
            cart.updateQuantity(productId, request.quantity());
            return CartResponse.from(cart);
        });
    }

    /**
     * {@code DELETE /cart/items/{productId}} – tétel törlése. Sikernél 204 No Content.
     *
     * @param productId a törlendő tétel azonosítója
     */
    @DeleteMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable String productId) {
        cartService.withCart(cart -> {
            cart.removeItem(productId);
            return null;
        });
    }

    /** {@code DELETE /cart} – a kosár ürítése (204). A tesztek előkészítő lépése is ezt használja. */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clear() {
        cartService.clear();
    }
}
