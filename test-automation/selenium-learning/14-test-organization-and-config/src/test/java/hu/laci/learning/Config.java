package hu.laci.learning;

/**
 * Központi konfiguráció: a futtatás beállításait rendszer-tulajdonságokból (system property) olvassa.
 * <p>
 * A tulajdonságokat parancssorból adhatjuk meg, így a teszt kódját nem kell módosítani:
 * <pre>
 *   mvn test -Dheadless=true -Dwait.seconds=10
 * </pre>
 * A pom.xml a Maven tulajdonságokat átadja a tesztek JVM-jének (lásd a Surefire systemPropertyVariables részét),
 * és ott alapértékek is meg vannak adva.
 */
public final class Config {

    private Config() {
        // Segédosztály, nem példányosítjuk.
    }

    /**
     * @return {@code true}, ha a böngészőt ablak nélkül (headless) kell indítani.
     *         Alapérték: false. Beállítás: -Dheadless=true vagy -Pheadless
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", "false"));
    }

    /**
     * @return az explicit várakozások időkorlátja másodpercben. Alapérték: 5. Beállítás: -Dwait.seconds=10
     */
    public static int explicitWaitSeconds() {
        // A getProperty második paramétere az alapérték, ha a tulajdonság nincs megadva.
        return Integer.parseInt(System.getProperty("wait.seconds", "5"));
    }
}
