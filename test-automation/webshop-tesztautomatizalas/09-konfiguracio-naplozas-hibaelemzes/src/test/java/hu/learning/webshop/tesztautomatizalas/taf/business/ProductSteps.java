package hu.learning.webshop.tesztautomatizalas.taf.business;

import hu.learning.webshop.tesztautomatizalas.domain.Product;
import hu.learning.webshop.tesztautomatizalas.taf.core.ApiClient;
import io.restassured.response.Response;
import java.util.List;

/** A termékkeresés és -megtekintés üzleti lépései. */
public class ProductSteps {

    /** A core réteg HTTP kliense. */
    private final ApiClient api = ApiClient.getInstance();

    /**
     * Keresés a termékek között.
     *
     * @param query keresőszöveg ({@code null} = minden termék)
     * @return a találatok
     */
    public List<Product> search(String query) {
        return List.of(trySearch(query).then().statusCode(200).extract().as(Product[].class));
    }

    /**
     * Keresés ellenőrzés nélkül (pl. séma-validációhoz).
     *
     * @param query keresőszöveg ({@code null} = minden termék)
     * @return a nyers válasz
     */
    public Response trySearch(String query) {
        return query == null
                ? api.get("/products")
                : api.request().queryParam("q", query).get("/products");
    }

    /**
     * @param id termékazonosító
     * @return a termék (200 elvárt)
     */
    public Product get(String id) {
        return tryGet(id).then().statusCode(200).extract().as(Product.class);
    }

    /**
     * @param id termékazonosító
     * @return a nyers válasz
     */
    public Response tryGet(String id) {
        return api.get("/products/{id}", id);
    }
}
