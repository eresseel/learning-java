package hu.learning.webshop.tesztautomatizalas.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** A {@code POST /validate-username} végpont API-tesztjei (X. feladat). */
@DisplayName("API – felhasználónév-validáció")
class UsernameApiTest extends AbstractApiTest {

    /** A X. feladat SUT API-naplója: admin12 → {"valid": true} – pontosan ennyi mező. */
    @Test
    void admin12IsValid() {
        given().spec(api)
                .body(Map.of("username", "admin12"))        // Map → JSON szerializáció
        .when()
                .post("/validate-username")
        .then()
                .spec(okJson)
                .body("valid", equalTo(true))
                .body("$", aMapWithSize(1));               // nincs "reason" mező
    }

    /** Érvénytelen nevek: 200, valid=false, és van ok. */
    @ParameterizedTest(name = "\"{0}\" érvénytelen")
    @ValueSource(strings = {"adm12", "admin_12", "admin 12"})
    void invalidNames(String username) {
        given().spec(api).body(Map.of("username", username))
        .when().post("/validate-username")
        .then().spec(okJson)
                .body("valid", equalTo(false))
                .body("reason", containsString("A felhasználónév"));
    }

    /** Rossz HTTP metódus → 405 Method Not Allowed (a Spring alapértelmezett ProblemDetail-je). */
    @Test
    void getIsNotAllowed() {
        given().spec(api).when().get("/validate-username").then().statusCode(405);
    }
}
