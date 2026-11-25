package hu.learning.webshop.tesztautomatizalas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A webáruház backend (a tesztelt rendszer, SUT) belépési pontja.
 *
 * <p>A {@code @SpringBootApplication} bekapcsolja az automatikus konfigurációt és a
 * komponens-keresést ebben a csomagban és alcsomagjaiban.</p>
 */
@SpringBootApplication
public class WebshopApplication {

    /**
     * Elindítja a beágyazott webszervert.
     *
     * @param args parancssori argumentumok (pl. {@code --server.port=9090})
     */
    public static void main(String[] args) {
        SpringApplication.run(WebshopApplication.class, args);
    }
}
