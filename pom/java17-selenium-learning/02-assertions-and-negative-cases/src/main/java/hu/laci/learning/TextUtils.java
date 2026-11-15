package hu.laci.learning;

import java.util.Locale;

/**
 * Szövegkezelő segédmetódusok az assertion (ellenőrzés) gyakorlatokhoz.
 * <p>
 * Az osztály {@code final} és a konstruktora privát, mert csak statikus metódusokat
 * tartalmaz, és nem akarjuk, hogy példányosítsák vagy leszármaztassák.
 */
public final class TextUtils {

    private TextUtils() {
        // Segédosztályt (utility class) nem szabad példányosítani, ezért a konstruktor privát.
    }

    /**
     * Levágja a szöveg elejéről és végéről a szóközöket, majd kisbetűssé alakítja.
     *
     * @param value a normalizálandó szöveg (lehet null)
     * @return a normalizált szöveg, vagy {@code null}, ha a bemenet {@code null} volt
     */
    public static String normalize(String value) {
        // Védekezés a null ellen: null-on a trim() NullPointerException-t dobna.
        if (value == null) {
            return null;
        }
        // Locale.ROOT: nyelvfüggetlen kisbetűsítés, így pl. a török "I" betű sem okoz meglepetést.
        return value.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Visszaadja a bemenetet, ha tartalmaz nem-szóköz karaktert.
     *
     * @param value az ellenőrizendő szöveg
     * @return ugyanaz a szöveg, változtatás nélkül
     * @throws IllegalArgumentException ha az érték null vagy csak szóközökből áll
     */
    public static String requireNonBlank(String value) {
        // isBlank(): igaz, ha a szöveg üres vagy csak whitespace karaktereket tartalmaz.
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Value must not be blank");
        }
        return value;
    }
}
