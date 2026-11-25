package hu.learning.webshop.tesztautomatizalas.tests;

import static org.assertj.core.api.Assertions.assertThat;

import hu.learning.webshop.tesztautomatizalas.taf.business.UsernameRules;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * <b>X. feladat</b> – a felhasználónév-teszt hibaelemzése (TAS vagy SUT?).
 *
 * <p>Az eredeti teszt („Step 2: Assertion failed – "admin12" should be invalid”) a <em>régi</em>
 * szabály szerint írt elvárással futott. A SUT minden adatforrása (szervernapló, API válasz,
 * auditnapló, frontend konzol) egybehangzóan az <em>új</em> szabályt mutatja → a hiba a TAS-ban
 * (elavult tesztorákulum / teszteset-metaadat) van, nem a SUT-ban. Részletes elemzés: README.</p>
 */
@DisplayName("X. feladat – felhasználónév-szabály regresszió")
@Tag("feladat-x")
class UsernameRuleRegressionTest extends AbstractShopTest {

    /**
     * Az EREDETI, hibás teszt – megőrizzük, de kikapcsolva, hogy a build zöld maradjon.
     *
     * <p>Kipróbálás (szándékosan elbukik, a naplóban {@code outcome=FAILED} és a releváns HTTP hívás):</p>
     * <pre>mvn test -Dtest=UsernameRuleRegressionTest \
     *   -Djunit.jupiter.conditions.deactivate=org.junit.*DisabledCondition</pre>
     */
    @Test
    @Disabled("X. feladat: ELAVULT tesztorákulum – a régi (v1.0: kisbetű+szám, min. 8) szabályt várja. "
            + "Elemzés szerint TAS-hiba (hamis pozitív), nem SUT-hiba. Javított változat: admin12IsAcceptedByCurrentRule.")
    void admin12ShouldBeInvalid_OLD_RULE() {
        // Step 1: név beküldése
        boolean accepted = shop.usernames().isAccepted("admin12");
        // Step 2: a RÉGI elvárás – ez bukik el: "admin12" should be invalid
        assertThat(accepted).as("\"admin12\" should be invalid").isFalse();
    }

    /** A JAVÍTOTT teszt: az elvárás az aktuális, verziózott szabályból jön. */
    @Test
    void admin12IsAcceptedByCurrentRule() {
        // Arrange – az orákulum az aktuális szabály (v2.0)
        String input = "admin12";
        boolean expected = UsernameRules.CURRENT.accepts(input);
        // Act
        boolean actual = shop.usernames().isAccepted(input);
        // Assert – a SUT és az orákulum egyezik (és az elvárás valóban "érvényes")
        assertThat(expected).isTrue();
        assertThat(actual).as("SUT vs. szabály v%s", UsernameRules.CURRENT.version()).isEqualTo(expected);
    }

    /**
     * Regressziós háló: azok a bemenetek, ahol a régi és az új szabály ELTÉR – pont ezek fedik fel,
     * ha valamelyik oldal (SUT vagy TAS) a rossz verziót követi.
     *
     * @param input a vizsgált név
     */
    @ParameterizedTest(name = "\"{0}\": SUT == szabály v2.0")
    @ValueSource(strings = {"admin12", "abcdef", "Admin123", "ABCDEFGH", "abc_defgh", "abc12"})
    void sutFollowsCurrentRuleWhereRulesDiffer(String input) {
        assertThat(shop.usernames().isAccepted(input))
                .as("\"%s\" – v1.0 szerint: %s, v2.0 szerint: %s", input,
                        UsernameRules.V1_LOWERCASE_MIN8.accepts(input), UsernameRules.CURRENT.accepts(input))
                .isEqualTo(UsernameRules.CURRENT.accepts(input));
    }
}
