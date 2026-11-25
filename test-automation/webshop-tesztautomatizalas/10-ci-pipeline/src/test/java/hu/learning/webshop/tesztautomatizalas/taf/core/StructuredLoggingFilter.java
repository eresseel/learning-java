package hu.learning.webshop.tesztautomatizalas.taf.core;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST Assured szűrő: minden kéréshez korrelációs azonosítót ad, és strukturált naplósort ír
 * (TAS oldali naplózás – VI. fejezet: „tesztlépések, időzítések, hibák”).
 *
 * <p>Naplóformátum (kulcs=érték, egy sor kérésenként – gépileg kereshető, pl. grep-pel vagy
 * naplóelemzővel):</p>
 * <pre>event=HTTP_CALL method=POST path=/cart/items status=201 durationMs=12 correlationId=…</pre>
 *
 * <p>Hibás válasznál (≥ 400) vagy {@code webshop.logBodies=true} esetén a kérés és a válasz törzse
 * is a naplóba kerül. Az utolsó hívás összefoglalóját szálanként megőrizzük: az eredmény-osztályozó
 * ({@link ResultClassifierExtension}) bukáskor kiírja – így a hibajelentés tartalmazza a releváns
 * HTTP hívást és a korrelációs ID-t, amivel a SUT naplójában visszakereshető.</p>
 */
public class StructuredLoggingFilter implements Filter {

    /** A korrelációs azonosító fejléce (ugyanaz, amit a SUT {@code CorrelationIdFilter}-e vár). */
    public static final String CORRELATION_HEADER = "X-Correlation-Id";

    /** A TAS naplója. */
    private static final Logger LOG = LoggerFactory.getLogger("TAS.HTTP");
    /** Az utolsó HTTP hívás összefoglalója szálanként (párhuzamos futtatásnál sem keveredik). */
    private static final ThreadLocal<String> LAST_EXCHANGE = new ThreadLocal<>();

    /** Törzsek naplózása sikeres hívásnál is. */
    private final boolean logBodies;

    /**
     * @param logBodies sikeres hívásnál is naplózzuk-e a törzseket
     */
    public StructuredLoggingFilter(boolean logBodies) {
        this.logBodies = logBodies;
    }

    /**
     * @return az aktuális szál utolsó HTTP hívásának összefoglalója, vagy {@code null}
     */
    public static String lastExchange() {
        return LAST_EXCHANGE.get();
    }

    /** Törli az utolsó hívást (teszt elején, hogy ne egy korábbi teszt hívását lássuk). */
    public static void clearLastExchange() {
        LAST_EXCHANGE.remove();
    }

    /**
     * A kérés elküldése köré épülő naplózás.
     *
     * @param req a kérés
     * @param res a válasz-specifikáció
     * @param ctx a szűrőlánc
     * @return a válasz
     */
    @Override
    public Response filter(FilterableRequestSpecification req, FilterableResponseSpecification res, FilterContext ctx) {
        // 1. Korrelációs ID – ha a teszt nem adott meg sajátot
        String correlationId = req.getHeaders().getValue(CORRELATION_HEADER);
        if (correlationId == null) {
            correlationId = "tas-" + UUID.randomUUID();
            req.header(CORRELATION_HEADER, correlationId);
        }
        // 2. Időmérés és a kérés továbbengedése
        long start = System.nanoTime();
        Response response = ctx.next(req, res);
        long ms = (System.nanoTime() - start) / 1_000_000;
        // 3. Egysoros, strukturált összefoglaló
        String summary = String.format("event=HTTP_CALL method=%s path=%s status=%d durationMs=%d correlationId=%s",
                req.getMethod(), req.getDerivedPath(), response.statusCode(), ms, correlationId);
        LAST_EXCHANGE.set(summary);
        // 4. Hibánál (vagy kérésre) a törzsek is – a hibaelemzéshez nélkülözhetetlenek
        if (response.statusCode() >= 400 || logBodies) {
            LOG.info("{} requestBody={} responseBody={}", summary, abbreviate(req.getBody()), abbreviate(response.asString()));
        } else {
            LOG.info(summary);
        }
        return response;
    }

    /** Hosszú törzsek rövidítése, hogy a napló olvasható maradjon. */
    private static String abbreviate(Object body) {
        if (body == null) {
            return "-";
        }
        String s = body.toString().replaceAll("\\s+", " ");
        return s.length() > 500 ? s.substring(0, 500) + "…" : s;
    }
}
