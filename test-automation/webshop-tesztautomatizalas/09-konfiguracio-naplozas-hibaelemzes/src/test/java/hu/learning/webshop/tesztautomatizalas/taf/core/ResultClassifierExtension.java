package hu.learning.webshop.tesztautomatizalas.taf.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JUnit 5 kiterjesztés, amely minden teszt eredményét a tananyag négy tesztállapotába sorolja
 * ({@link TestOutcome}), naplózza, és osztályonként összesítést ír.
 *
 * <p>Kimenetek:</p>
 * <ul>
 *   <li>naplósor tesztenként: {@code event=TEST_RESULT outcome=FAILED test=… cause=… lastHttp=…};</li>
 *   <li>összesítés tesztosztályonként: {@code Passed=5 Failed=0 TAS_error=0 Inconclusive=0};</li>
 *   <li>CSV sor tesztenként a {@code target/taf-results.csv} fájlba (trendelemzéshez, riportkészítő
 *       eszközhöz – VI. fejezet: jelentéskészítés és vizualizáció).</li>
 * </ul>
 *
 * <p>Használat: {@code @ExtendWith(ResultClassifierExtension.class)} a tesztosztályon.</p>
 */
public class ResultClassifierExtension implements TestWatcher, BeforeEachCallback, AfterAllCallback {

    /** A TAS eredménynaplója. */
    private static final Logger LOG = LoggerFactory.getLogger("TAS.RESULT");
    /** Teszt egyedi azonosítója → osztályozás (a saját tesztjeink is ebből olvasnak). */
    private static final Map<String, TestOutcome> RECORDED = new ConcurrentHashMap<>();
    /** Az eredményfájl helye; {@code -Dwebshop.resultsFile=...}-lal felülírható. */
    private static final Path RESULTS_FILE = Path.of(System.getProperty("webshop.resultsFile", "target/taf-results.csv"));

    /**
     * @return az eddig rögzített osztályozások (teszt egyedi azonosítója → állapot), csak olvasható nézet
     */
    public static Map<String, TestOutcome> recorded() {
        return Map.copyOf(RECORDED);
    }

    /**
     * Teszt előtt töröljük az előző teszt utolsó HTTP hívását, hogy bukáskor ne azt lássuk.
     *
     * @param context a teszt kontextusa
     */
    @Override
    public void beforeEach(ExtensionContext context) {
        StructuredLoggingFilter.clearLastExchange();
    }

    /** {@inheritDoc} */
    @Override
    public void testSuccessful(ExtensionContext context) {
        record(context, TestOutcome.PASSED, null);
    }

    /** {@inheritDoc} */
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        record(context, TestOutcome.classify(cause), cause);
    }

    /** {@inheritDoc} */
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        record(context, TestOutcome.INCONCLUSIVE, cause);
    }

    /**
     * A {@code @Disabled} teszteket nem soroljuk a négy állapotba, de naplózzuk az okot:
     * a kikapcsolt teszt is információ (pl. X. feladat: elavult teszt, javításra vár).
     */
    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        LOG.info("event=TEST_DISABLED test={} reason={}", context.getDisplayName(), reason.orElse("-"));
    }

    /**
     * Osztályonkénti összesítés a tesztosztály végén.
     *
     * @param context az osztály kontextusa
     */
    @Override
    public void afterAll(ExtensionContext context) {
        // A rögzített eredmények közül az ehhez az osztályhoz tartozókat számoljuk meg
        String classId = context.getUniqueId();
        Map<TestOutcome, Integer> counts = new EnumMap<>(TestOutcome.class);
        for (TestOutcome o : TestOutcome.values()) {
            counts.put(o, 0);
        }
        RECORDED.forEach((id, outcome) -> {
            if (id.startsWith(classId)) {
                counts.merge(outcome, 1, Integer::sum);
            }
        });
        LOG.info("event=TEST_SUMMARY class={} Passed={} Failed={} TAS_error={} Inconclusive={}",
                context.getDisplayName(), counts.get(TestOutcome.PASSED), counts.get(TestOutcome.FAILED),
                counts.get(TestOutcome.TAS_ERROR), counts.get(TestOutcome.INCONCLUSIVE));
    }

    /**
     * Egy teszt eredményének rögzítése: memória, napló, CSV.
     *
     * @param context a teszt kontextusa
     * @param outcome az osztályozás
     * @param cause   a kivétel (sikeres tesztnél {@code null})
     */
    private static void record(ExtensionContext context, TestOutcome outcome, Throwable cause) {
        RECORDED.put(context.getUniqueId(), outcome);
        String test = context.getRequiredTestClass().getSimpleName() + "." + context.getDisplayName();
        if (outcome == TestOutcome.PASSED) {
            LOG.info("event=TEST_RESULT outcome={} test={}", outcome, test);
        } else {
            // Bukáskor minden adat egy sorban, ami a TAS-vs-SUT elemzéshez kell
            LOG.warn("event=TEST_RESULT outcome={} test={} cause={}: {} lastHttp=[{}]", outcome, test,
                    cause.getClass().getSimpleName(), firstLine(cause.getMessage()),
                    Optional.ofNullable(StructuredLoggingFilter.lastExchange()).orElse("-"));
        }
        appendCsv(test, outcome);
    }

    /** CSV sor hozzáfűzése; a riportírás hibája nem buktathatja el a tesztet (csak figyelmeztetünk). */
    private static synchronized void appendCsv(String test, TestOutcome outcome) {
        try {
            Files.createDirectories(RESULTS_FILE.toAbsolutePath().getParent());
            boolean newFile = Files.notExists(RESULTS_FILE);
            String line = (newFile ? "timestamp;test;outcome\n" : "")
                    + Instant.now() + ";" + test.replace(';', ',') + ";" + outcome + "\n";
            Files.writeString(RESULTS_FILE, line, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOG.warn("event=REPORT_WRITE_FAILED file={} error={}", RESULTS_FILE, e.getMessage());
        }
    }

    /** Többsoros üzenet első sora (a napló egysoros maradjon). */
    private static String firstLine(String message) {
        if (message == null) {
            return "-";
        }
        int nl = message.indexOf('\n');
        return nl < 0 ? message : message.substring(0, nl);
    }
}
