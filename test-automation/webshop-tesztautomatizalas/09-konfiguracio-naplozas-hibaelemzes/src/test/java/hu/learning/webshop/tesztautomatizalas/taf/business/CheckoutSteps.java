package hu.learning.webshop.tesztautomatizalas.taf.business;

import hu.learning.webshop.tesztautomatizalas.taf.core.ApiClient;
import hu.learning.webshop.tesztautomatizalas.web.dto.CheckoutResponse;
import io.restassured.response.Response;

/** A fizetés üzleti lépései. */
public class CheckoutSteps {

    /** A core réteg HTTP kliense. */
    private final ApiClient api = ApiClient.getInstance();

    /**
     * @return a sikeres fizetés eredménye (201 elvárt)
     */
    public CheckoutResponse pay() {
        return tryPay().then().statusCode(201).extract().as(CheckoutResponse.class);
    }

    /**
     * @return a nyers válasz (negatív tesztekhez)
     */
    public Response tryPay() {
        return api.post("/checkout", null);
    }
}
