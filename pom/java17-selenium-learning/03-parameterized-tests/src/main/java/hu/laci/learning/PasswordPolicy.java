package hu.laci.learning;

/**
 * Minimális jelszó-szabály a paraméterezett tesztek gyakorlásához.
 */
public class PasswordPolicy {

    /**
     * Ellenőrzi, hogy a jelszó eléri-e a minimális hosszt (8 karakter).
     *
     * @param password az ellenőrizendő jelszó (lehet null)
     * @return {@code true}, ha a jelszó nem null és legalább 8 karakter hosszú
     */
    public boolean isValid(String password) {
        // A null-ellenőrzés van elöl: a && rövidzárral kiértékelt, így a length() csak
        // akkor hívódik meg, ha a jelszó nem null.
        return password != null && password.length() >= 8;
    }
}
