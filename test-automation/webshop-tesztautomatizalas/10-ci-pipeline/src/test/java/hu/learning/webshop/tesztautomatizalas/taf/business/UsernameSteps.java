package hu.learning.webshop.tesztautomatizalas.taf.business;

import hu.learning.webshop.tesztautomatizalas.taf.core.ApiClient;
import hu.learning.webshop.tesztautomatizalas.web.dto.UsernameRequest;
import hu.learning.webshop.tesztautomatizalas.web.dto.UsernameValidationResponse;
import io.restassured.response.Response;

/**
 * A regisztráció felhasználónév-ellenőrzésének lépései.
 *
 * <p>A III. feladat (TAF rétegezés a belépési funkcióhoz) megfelelője API-szinten: a tesztszkript
 * csak annyit mond, „ellenőrizd ezt a nevet”, a HTTP részletei ebben a rétegben vannak.</p>
 */
public class UsernameSteps {

    /** A core réteg HTTP kliense. */
    private final ApiClient api = ApiClient.getInstance();

    /**
     * @param username a vizsgált név
     * @return a SUT válasza (200 elvárt)
     */
    public UsernameValidationResponse validate(String username) {
        return tryValidate(username).then().statusCode(200).extract().as(UsernameValidationResponse.class);
    }

    /**
     * @param username a vizsgált név
     * @return {@code true}, ha a SUT elfogadja
     */
    public boolean isAccepted(String username) {
        return validate(username).valid();
    }

    /**
     * @param username a vizsgált név
     * @return a nyers válasz
     */
    public Response tryValidate(String username) {
        return api.post("/validate-username", new UsernameRequest(username));
    }
}
