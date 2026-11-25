package com.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Alap teszt osztály WireMock + Testcontainers infrastruktúrával.
 *
 * Architektúra:
 *
 *   [REST Assured] → [Spring Boot app:RANDOM_PORT]
 *                           ↓
 *                   [PostgreSQL konténer]  (Testcontainers)
 *                           ↓
 *                   [WireMock szerver]     (külső notification API szimulálása)
 *
 * WireMock lifecycle:
 *  - static WireMockServer: az összes teszt osztály közt megosztott (gyorsabb startup)
 *  - @BeforeEach: resetAllRequests() – előző stub-ok törlése
 *  - @AfterEach: resetAllRequests() – biztonsági cleanup
 *
 * @DynamicPropertySource:
 *  - Spring context indulása előtt fut
 *  - Beállítja notification.service.url-t a WireMock szerver dinamikus portjára
 *  - Ezzel a NotificationClient a WireMock-hoz küld, nem a valódi API-hoz
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public abstract class AbstractWireMockIntegrationTest {

    // --- Testcontainers: PostgreSQL ---

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    // --- WireMock szerver ---

    /**
     * WireMockServer statikusan – közös az összes leszármazott tesztosztály közt.
     * dynamicPort(): OS választja a portot → nincs portütközés párhuzamos futtatásban.
     *
     * Inicializálás: statikus initializer blokk, mert a @DynamicPropertySource
     * static metódusból hívódik meg, és a WireMock-nak már futnia kell ott.
     */
    static WireMockServer wireMockServer;

    static {
        wireMockServer = new WireMockServer(
                WireMockConfiguration.wireMockConfig().dynamicPort()
        );
        wireMockServer.start();
    }

    /**
     * @DynamicPropertySource: Spring context indulásakor fut.
     * Beállítja a notification.service.url property-t a WireMock URL-jére.
     * Így a NotificationClient automatikusan a WireMock-hoz küld kéréseket.
     *
     * Fontos: statikus és a Spring context inicializálása előtt fut le.
     */
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // WireMock URL: http://localhost:{dinamikus-port}
        registry.add("notification.service.url", wireMockServer::baseUrl);
    }

    // --- Port és REST Assured konfiguráció ---

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        // REST Assured base URL beállítása
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        // WireMock stub-ok törlése minden teszt előtt → izolált tesztek
        wireMockServer.resetAll();
    }

    @AfterEach
    void tearDown() {
        // Extra biztonság: teszt után is töröljük a stub-okat
        wireMockServer.resetAll();
    }
}
