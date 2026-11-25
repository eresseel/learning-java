package hu.learning.webshop.tesztautomatizalas.validation;

/**
 * Egy felhasználónév-ellenőrzés eredménye.
 *
 * <p>Nem csak egy {@code boolean}-t adunk vissza, hanem az elutasítás okát is:
 * így a teszt (és később a REST API) pontosan meg tudja mondani, <em>miért</em>
 * érvénytelen egy név. Ez a megfigyelhetőséget (tesztelhetőséget) javítja – lásd
 * a tananyag II. fejezetét (megfigyelhetőség, irányíthatóság).</p>
 *
 * @param valid  érvényes-e a név
 * @param reason az elutasítás oka magyarul; érvényes névnél {@code null}
 */
public record ValidationResult(boolean valid, String reason) {

    /**
     * Gyártó metódus az érvényes eredményhez.
     *
     * @return érvényes, ok nélküli eredmény
     */
    public static ValidationResult ok() {
        return new ValidationResult(true, null);
    }

    /**
     * Gyártó metódus az érvénytelen eredményhez.
     *
     * @param reason az elutasítás oka
     * @return érvénytelen eredmény a megadott okkal
     */
    public static ValidationResult invalid(String reason) {
        return new ValidationResult(false, reason);
    }
}
