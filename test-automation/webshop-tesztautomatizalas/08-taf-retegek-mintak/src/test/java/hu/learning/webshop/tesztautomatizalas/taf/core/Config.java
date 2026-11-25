package hu.learning.webshop.tesztautomatizalas.taf.core;

/**
 * A tesztkörnyezet konfigurációja: hol fut a SUT.
 *
 * <p>Ebben a lépésben csak a lokálisan (véletlen porton) indított SUT-ot kezeljük;
 * a 09. lépés bővíti környezeti változókkal és rendszer-property-kkel.</p>
 *
 * @param baseUri a SUT alap URI-ja séma és hoszt szerint (pl. {@code http://localhost})
 * @param port    a SUT portja
 */
public record Config(String baseUri, int port) {

    /**
     * A lokálisan, a teszt JVM-jében indított SUT konfigurációja.
     *
     * @param port a Spring Boot által választott véletlen port
     * @return a konfiguráció
     */
    public static Config local(int port) {
        return new Config("http://localhost", port);
    }

    /**
     * @return a teljes alap URL (pl. {@code http://localhost:54321})
     */
    public String baseUrl() {
        return baseUri + ":" + port;
    }
}
