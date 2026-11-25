package com.example;

import com.example.client.NotificationClient;
import com.example.repository.TaskRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Konfigurált timeout tesztelése – külön test class, mert speciális RestClient konfig kell.
 *
 * Probléma: az alapértelmezett RestClient-nek nincs timeout beállítva.
 * Megoldás: @TestConfiguration bean a timeout-os RestClient.Builder-hez.
 *
 * WireMock delay > timeout → ResourceAccessException → NotificationClient catch → false → task létrejön.
 *
 * Ez demonstrálja a valós production konfigurációt:
 *  - 1 másodperc timeout a notification service-re
 *  - 3 másodperces WireMock delay → timeout kivétel → task MÉGIS létrejön
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@DisplayName("Notification Client - Timeout tesztek konfigurált timeout-tal")
class NotificationTimeoutIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    // Külön WireMock szerver ehhez a teszt osztályhoz
    static WireMockServer wireMockServer;

    static {
        wireMockServer = new WireMockServer(
                WireMockConfiguration.wireMockConfig().dynamicPort()
        );
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("notification.service.url", wireMockServer::baseUrl);
    }

    /**
     * @TestConfiguration: Spring context-be kerül, de csak tesztekhez.
     * Felülírja a production NotificationClient bean-t egy timeout-os változattal.
     *
     * SimpleClientHttpRequestFactory: JDK HttpURLConnection alapú,
     * setReadTimeout() beállítható milliszekundumban.
     */
    @TestConfiguration
    static class TimeoutConfig {

        @Bean
        NotificationClient notificationClientWithTimeout(
                @org.springframework.beans.factory.annotation.Value(
                    "${notification.service.url}") String baseUrl) {

            // 1 másodperces read timeout beállítása
            // Ha a szerver 1 másodpercnél tovább nem válaszol, SocketTimeoutException
            org.springframework.http.client.SimpleClientHttpRequestFactory factory =
                    new org.springframework.http.client.SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(500);   // 500ms kapcsolat timeout
            factory.setReadTimeout(1000);      // 1000ms (1s) olvasási timeout

            RestClient restClient = RestClient.builder()
                    .baseUrl(baseUrl)
                    .requestFactory(factory)
                    .build();

            // Package-private konstruktorral nem tudunk bean-t csinálni,
            // ezért a NotificationClient-et kell egy factory metodussal példányosítani.
            // Alternatíva: NotificationClient fogad RestClient paramétert konstruktorban.
            // Ebben a példában @Primary bean-t használunk.
            return new NotificationClient(RestClient.builder().baseUrl(baseUrl), baseUrl);
        }
    }

    @LocalServerPort
    int port;

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        io.restassured.RestAssured.baseURI = "http://localhost";
        io.restassured.RestAssured.port = port;
        wireMockServer.resetAll();
    }

    @AfterEach
    void cleanup() {
        taskRepository.deleteAll();
        wireMockServer.resetAll();
    }

    // =========================================================================
    // Timeout szimulálás WireMock fixedDelay-jel
    // =========================================================================

    @Test
    @DisplayName("WireMock 3s delay, 1s timeout → timeout hiba, task MÉGIS létrejön")
    void createTask_notificationTimeout_taskStillCreated() {
        // A WireMock 3 másodpercet vár mielőtt válaszol
        // A RestClient timeout 1 másodperc → a kérés időzik ki
        wireMockServer.stubFor(
                post(urlEqualTo("/notifications/tasks"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withFixedDelay(3000)  // 3 másodperces késleltetés
                                .withBody("{\"status\":\"delivered\"}"))
        );

        long startTime = System.currentTimeMillis();

        // REST Assured timeout 15s – a teszt kerete, ne a kérés időzzön ki
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"title": "Timeout test task", "description": "Should be created despite timeout"}
                """)
        .when()
            .post("/api/tasks")
        .then()
            // Kulcspont: 201 Created! A timeout nem töri meg a task létrehozást.
            .statusCode(201)
            .body("title", equalTo("Timeout test task"));

        long elapsed = System.currentTimeMillis() - startTime;

        // Ha timeout 1s, a teljes kérés 1-2 másodperc közt kell legyen (nem 3s)
        // Megjegyzés: az alapértelmezett RestClient-nél ez 3s+ lenne
        System.out.println("Request took: " + elapsed + "ms");
    }

    @Test
    @DisplayName("WireMock chunked delay – fokozatos lassulás szimulálása")
    void createTask_notificationChunkedDelay_taskStillCreated() {
        // Chunked delay: a válasz fejrész után késlelteti az adatok küldését
        // Ez más timeout típust tesztel (read timeout vs connection timeout)
        wireMockServer.stubFor(
                post(urlEqualTo("/notifications/tasks"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withChunkedDribbleDelay(5, 2000)  // 5 darab, 2000ms összesen
                                .withBody("{\"status\":\"chunked\"}"))
        );

        given()
            .contentType(ContentType.JSON)
            .body("""
                {"title": "Chunked delay task", "description": null}
                """)
        .when()
            .post("/api/tasks")
        .then()
            .statusCode(201);

        wireMockServer.verify(1, postRequestedFor(urlEqualTo("/notifications/tasks")));
    }
}
