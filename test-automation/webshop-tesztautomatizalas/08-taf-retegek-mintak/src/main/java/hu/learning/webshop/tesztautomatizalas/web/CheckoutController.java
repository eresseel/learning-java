package hu.learning.webshop.tesztautomatizalas.web;

import hu.learning.webshop.tesztautomatizalas.service.CheckoutService;
import hu.learning.webshop.tesztautomatizalas.web.dto.CheckoutResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** A fizetés végpontja (I. feladat: „Fizetés PayPal integrációval”). */
@RestController
public class CheckoutController {

    /** A fizetési folyamat. */
    private final CheckoutService checkoutService;

    /**
     * @param checkoutService a fizetési szolgáltatás
     */
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    /**
     * {@code POST /checkout} – a kosár kifizetése. Sikernél 201 Created.
     *
     * @return a létrejött (kifizetett) rendelés
     */
    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public CheckoutResponse checkout() {
        return checkoutService.checkout();
    }
}
