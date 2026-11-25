package com.example;

import com.example.repository.TaskRepository;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.hamcrest.Matchers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * WireMock integrációs tesztek – külső notification API szimulálása.
 *
 * Amit tesztelünk:
 *  1. Sikeres értesítés (200 OK) → task létrejön, 201-et kap a kliens
 *  2. Külső 500 Internal Server Error → task MÉGIS létrejön (resilience)
 *  3. Külső 503 Service Unavailable → task MÉGIS létrejön
 *  4. Timeout (FixedDelayResponse) → task MÉGIS létrejön, de lassabb
 *
 * WireMock DSL magyarázat:
 *  stubFor(post(urlEqualTo("/..."))  → HTTP metódus + URL egyezés
 *    .willReturn(aResponse()         → milyen választ adjon
 *      .withStatus(200)              → HTTP státuszkód
 *      .withHeader("Content-Type", "application/json")
 *      .withBody("...")))            → response body
 *
 * verify():
 *  WireMock megjegyzi az összes beérkező kérést.
 *  verify(1, postRequestedFor(...)) → ellenőrzi hogy pontosan 1 POST ment el.
 */
@DisplayName("Notification Client - WireMock integrációs tesztek")
class NotificationClientIT extends AbstractWireMockIntegrationTest {

    @Autowired
    TaskRepository taskRepository;

    @AfterEach
    void cleanup() {
        taskRepository.deleteAll();
    }

    // =========================================================================
    // Sikeres értesítés
    // =========================================================================

    @Nested
    @DisplayName("Sikeres értesítés esetek")
    class SuccessfulNotification {

