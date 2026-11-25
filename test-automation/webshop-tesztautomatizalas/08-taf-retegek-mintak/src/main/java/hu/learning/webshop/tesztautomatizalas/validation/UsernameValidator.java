package hu.learning.webshop.tesztautomatizalas.validation;

/**
 * A webáruház regisztrációs felhasználónév-szabályát megvalósító osztály.
 *
 * <p><b>Az AKTUÁLIS szabály</b> (a tananyag X. feladatának SUT konzolnaplója szerint:
 * „minimum 6 karakter, betű vagy szám”):</p>
 * <ul>
 *   <li>a név nem lehet {@code null} és nem lehet üres;</li>
 *   <li>legalább {@value #MIN_LENGTH} karakter hosszú;</li>
 *   <li>kizárólag betűt (angol ábécé, kis- és nagybetű) vagy számjegyet tartalmazhat.</li>
 * </ul>
 *
 * <p><b>A RÉGI (elavult) szabály</b> – ami a X. feladatban a teszteset metaadataiban maradt –
 * „csak kisbetű és szám, min. 8 karakter” volt. Ezt itt szándékosan NEM valósítjuk meg;
 * a 09. lépés mutatja be, mi történik, ha egy teszt még a régi szabályt várja.</p>
 *
 * <p>Saját döntés: az ékezetes betűket (pl. „á”) nem engedjük, mert a „betű” fogalmát a
 * tananyag nem pontosítja, és a felhasználónevek tipikusan ASCII-korlátosak. Felső hosszkorlát
 * nincs, mert a tananyag sem ír ilyet.</p>
 */
public class UsernameValidator {

    /** A felhasználónév minimális hossza az aktuális szabály szerint. */
    public static final int MIN_LENGTH = 6;

    /**
     * Ellenőrzi a felhasználónevet, és indoklással együtt adja vissza az eredményt.
     *
     * @param username az ellenőrizendő név (lehet {@code null} is)
     * @return az ellenőrzés eredménye; érvénytelen esetben magyar nyelvű okkal
     */
    public ValidationResult validate(String username) {
        // 1. Hiányzó vagy üres bemenet: ezt külön ágon kezeljük, hogy ne kapjunk NullPointerException-t.
        if (username == null || username.isEmpty()) {
            return ValidationResult.invalid("A felhasználónév megadása kötelező.");
        }
        // 2. Hosszellenőrzés – a határérték-elemzés (5/6/7) éppen ezt a feltételt célozza.
        if (username.length() < MIN_LENGTH) {
            return ValidationResult.invalid(
                    "A felhasználónév legalább " + MIN_LENGTH + " karakter hosszú legyen.");
        }
        // 3. Karakterkészlet-ellenőrzés: minden karakter betű (a-z, A-Z) vagy számjegy (0-9) legyen.
        for (char c : username.toCharArray()) {
            if (!isAsciiLetterOrDigit(c)) {
                return ValidationResult.invalid(
                        "A felhasználónév csak betűt vagy számot tartalmazhat (hibás karakter: '" + c + "').");
            }
        }
        // 4. Minden feltétel teljesült.
        return ValidationResult.ok();
    }

    /**
     * Rövidítés, ha csak az igen/nem válasz érdekel.
     *
     * @param username az ellenőrizendő név
     * @return {@code true}, ha a név az aktuális szabály szerint érvényes
     */
    public boolean isValid(String username) {
        return validate(username).valid();
    }

    /**
     * Megmondja, hogy a karakter angol betű vagy számjegy-e.
     *
     * <p>A {@link Character#isLetterOrDigit(char)}-ot szándékosan nem használjuk, mert az
     * az ékezetes és egyéb Unicode betűket is elfogadná.</p>
     *
     * @param c a vizsgált karakter
     * @return {@code true}, ha {@code [a-zA-Z0-9]}
     */
    private static boolean isAsciiLetterOrDigit(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');
    }
}
