package hu.learning.webshop.tesztautomatizalas.taf.business;

import java.util.function.Predicate;

/**
 * A felhasználónév-szabály <b>tesztorákulumai</b>, verzióval és forrással (teszteset-metaadat).
 *
 * <p>A X. feladat tanulsága: a teszt egy elavult szabályt várt, mert a tesztmetaadat nem követte
 * a követelmény változását. Ezért a szabály itt egy helyen, verzióval és forrásmegjelöléssel él;
 * a tesztek erre hivatkoznak, nem saját, beégetett elvárásra.</p>
 */
public enum UsernameRules {

    /** ELAVULT: „csak kisbetű és szám, min. 8 karakter” – a tesztmetaadatban maradt korábbi szabály. */
    V1_LOWERCASE_MIN8("1.0", "korábbi követelmény (elavult)", "[a-z0-9]{8,}"),

    /** AKTUÁLIS: „minimum 6 karakter, betű vagy szám” – a SUT konzolnaplója és a jelenlegi követelmény szerint. */
    V2_ALNUM_MIN6("2.0", "aktuális követelmény; SUT konzolnapló: 'minimum 6 karakter, betű vagy szám'", "[A-Za-z0-9]{6,}");

    /** Az éppen érvényes szabály – ha a követelmény változik, csak ezt a sort kell átírni. */
    public static final UsernameRules CURRENT = V2_ALNUM_MIN6;

    /** A szabály verziója. */
    private final String version;
    /** Honnan származik a szabály (nyomonkövethetőség). */
    private final String source;
    /** A szabály reguláris kifejezésként. */
    private final Predicate<String> rule;

    UsernameRules(String version, String source, String regex) {
        this.version = version;
        this.source = source;
        this.rule = s -> s != null && s.matches(regex);
    }

    /**
     * @param username a vizsgált név
     * @return érvényes-e a név e szabályverzió szerint
     */
    public boolean accepts(String username) {
        return rule.test(username);
    }

    /** @return a szabály verziója */
    public String version() {
        return version;
    }

    /** @return a szabály forrása */
    public String source() {
        return source;
    }
}
