package hu.learning.webshop.tesztautomatizalas.api;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.config.LogConfig.logConfig;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Közös ősosztály a REST Assured API-tesztekhez.
 *
 * <p>A {@code RANDOM_PORT} miatt a Spring Boot <b>valódi HTTP szervert</b> indít egy szabad
 * porton – a tesztek így ugyanúgy, hálózaton át hívják a SUT-ot, mint egy külső kliens
 * (feketedobozos szemlélet). A port értékét a {@link LocalServerPort} adja.</p>
 *
 * <p><b>Specifikáció-újrahasznosítás:</b> a közös kérés- és válaszbeállításokat egyszer
 * írjuk meg ({@link RequestSpecification}, {@link ResponseSpecification}), a tesztek
 * pedig {@code .spec(...)}-cel használják – így nincs ismétlődő kód (Clean Code, IV. fejezet).</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractApiTest {

    /** A véletlen port, amin a SUT fut. */
    @LocalServerPort
    private int port;

    /** Közös kérés-specifikáció: alap URI, port, JSON tartalomtípus, BigDecimal számkezelés. */
    protected RequestSpecification api;

    /** Közös válasz-specifikáció sikeres JSON válaszhoz (200 + application/json). */
    protected ResponseSpecification okJson;

    /**
     * Minden teszt előtt felépíti a specifikációkat és kiüríti a kosarat (tesztizoláció).
     * A port csak a Spring kontextus indulása után ismert, ezért nem statikus.
     */
    @BeforeEach
    void setUpSpecs() {
        // 1. Kérés-specifikáció
        api = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(port)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(RestAssuredConfig.config()
                        // A JSON számokat BigDecimal-ként olvassuk: pénzösszegnél nincs lebegőpontos pontatlanság
                        .jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL))
                        // Csak akkor naplózzuk a teljes kérést és választ, ha egy ellenőrzés elbukik:
                        // sikeres futásnál csend van, hibánál viszont minden adat megvan az elemzéshez
                        .logConfig(logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)))
                .build();

        // 2. Válasz-specifikáció
        okJson = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();

        // 3. Előfeltétel: üres kosár (a SUT irányíthatóságát kihasználva, API-n keresztül)
        given().spec(api).when().delete("/cart").then().statusCode(204);
    }

    /**
     * Válasz-specifikáció ProblemDetail hibaválaszhoz.
     *
     * @param status    elvárt HTTP státusz
     * @param errorCode elvárt saját hibakód
     * @return a specifikáció
     */
    protected static ResponseSpecification problem(int status, String errorCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .expectContentType("application/problem+json")
                .expectBody("status", equalTo(status))
                .expectBody("errorCode", equalTo(errorCode))
                .build();
    }
}
