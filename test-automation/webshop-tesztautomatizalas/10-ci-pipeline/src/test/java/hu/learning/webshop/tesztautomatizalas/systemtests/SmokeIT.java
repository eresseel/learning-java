package hu.learning.webshop.tesztautomatizalas.systemtests;

import static org.assertj.core.api.Assertions.assertThat;

import hu.learning.webshop.tesztautomatizalas.domain.Product;
import hu.learning.webshop.tesztautomatizalas.taf.core.ApiClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Füsttesztek (smoke) közvetlenül a telepítés után: él-e a rendszer, működnek-e a fő funkciók.
 * Gyorsak és stabilak – ezek a telepítési minőségkapu első vonala.
 */
@DisplayName("Rendszerteszt – füstteszt a telepített SUT-on")
class SmokeIT extends AbstractSystemIT {

    /** A SUT egészséges. */
    @Test
    void healthIsUp() {
        assertThat(ApiClient.getInstance().get("/actuator/health").jsonPath().getString("status")).isEqualTo("UP");
    }

    /** A katalógus elérhető és kereshető. */
    @Test
    void productSearchWorks() {
        assertThat(shop.products().search("egér")).extracting(Product::id).contains("P001");
    }

    /** A regisztráció felhasználónév-ellenőrzése az aktuális szabállyal működik (X. feladat). */
    @Test
    void usernameValidationWorks() {
        assertThat(shop.usernames().isAccepted("admin12")).isTrue();
        assertThat(shop.usernames().isAccepted("adm")).isFalse();
    }
}
