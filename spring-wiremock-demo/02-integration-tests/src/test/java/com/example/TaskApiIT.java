package com.example;

import com.example.dto.TaskRequest;
import com.example.repository.TaskRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Task API integrációs tesztek – REST Assured + Testcontainers PostgreSQL.
 *
 * Tesztelési stratégia:
 *  - Teljes HTTP stack: kérés → controller → service → JPA → PostgreSQL konténer
 *  - Nem mockoljuk az adatbázist (H2 helyett valódi PG) → reális viselkedés
 *  - Minden teszt után cleanup: taskRepository.deleteAll()
 *
 * Nested osztályok:
 *  - Logikai csoportosítás végpontonként
 *  - Elkülönített @DisplayName leírások
 *  - Közös @AfterEach a cleanup-hoz az AbstractIntegrationTest-ből
 */
@DisplayName("Task API - Integrációs tesztek")
class TaskApiIT extends AbstractIntegrationTest {

    @Autowired
    TaskRepository taskRepository;

    /** Minden teszt után töröljük az adatokat → izolált tesztek */
    @AfterEach
    void cleanup() {
        taskRepository.deleteAll();
    }

    // =========================================================================
    // POST /api/tasks
    // =========================================================================

    @Nested
    @DisplayName("POST /api/tasks")
    class CreateTask {

        @Test
        @DisplayName("Érvényes kéréssel 201 Created-et ad vissza és Location headert")
        void createTask_validRequest_returns201() {
            // REST Assured DSL:
            // given() - előfeltételek (header, body, auth)
            // when()  - HTTP akció
            // then()  - assertek (Hamcrest matcher-ek)
            given()
                .contentType(ContentType.JSON)
                .body(new TaskRequest("Fix login bug", "Users cannot log in with OAuth"))
            .when()
                .post("/api/tasks")
            .then()
                .statusCode(201)
                // Location header: /api/tasks/{id} alakú URI
                .header("Location", matchesPattern(".*/api/tasks/\\d+"))
                // Response body mezők
                .body("id", notNullValue())
                .body("title", equalTo("Fix login bug"))
                .body("description", equalTo("Users cannot log in with OAuth"))
                .body("status", equalTo("TODO"))
                .body("createdAt", notNullValue());
        }

        @Test
        @DisplayName("Üres title esetén 400 Bad Request + violations map")
        void createTask_blankTitle_returns400() {
            given()
                .contentType(ContentType.JSON)
                .body(new TaskRequest("", null))  // üres title → @NotBlank hiba
            .when()
                .post("/api/tasks")
            .then()
                .statusCode(400)
                // ProblemDetail violations.title tartalmazza a hibát
                .body("violations.title", notNullValue());
        }

        @Test
        @DisplayName("Hiányzó JSON body esetén 400 Bad Request")
        void createTask_noBody_returns400() {
            given()
                .contentType(ContentType.JSON)
                // body szándékosan hiányzik
            .when()
                .post("/api/tasks")
            .then()
                .statusCode(400);
        }

        @Test
        @DisplayName("Túl hosszú title esetén 400 Bad Request")
        void createTask_tooLongTitle_returns400() {
            String longTitle = "A".repeat(256);  // max 255 karakter

            given()
                .contentType(ContentType.JSON)
                .body(new TaskRequest(longTitle, null))
            .when()
                .post("/api/tasks")
            .then()
                .statusCode(400)
                .body("violations.title", notNullValue());
        }
    }

    // =========================================================================
    // GET /api/tasks/{id}
    // =========================================================================

    @Nested
    @DisplayName("GET /api/tasks/{id}")
    class GetTaskById {

        @Test
        @DisplayName("Létező id esetén 200 OK és a task adatait adja vissza")
        void getTask_existingId_returns200() {
            // Előkészítés: task létrehozása és az id kinyerése
            int createdId = given()
                    .contentType(ContentType.JSON)
                    .body(new TaskRequest("Write tests", "Cover all edge cases"))
                .when()
                    .post("/api/tasks")
                .then()
                    .statusCode(201)
                    .extract()
                    .path("id");  // JSON path extraction

            // Az imént létrehozott task lekérdezése
            given()
            .when()
                .get("/api/tasks/{id}", createdId)
            .then()
                .statusCode(200)
                .body("id", equalTo(createdId))
                .body("title", equalTo("Write tests"))
                .body("status", equalTo("TODO"));
        }

