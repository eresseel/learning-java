package hu.laci.learning;

/**
 * Egyszerű számológép osztály, amelyen a unit tesztelés alapjait gyakoroljuk.
 * <p>
 * Ez az "éles" (production) kód: a {@code src/main/java} mappában van, a tesztje pedig
 * a {@code src/test/java} mappában. A két mappa szétválasztása miatt a teszt kód
 * nem kerül bele az elkészült alkalmazásba.
 */
public class Calculator {

    /**
     * Összeadja a két egész számot.
     *
     * @param left  az első tag
     * @param right a második tag
     * @return a két szám összege
     */
    public int add(int left, int right) {
        // Egyetlen művelet, szándékosan egyszerű: a hangsúly a tesztelésen van, nem a logikán.
        return left + right;
    }
}
