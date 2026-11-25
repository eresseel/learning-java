package com.example.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * Külső értesítési szolgáltatás HTTP kliense.
 *
 * Feladat: új Task létrehozásakor POST kérést küld egy külső notification API-nak.
 * Ez a komponens a WireMock tesztelés céltáblája.
 *
 * Technológia: Spring 6 RestClient (a régi RestTemplate modern utódja).
 *  - Fluent API
 *  - Szinkron, blokkoló (WebClient = aszinkron, reaktív)
 *  - Automatikus JSON szerializáció/deszializáció
 *
 * Hibakezelési stratégia:
 *  - 5xx hiba esetén: RestClientException dobódik → task létrehozás NEM szakad meg
 *    (a külső értesítés best-effort: ha nem sikerül, logolunk, de folytatjuk)
 *  - Timeout esetén: ResourceAccessException (RestClientException leszármazott)
 *  - Ez az "anti-fragile" minta: a függő szolgáltatás hibája ne terjedjen szét
 */
@Component
public class NotificationClient {

    private static final Logger log = LoggerFactory.getLogger(NotificationClient.class);

    private final RestClient restClient;

    /**
     * @Value inject: az alap URL konfigurálható (tesztben WireMock URL-re cseréljük).
     * Default: http://localhost:8081 (helyi fejlesztéshez)
     */
    public NotificationClient(
            RestClient.Builder restClientBuilder,
            @Value("${notification.service.url:http://localhost:8081}") String baseUrl) {

        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Értesítés küldése új task létrehozásakor.
     *
     * @param taskId  az újonnan létrehozott task azonosítója
     * @param title   a task neve (emberi olvashatóság kedvéért)
     * @return true ha sikerült, false ha a külső API hibát adott vissza
     */
    public boolean sendTaskCreatedNotification(Long taskId, String title) {
        try {
            // POST /notifications/tasks
            // body: { "taskId": 42, "event": "TASK_CREATED", "title": "Fix login bug" }
            restClient.post()
                    .uri("/notifications/tasks")
                    .body(Map.of(
                            "taskId", taskId,
                            "event", "TASK_CREATED",
                            "title", title
                    ))
                    .retrieve()
                    // onStatus: ha 5xx jön, ne dobjon restClientException automatikusan,
                    // hanem kezelhetőbb üzenettel dobjuk magunk
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new ExternalServiceException(
                                "Notification service error: HTTP " + response.getStatusCode());
                    })
                    .toBodilessEntity();  // nem kell a válasz body-ja

            log.info("Notification sent for task id={}", taskId);
            return true;

        } catch (ExternalServiceException e) {
            // Ismert, kezelt hiba: külső szerver hiba (500/503)
            log.warn("Notification failed for task id={}: {}", taskId, e.getMessage());
            return false;

        } catch (RestClientException e) {
            // Hálózati hiba, timeout, kapcsolat megtagadva stb.
            log.warn("Notification request failed for task id={}: {}", taskId, e.getMessage());
            return false;
        }
    }

    /**
     * Belső kivétel – csak ezen a csomagon belül használt,
     * a külső API hibáit reprezentálja.
     */
    public static class ExternalServiceException extends RuntimeException {
        public ExternalServiceException(String message) {
            super(message);
        }
    }
}