        @Test
        @DisplayName("Nem létező id esetén 404 Not Found + ProblemDetail")
        void getTask_nonExistingId_returns404() {
            given()
            .when()
                .get("/api/tasks/{id}", 99999L)
            .then()
                .statusCode(404)
                // RFC 7807 ProblemDetail: "detail" mező tartalmazza a hibaüzenetet
                .body("detail", containsString("99999"));
        }
    }

    // =========================================================================
    // GET /api/tasks
    // =========================================================================

    @Nested
    @DisplayName("GET /api/tasks")
    class GetAllTasks {

        @Test
        @DisplayName("Üres adatbázis esetén üres listát ad vissza")
        void getAllTasks_empty_returnsEmptyList() {
            given()
            .when()
                .get("/api/tasks")
            .then()
                .statusCode(200)
                .body("", hasSize(0));  // üres JSON tömb
        }

        @Test
        @DisplayName("Két task létrehozása után kettőt ad vissza")
        void getAllTasks_twoTasks_returnsTwo() {
            // Két task létrehozása
            given().contentType(ContentType.JSON)
                   .body(new TaskRequest("Task 1", null))
                   .post("/api/tasks").then().statusCode(201);

            given().contentType(ContentType.JSON)
                   .body(new TaskRequest("Task 2", "Description"))
                   .post("/api/tasks").then().statusCode(201);

            given()
            .when()
                .get("/api/tasks")
            .then()
                .statusCode(200)
                .body("", hasSize(2))
                .body("title", hasItems("Task 1", "Task 2"));
        }
    }

    // =========================================================================
    // PUT /api/tasks/{id}
    // =========================================================================

    @Nested
    @DisplayName("PUT /api/tasks/{id}")
    class UpdateTask {

        @Test
        @DisplayName("Létező task frissítése 200 OK és frissített adatokat ad vissza")
        void updateTask_existingId_returns200() {
            int id = given()
                    .contentType(ContentType.JSON)
                    .body(new TaskRequest("Original title", null))
                .post("/api/tasks").then().statusCode(201)
                .extract().path("id");

            given()
                .contentType(ContentType.JSON)
                .body(new TaskRequest("Updated title", "Updated description"))
            .when()
                .put("/api/tasks/{id}", id)
            .then()
                .statusCode(200)
                .body("title", equalTo("Updated title"))
                .body("description", equalTo("Updated description"));
        }

        @Test
        @DisplayName("Nem létező task frissítése 404-et ad vissza")
        void updateTask_nonExistingId_returns404() {
            given()
                .contentType(ContentType.JSON)
                .body(new TaskRequest("New title", null))
            .when()
                .put("/api/tasks/{id}", 99999L)
            .then()
                .statusCode(404);
        }
    }

    // =========================================================================
    // DELETE /api/tasks/{id}
    // =========================================================================

    @Nested
    @DisplayName("DELETE /api/tasks/{id}")
    class DeleteTask {

        @Test
        @DisplayName("Létező task törlése 204 No Content-et ad vissza")
        void deleteTask_existingId_returns204() {
            int id = given()
                    .contentType(ContentType.JSON)
                    .body(new TaskRequest("To delete", null))
                .post("/api/tasks").then().statusCode(201)
                .extract().path("id");

            given().when().delete("/api/tasks/{id}", id)
                   .then().statusCode(204);

            // Ellenőrzés: a task valóban eltűnt
            given().when().get("/api/tasks/{id}", id)
                   .then().statusCode(404);
        }

        @Test
        @DisplayName("Nem létező task törlése 404-et ad vissza")
        void deleteTask_nonExistingId_returns404() {
            given().when().delete("/api/tasks/{id}", 99999L)
                   .then().statusCode(404);
        }
    }
}
