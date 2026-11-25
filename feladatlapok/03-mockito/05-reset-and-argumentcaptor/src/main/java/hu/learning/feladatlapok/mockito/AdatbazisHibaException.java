package hu.learning.feladatlapok.mockito;

/**
 * A tárolási réteg (adatbázis) technikai hibáját jelzi, pl. megszakadt kapcsolat.
 * <p>
 * A {@link SzamlaRepository} megvalósításai dobhatják. Tesztben
 * {@code doThrow(...)}-val szimuláljuk, valódi adatbázis nélkül.
 */
public class AdatbazisHibaException extends RuntimeException {

    /**
     * Létrehozza a kivételt.
     *
     * @param uzenet a hiba leírása
     */
    public AdatbazisHibaException(String uzenet) {
        super(uzenet);
    }
}
