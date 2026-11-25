package hu.learning.webshop.tesztautomatizalas.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Korrelációs azonosító és kérésnapló minden HTTP kéréshez (SUT oldali naplózás).
 *
 * <p>A tananyag VI. fejezete szerint a hibaelemzés egyik eszköze a <b>korrelációs ID</b>: a teszt
 * (TAS) minden kéréshez {@value #HEADER} fejlécet küld, a SUT ezt a saját naplójába írja és
 * visszaküldi. Így egy elbukott teszt naplósorához pontosan megtalálható a szerver oldali napló.</p>
 *
 * <p>Ha a kliens nem küld azonosítót, a szűrő generál egyet.</p>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter extends OncePerRequestFilter {

    /** A korrelációs azonosító fejléce. */
    public static final String HEADER = "X-Correlation-Id";
    /** Az MDC kulcs (a naplóminta {@code %X{correlationId}} része erre hivatkozik). */
    public static final String MDC_KEY = "correlationId";
    /** Csak biztonságos karakterek: a fejléc értéke a naplóba kerül (naplóinjekció ellen). */
    private static final Pattern SAFE_ID = Pattern.compile("[A-Za-z0-9._-]{1,64}");

    /** Kérésnapló. */
    private static final Logger LOG = LoggerFactory.getLogger(CorrelationIdFilter.class);

    /**
     * Beállítja a korrelációs azonosítót, továbbengedi a kérést, majd naplózza az eredményt.
     *
     * @param request  a kérés
     * @param response a válasz
     * @param chain    a szűrőlánc
     * @throws ServletException servlet hiba esetén
     * @throws IOException      I/O hiba esetén
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 1. Azonosító: a kliensé, ha érvényes; különben új
        String incoming = request.getHeader(HEADER);
        String correlationId = incoming != null && SAFE_ID.matcher(incoming).matches()
                ? incoming : UUID.randomUUID().toString();
        // 2. MDC: ettől kezdve a szál minden naplósorában szerepel
        MDC.put(MDC_KEY, correlationId);
        response.setHeader(HEADER, correlationId);
        long start = System.nanoTime();
        try {
            chain.doFilter(request, response);
        } finally {
            // 3. Strukturált (kulcs=érték) naplósor – gépileg is jól kereshető
            long ms = (System.nanoTime() - start) / 1_000_000;
            LOG.info("event=HTTP_REQUEST method={} path={} status={} durationMs={}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), ms);
            // 4. A szál visszakerül a poolba – az MDC-t takarítani kell
            MDC.remove(MDC_KEY);
        }
    }
}
