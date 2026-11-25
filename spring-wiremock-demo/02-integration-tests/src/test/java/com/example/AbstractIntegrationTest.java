package com.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Alap integrációs teszt osztály – minden IT teszt ebből örököl.
 *
 * Kulcs annotációk:
 *
 * @SpringBootTest(webEnvironment = RANDOM_PORT)
 *   - Teljes Spring context indul (minden bean)
 *   - Véletlenszerű portot kap → párhuzamos futtatás biztonságos
 *   - Valódi HTTP kérések mennek (nem MockMvc)
 *
 * @Testcontainers
 *   - JUnit 5 extension aktiválása: @Container mezők lifecycle-ját kezeli
 *
 * @Container + static
 *   - static: konténer az összes teszt osztály közt megosztott (gyorsabb)
 *   - nem static: minden teszt osztályhoz új konténer indul (izolált, lassabb)
 *
 * @ServiceConnection (Spring Boot 3.1+)
 *   - Automatikusan konfigurálja a DataSource URL-t, user-t, password-t
 *   - Nincs szükség @DynamicPropertySource-ra vagy @TestPropertySource-ra
 *
 * @ActiveProfiles("test")
 *   - application-test.yml betöltése: ddl-auto=create-drop
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    /**
     * PostgreSQL 16 konténer.
     * static → osztott konténer minden leszármazott tesztosztály közt.
     * Testcontainers automatikusan elindítja és leállítja.
     *
     * Kockázat: ha az egyik teszt adatot hagy maga után, hatással lehet
     * a következőre → ezért a tesztekben @Transactional + rollback, vagy
     * explicit cleanup (@AfterEach deleteAll) szükséges.
     */
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    /** Futó szerver portja – @BeforeEach állítja be a REST Assured base URI-t. */
    @LocalServerPort
    int port;

    /**
     * Minden teszt előtt beállítjuk a REST Assured alap URL-t.
     * Így a tesztekben elég: given().when().get("/api/tasks").then()...
     */
    @BeforeEach
    void setUpRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }
}
