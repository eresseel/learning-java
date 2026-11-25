package hu.learning.webshop.tesztautomatizalas.taf.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.testkit.engine.EngineTestKit;

/**
 * A {@link ResultClassifierExtension} tesztje: egy szándékosan vegyes eredményű mintaosztályt
 * futtatunk le programozottan (JUnit Platform TestKit), majd megnézzük, jól osztályozott-e.
 *
 * <p>A mintaosztály statikus belső osztály: a Maven Surefire nem futtatja önállóan (a belső
 * osztályokat kihagyja), csak ez a teszt indítja el – így a szándékos bukások nem törik a buildet.</p>
 */
class ResultClassifierExtensionTest {

    /** Minta: mind a négy kimenet egy-egy példája. */
    @ExtendWith(ResultClassifierExtension.class)
    static class MixedOutcomes {

        /** Sikeres. */
        @Test
        void passes() {
            assertThat(1 + 1).isEqualTo(2);
        }

        /** Ellenőrzés nem teljesül → Failed. */
        @Test
        void assertionFails() {
            assertThat("admin12").as("régi szabály szerint érvénytelen").hasSizeGreaterThanOrEqualTo(8);
        }

        /** Kivétel a tesztkódban (pl. hiányzó tesztadat) → TAS error. */
        @Test
        void tasCodeThrows() {
            String missingTestData = null;
            missingTestData.length();
        }

        /** Előfeltétel nem teljesül → Inconclusive. */
        @Test
        void preconditionNotMet() {
            Assumptions.abort("a tesztkörnyezet nem elérhető");
        }
    }

    /** A négy teszt a négy különböző állapotba kerül. */
    @Test
    void classifiesAllFourOutcomes() {
        // Act – a mintaosztály futtatása a Jupiter motorral
        EngineTestKit.engine("junit-jupiter").selectors(selectClass(MixedOutcomes.class)).execute()
                .testEvents().assertStatistics(stats -> stats.started(4).succeeded(1).failed(2).aborted(1));

        // Assert – a kiterjesztés által rögzített osztályozás metódusnév szerint
        Map<String, TestOutcome> byMethod = ResultClassifierExtension.recorded().entrySet().stream()
                .filter(e -> e.getKey().contains("MixedOutcomes"))
                .collect(Collectors.toMap(e -> methodName(e.getKey()), Map.Entry::getValue));
        assertThat(byMethod).containsExactlyInAnyOrderEntriesOf(Map.of(
                "passes", TestOutcome.PASSED,
                "assertionFails", TestOutcome.FAILED,
                "tasCodeThrows", TestOutcome.TAS_ERROR,
                "preconditionNotMet", TestOutcome.INCONCLUSIVE));
    }

    /** A tiszta osztályozó függvény közvetlen tesztje. */
    @Test
    void classifyByThrowableType() {
        assertThat(TestOutcome.classify(null)).isEqualTo(TestOutcome.PASSED);
        assertThat(TestOutcome.classify(new AssertionError("x"))).isEqualTo(TestOutcome.FAILED);
        assertThat(TestOutcome.classify(new java.net.ConnectException("refused"))).isEqualTo(TestOutcome.TAS_ERROR);
        assertThat(TestOutcome.classify(new org.opentest4j.TestAbortedException("x"))).isEqualTo(TestOutcome.INCONCLUSIVE);
    }

    /** "[engine:junit-jupiter]/[class:...]/[method:passes()]" → "passes". */
    private static String methodName(String uniqueId) {
        int start = uniqueId.lastIndexOf("[method:") + "[method:".length();
        return uniqueId.substring(start, uniqueId.indexOf('(', start));
    }
}