        @Test
        @DisplayName("Notification API 200-at ad → task létrejön, 201 visszatérés")
        void createTask_notificationSucceeds_returns201() {
            // --- ARRANGE: WireMock stub beállítása ---
            // A WireMock szerveren regisztrálunk egy "elvárást": ha POST /notifications/tasks
            // kérés jön, adjunk vissza 200-at.
            wireMockServer.stubFor(
                    post(urlEqualTo("/notifications/tasks"))
                            .willReturn(aResponse()
                                    .withStatus(200)
                                    .withHeader("Content-Type", "application/json")
                                    .withBody("{\"status\":\"delivered\"}"))
            );

            // --- ACT: task létrehozása a valódi API-n keresztül ---
            given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "title": "Implement WireMock",
                      "description": "Add WireMock to test external API calls"
                    }
                    """)
            .when()
                .post("/api/tasks")
            .then()
                // Task sikeresen létrejött
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", Matchers.equalTo("Implement WireMock"));

            // --- ASSERT: WireMock kapta-e a kérést? ---
            // Ellenőrizzük, hogy pontosan 1 POST kérés ment a WireMock-hoz
            wireMockServer.verify(1, postRequestedFor(urlEqualTo("/notifications/tasks")));
        }

        @Test
        @DisplayName("Notification API request body tartalmazza a task adatait")
        void createTask_notificationRequestBodyIsCorrect() {
            wireMockServer.stubFor(
                    post(urlEqualTo("/notifications/tasks"))
                            .withRequestBody(matchingJsonPath("$.taskId"))       // taskId mező létezik
                            .withRequestBody(matchingJsonPath("$.event",
                                    equalTo("TASK_CREATED")))                   // event = TASK_CREATED
                            .withRequestBody(matchingJsonPath("$.title",
                                    equalTo("Body Verification Test")))         // title egyezik
                            .willReturn(ok().withBody("{\"status\":\"ok\"}"))
            );

            given()
                .contentType(ContentType.JSON)
                .body("""
                    {"title": "Body Verification Test", "description": null}
                    """)
            .when()
                .post("/api/tasks")
            .then()
                .statusCode(201);

            // Ha a stub nem egyezik (pl. rossz body), WireMock 404-et adna vissza
            // → NotificationClient false-t adna → task MÉGIS létrejön, de ez jelzi a hibát
            wireMockServer.verify(1, postRequestedFor(urlEqualTo("/notifications/tasks")));
        }
    }

    // =========================================================================
    // 5xx hibák (resilience)
    // =========================================================================

    @Nested
    @DisplayName("5xx hiba – resilience tesztek")
    class ServerErrorResilience {

        @Test
        @DisplayName("Notification API 500-at ad → task MÉGIS létrejön (best-effort)")
        void createTask_notificationReturns500_taskStillCreated() {
            // --- ARRANGE: 500-at szimulálunk ---
            wireMockServer.stubFor(
                    post(urlEqualTo("/notifications/tasks"))
                            .willReturn(aResponse()
                                    .withStatus(500)
                                    .withHeader("Content-Type", "application/json")
                                    .withBody("""
                                        {
                                          "error": "Internal Server Error",
                                          "message": "Database connection failed"
                                        }
                                        """))
            );

            // --- ACT + ASSERT ---
            // Task létrehozása SIKERES kell legyen annak ellenére, hogy az értesítés 500-at ad
            given()
                .contentType(ContentType.JSON)
                .body("""
                    {"title": "Task with notification failure", "description": "Should still be created"}
                    """)
            .when()
                .post("/api/tasks")
            .then()
                // Kulcspont: 201 Created! Nem 500! A belső resilience logika elnyeli a hibát.
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", Matchers.equalTo("Task with notification failure"));

            // A WireMock valóban kapott egy kérést (az értesítés megpróbálkozott)
            wireMockServer.verify(1, postRequestedFor(urlEqualTo("/notifications/tasks")));
        }

        @Test
        @DisplayName("Notification API 503 Service Unavailable → task MÉGIS létrejön")
        void createTask_notificationReturns503_taskStillCreated() {
            wireMockServer.stubFor(
                    post(urlEqualTo("/notifications/tasks"))
                            .willReturn(aResponse()
                                    .withStatus(503)
                                    .withHeader("Content-Type", "application/json")
                                    .withBody("""
                                        {
                                          "error": "Service Unavailable",
                                          "message": "Notification service is temporarily down"
                                        }
                                        """))
            );

            given()
                .contentType(ContentType.JSON)
                .body("""
                    {"title": "Task during service outage", "description": null}
                    """)
            .when()
                .post("/api/tasks")
            .then()
                .statusCode(201)
                .body("title", Matchers.equalTo("Task during service outage"));

            wireMockServer.verify(1, postRequestedFor(urlEqualTo("/notifications/tasks")));
        }

        @Test
        @DisplayName("Visszatérő 500 hibák: több task is létrejön, mindegyik 201-et kap")
        void multipleTaskCreations_persistentNotificationFailure_allTasksCreated() {
            // Az összes kérésre 500-at ad
            wireMockServer.stubFor(
                    post(urlEqualTo("/notifications/tasks"))
                            .willReturn(serverError())  // WireMock shorthand 500-ra
            );

            // 3 task létrehozása – mindegyik sikeres kell legyen
            for (int i = 1; i <= 3; i++) {
                given()
                    .contentType(ContentType.JSON)
                    .body("{\"title\": \"Task " + i + "\", \"description\": null}")
                .when()
                    .post("/api/tasks")
                .then()
                    .statusCode(201)
                    .body("title", Matchers.equalTo("Task " + i));
            }

            // 3 értesítési kísérlet ment a WireMock-hoz
            wireMockServer.verify(3, postRequestedFor(urlEqualTo("/notifications/tasks")));
        }
    }

    // =========================================================================
    // Timeout szimulálás
    // =========================================================================

    @Nested
    @DisplayName("Timeout szimuláció")
    class TimeoutSimulation {

        /**
         * WireMock fixedDelay(): mesterséges késleltetés a válasz előtt.
         *
         * Fontos: a RestClient alapértelmezetten NINCS timeout konfigurálva,
         * ezért ez a teszt a válasz megvárásával sikerül (csak lassan fut).
         *
         * Production-ban RestClient.Builder-en kell timeout-ot beállítani:
         *   .requestFactory(factory -> factory.setConnectTimeout(1000))
         *
         * Ez a teszt demonstrálja, hogy a lassú külső API sem töri meg az alkalmazást.
         */
        @Test
        @DisplayName("Lassú notification API (2s delay) → task MÉGIS létrejön (alapértelmezett timeout)")
        void createTask_slowNotificationApi_taskCreatedEventually() {
            // 2 másodperces késleltetés a válasz előtt
            wireMockServer.stubFor(
                    post(urlEqualTo("/notifications/tasks"))
                            .willReturn(aResponse()
                                    .withStatus(200)
                                    .withFixedDelay(2000)  // 2000 ms = 2 másodperc
                                    .withBody("{\"status\":\"delivered\"}"))
            );

            // REST Assured timeout növelése 10 másodpercre hogy ne a tesztelő időzzön ki
            given()
                .contentType(ContentType.JSON)
                .body("""
                    {"title": "Task with slow notification", "description": "2s delay test"}
                    """)
            .when()
                .post("/api/tasks")
            .then()
                // A task létrejön – a kérés megvárja a lassú választ (timeout nincs beállítva)
                .statusCode(201)
                .body("title", Matchers.equalTo("Task with slow notification"))
                // Teljes kérés ideje > 2 másodperc (mivel a notification vár)
                .time(greaterThan(1900L));  // ms-ban, kicsit kevesebb mint 2s a biztonság miatt
        }
    }

    // =========================================================================
    // Kérés nélkül hívás – WireMock hiány szimulálása
    // =========================================================================

    @Nested
    @DisplayName("WireMock stub nélküli esetek")
    class NoStubConfigured {

        /**
         * Ha a WireMock-on nincs stub beállítva az adott URL-re,
         * WireMock 404-et ad vissza.
         * Ez is 5xx-nek számít a NotificationClient-ben? Nem – 4xx-et nem kezel külön.
         * RestClient alapján 4xx esetén ResponseErrorHandler dob kivételt,
         * amit RestClientException-ként kap el a catch blokk → false visszatérés.
         */
        @Test
        @DisplayName("WireMock stub nélkül (404 visszatér) → task MÉGIS létrejön")
        void createTask_noStubConfigured_taskStillCreated() {
            // Nincs stub → WireMock 404-et ad vissza
            given()
                .contentType(ContentType.JSON)
                .body("""
                    {"title": "Task without notification stub", "description": null}
                    """)
            .when()
                .post("/api/tasks")
            .then()
                // A task MÉGIS létrejön – a 404 értesítési hiba nem töri meg az alkalmazást
                .statusCode(201)
                .body("title", Matchers.equalTo("Task without notification stub"));
        }
    }
}
