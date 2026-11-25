package hu.learning.webshop.tesztautomatizalas.tests;

import static org.assertj.core.api.Assertions.assertThat;

import hu.learning.webshop.tesztautomatizalas.taf.core.ApiClient;
import hu.learning.webshop.tesztautomatizalas.web.dto.UsernameValidationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Felhasználónév-ellenőrzés – a III. feladat (TAF rétegezés a belépési/regisztrációs funkcióhoz)
 * tesztszkript rétege. Figyeld meg: egyetlen URL, HTTP metódus vagy JSON sincs a fájlban.
 */
@DisplayName("Tesztszkript – felhasználónév")
class UsernameTest extends AbstractShopTest {

    /** X. feladat: admin12 elfogadva, ok nélkül. */
    @Test
    void admin12IsAccepted() {
        UsernameValidationResponse result = shop.usernames().validate("admin12");
        assertThat(result.valid()).isTrue();
        assertThat(result.reason()).isNull();
    }

    /** Érvénytelen nevek elutasítva. */
    @ParameterizedTest(name = "\"{0}\" elutasítva")
    @ValueSource(strings = {"adm12", "admin_12", "admin 12"})
    void invalidNamesAreRejected(String username) {
        assertThat(shop.usernames().isAccepted(username)).isFalse();
    }

    /** Rossz HTTP metódus – ez technikai teszt, ezért itt a core réteget használjuk közvetlenül. */
    @Test
    void getIsNotAllowed() {
        assertThat(ApiClient.getInstance().get("/validate-username").statusCode()).isEqualTo(405);
    }
}
