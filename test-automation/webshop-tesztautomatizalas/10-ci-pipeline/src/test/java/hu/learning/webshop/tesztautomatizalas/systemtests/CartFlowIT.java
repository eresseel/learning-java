package hu.learning.webshop.tesztautomatizalas.systemtests;

import static org.assertj.core.api.Assertions.assertThat;

import hu.learning.webshop.tesztautomatizalas.taf.core.Problem;
import hu.learning.webshop.tesztautomatizalas.web.dto.CartResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A VIII. feladat „Kosár” funkciója <b>rendszerszinten</b>, a telepített alkalmazáson:
 * ugyanaz az üzleti folyamat, mint a komponens- és komponensintegrációs szinten, de most a
 * valódi csomagolt jar-t, valódi konfigurációval, hálózaton át teszteljük.
 */
@DisplayName("Rendszerteszt – kosár folyamat a telepített SUT-on")
class CartFlowIT extends AbstractSystemIT {

    /** Hozzáadás → mennyiség frissítés → törlés. */
    @Test
    void addUpdateRemove() {
        shop.cart().add("P001", 2);
        CartResponse updated = shop.cart().updateQuantity("P001", 5);
        assertThat(updated.total()).isEqualByComparingTo("39950");

        shop.cart().remove("P001");
        assertThat(shop.cart().current().items()).isEmpty();
    }

    /** A hibakezelés a telepített rendszerben is a szerződés szerinti. */
    @Test
    void invalidQuantityIsRejected() {
        Problem problem = shop.cart().tryAdd("P001", 100).then().statusCode(400).extract().as(Problem.class);
        assertThat(problem.errorCode()).isEqualTo("INVALID_QUANTITY");
    }
}
