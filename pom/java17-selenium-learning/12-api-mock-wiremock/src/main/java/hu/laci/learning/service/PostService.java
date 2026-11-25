package hu.laci.learning.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Egy kis szolgáltatás-osztály, amely egy KÜLSŐ HTTP API-t hív (a tesztelendő rendszer).
 * <p>
 * A teszteknél a külső API-t WireMock helyettesíti, így a teszt gyors, determinisztikus
 * és internetkapcsolat nélkül is fut. A hibákat (404, 500, időtúllépés) is ki tudjuk váltani,
 * amit egy valódi szolgáltatással nehéz lenne.
 */
public class PostService {

    private final String baseUrl;
    private final Duration timeout;
    // A Java 11 óta beépített HTTP kliens: külön függőség nélkül hív HTTP szolgáltatást.
    private final HttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * @param baseUrl a külső szolgáltatás alap URL-je (pl. http://localhost:8080)
     * @param timeout a kapcsolódási és a válaszra várakozási időkorlát
     */
    public PostService(String baseUrl, Duration timeout) {
        this.baseUrl = baseUrl;
        this.timeout = timeout;
        this.httpClient = HttpClient.newBuilder().connectTimeout(timeout).build();
    }

    /**
     * Lekéri egy bejegyzés címét.
     *
     * @param postId a bejegyzés azonosítója
     * @return a bejegyzés címe
     * @throws IllegalStateException ha a szolgáltatás nem 200-zal válaszol, a válasz hibás,
     *                               vagy a hívás hálózati hibával / időtúllépéssel végződik
     */
    public String fetchTitle(int postId) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/posts/" + postId))
                // A kérés saját időkorlátja: ennyi ideig várunk a válaszra.
                .timeout(timeout)
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Csak a 200-as választ fogadjuk el sikeresnek.
            if (response.statusCode() != 200) {
                throw new IllegalStateException("Unexpected status code: " + response.statusCode());
            }

            JsonNode title = mapper.readTree(response.body()).path("title");
            if (title.isMissingNode() || title.isNull()) {
                throw new IllegalStateException("The response has no title field");
            }
            return title.asText();
        } catch (IOException e) {
            // Ide tartozik a hálózati hiba, az időtúllépés (HttpTimeoutException) és a hibás JSON is.
            throw new IllegalStateException("Request failed: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            // Az interrupt jelzést vissza kell állítani, hogy a hívó lánc észlelhesse a megszakítást.
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Request was interrupted", e);
        }
    }
}
