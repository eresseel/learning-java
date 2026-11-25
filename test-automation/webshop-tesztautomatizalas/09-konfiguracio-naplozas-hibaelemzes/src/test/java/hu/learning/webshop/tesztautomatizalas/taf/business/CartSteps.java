package hu.learning.webshop.tesztautomatizalas.taf.business;

import hu.learning.webshop.tesztautomatizalas.taf.core.ApiClient;
import hu.learning.webshop.tesztautomatizalas.web.dto.AddCartItemRequest;
import hu.learning.webshop.tesztautomatizalas.web.dto.CartResponse;
import hu.learning.webshop.tesztautomatizalas.web.dto.UpdateQuantityRequest;
import io.restassured.response.Response;

/** A kosár üzleti lépései (VIII. feladat: kosárba helyezés, mennyiség frissítése). */
public class CartSteps {

    /** A core réteg HTTP kliense (Singleton). */
    private final ApiClient api = ApiClient.getInstance();

    /** Kiüríti a kosarat (előfeltétel-teremtés). */
    public void clear() {
        api.delete("/cart").then().statusCode(204);
    }

    /**
     * Terméket tesz a kosárba, és elvárja a sikert.
     *
     * @param productId termék
     * @param quantity  mennyiség
     * @return a kosár új állapota
     */
    public CartResponse add(String productId, int quantity) {
        return tryAdd(productId, quantity).then().statusCode(201).extract().as(CartResponse.class);
    }

    /**
     * Terméket próbál a kosárba tenni, ellenőrzés nélkül.
     *
     * @param productId termék
     * @param quantity  mennyiség
     * @return a nyers válasz
     */
    public Response tryAdd(String productId, int quantity) {
        return api.post("/cart/items", new AddCartItemRequest(productId, quantity));
    }

    /**
     * Felülírja egy tétel mennyiségét, és elvárja a sikert.
     *
     * @param productId termék
     * @param quantity  új mennyiség
     * @return a kosár új állapota
     */
    public CartResponse updateQuantity(String productId, int quantity) {
        return tryUpdateQuantity(productId, quantity).then().statusCode(200).extract().as(CartResponse.class);
    }

    /**
     * Mennyiség-módosítási kísérlet, ellenőrzés nélkül.
     *
     * @param productId termék
     * @param quantity  új mennyiség
     * @return a nyers válasz
     */
    public Response tryUpdateQuantity(String productId, int quantity) {
        return api.patch("/cart/items/{id}", new UpdateQuantityRequest(quantity), productId);
    }

    /**
     * Törli a tételt, és elvárja a sikert (204).
     *
     * @param productId termék
     */
    public void remove(String productId) {
        tryRemove(productId).then().statusCode(204);
    }

    /**
     * Törlési kísérlet, ellenőrzés nélkül.
     *
     * @param productId termék
     * @return a nyers válasz
     */
    public Response tryRemove(String productId) {
        return api.delete("/cart/items/{id}", productId);
    }

    /**
     * @return a kosár aktuális állapota
     */
    public CartResponse current() {
        return tryCurrent().then().statusCode(200).extract().as(CartResponse.class);
    }

    /**
     * @return a {@code GET /cart} nyers válasza (pl. séma-validációhoz)
     */
    public Response tryCurrent() {
        return api.get("/cart");
    }
}
