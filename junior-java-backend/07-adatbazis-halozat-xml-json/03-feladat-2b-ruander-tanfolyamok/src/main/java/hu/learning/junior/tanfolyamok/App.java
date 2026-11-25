package hu.learning.junior.tanfolyamok;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Feladat II.B – a ruander.hu oldal tartalmának lekérése, a tanfolyamok
 * adatainak (megnevezés, ár) tárolása listában, majd kiírása.
 */
public class App {

    /** Alapértelmezett cím: a főoldal (itt a kártyákon ár is van). */
    static final String ALAP_URL = "https://www.ruander.hu/";

    /**
     * Belépési pont.
     *
     * @param args opcionálisan másik oldal címe, pl.
     *             {@code https://www.ruander.hu/programozo-tanfolyamok.html}
     */
    public static void main(String[] args) {
        String url = args.length > 0 ? args[0] : ALAP_URL;
        try {
            // 1. Az oldal letöltése.
            String html = letoltes(url);
            // 2. Feldolgozás: tanfolyamok listája.
            List<Tanfolyam> tanfolyamok = RuanderParser.feldolgoz(html);
            if (tanfolyamok.isEmpty()) {
                System.out.println("Nem találtam tanfolyamot – lehet, hogy megváltozott az oldal szerkezete.");
                return;
            }
            // 3. Kiírás ár szerint csökkenő sorrendben, magyar ezres tagolással.
            Locale magyar = Locale.of("hu", "HU");
            System.out.println(tanfolyamok.size() + " tanfolyam (" + url + "):");
            tanfolyamok.stream()
                    .sorted(Comparator.comparingInt(Tanfolyam::ar).reversed())
                    .forEach(t -> System.out.printf(magyar, "%,12d Ft  %s%n", t.ar(), t.megnevezes()));
        } catch (IOException e) {
            System.err.println("Nem sikerült letölteni az oldalt: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Letölt egy oldalt a JDK HttpClient-jével.
     *
     * @param url a cím
     * @return a HTML forrás
     * @throws IOException          hálózati hiba vagy nem 200-as válasz esetén
     * @throws InterruptedException ha a szálat megszakítják
     */
    static String letoltes(String url) throws IOException, InterruptedException {
        // A HttpClient AutoCloseable (Java 21+), ezért try-with-resources-szal zárjuk le.
        try (HttpClient http = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build()) {
            HttpRequest keres = HttpRequest.newBuilder(URI.create(url))
                    .header("User-Agent", "learning-java-demo/1.0 (oktatasi peldaprogram)")
                    .timeout(Duration.ofSeconds(20))
                    .build();
            HttpResponse<String> valasz = http.send(keres, HttpResponse.BodyHandlers.ofString());
            if (valasz.statusCode() != 200) {
                throw new IOException("HTTP " + valasz.statusCode());
            }
            return valasz.body();
        }
    }
}
