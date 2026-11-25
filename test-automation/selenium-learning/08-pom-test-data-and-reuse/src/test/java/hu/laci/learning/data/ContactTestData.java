package hu.laci.learning.data;

/**
 * Az újrafelhasználható, nem érzékeny tesztadatok központi helye.
 * <p>
 * Az adatok elkülönítése a tesztlogikától azt jelenti, hogy egy adat módosítása
 * egyetlen helyen elvégezhető, és a tesztek olvashatóbbak maradnak.
 * Az osztály csak konstansokat tartalmaz, ezért final, és a konstruktora privát.
 */
public final class ContactTestData {

    private ContactTestData() {
        // Konstansokat tároló osztályt nem kell példányosítani.
    }

    // Első érvényes kapcsolattartó.
    public static final String VALID_NAME = "Laci";
    public static final String VALID_EMAIL = "laci@example.com";

    // Második érvényes kapcsolattartó: ugyanazt a viselkedést más adattal is ellenőrizzük.
    public static final String SECOND_NAME = "Alex";
    public static final String SECOND_EMAIL = "alex@example.com";
}
