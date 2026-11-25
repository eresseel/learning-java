package hu.learning.webshop.tesztautomatizalas.web;

import hu.learning.webshop.tesztautomatizalas.domain.InvalidQuantityException;
import hu.learning.webshop.tesztautomatizalas.domain.ItemNotInCartException;
import hu.learning.webshop.tesztautomatizalas.domain.UnknownProductException;
import hu.learning.webshop.tesztautomatizalas.domain.WebshopException;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Központi hibakezelő: a kivételeket egységes, RFC 9457 szerinti {@link ProblemDetail}
 * JSON-ná alakítja ({@code Content-Type: application/problem+json}).
 *
 * <p>Minden válaszba saját {@code errorCode} mezőt is teszünk: a tesztek (és a kliensek)
 * erre stabilabban építhetnek, mint a szabad szöveges {@code detail}-re.</p>
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Ismeretlen termék → 404 Not Found.
     *
     * @param ex a kivétel
     * @return a hibaválasz
     */
    @ExceptionHandler(UnknownProductException.class)
    public ProblemDetail unknownProduct(UnknownProductException ex) {
        return problem(HttpStatus.NOT_FOUND, "Ismeretlen termék", "UNKNOWN_PRODUCT", ex);
    }

    /**
     * Nem kosárban lévő tétel → 404 Not Found.
     *
     * @param ex a kivétel
     * @return a hibaválasz
     */
    @ExceptionHandler(ItemNotInCartException.class)
    public ProblemDetail itemNotInCart(ItemNotInCartException ex) {
        return problem(HttpStatus.NOT_FOUND, "A tétel nincs a kosárban", "ITEM_NOT_IN_CART", ex);
    }

    /**
     * Tartományon kívüli mennyiség → 400 Bad Request.
     *
     * @param ex a kivétel
     * @return a hibaválasz
     */
    @ExceptionHandler(InvalidQuantityException.class)
    public ProblemDetail invalidQuantity(InvalidQuantityException ex) {
        return problem(HttpStatus.BAD_REQUEST, "Érvénytelen mennyiség", "INVALID_QUANTITY", ex);
    }

    /**
     * A Bean Validation hibáit (pl. hiányzó {@code productId}) a mezőüzenetekkel együtt adjuk vissza.
     * A szülőosztály metódusát írjuk felül, hogy saját {@code errorCode}-ot is kapjon.
     *
     * @param ex      a validációs kivétel
     * @param headers válaszfejlécek
     * @param status  HTTP státusz (400)
     * @param request az aktuális kérés
     * @return a hibaválasz
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        // 1. A mezőhibákból olvasható szöveg: "productId: a productId kötelező; ..."
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining("; "));
        // 2. ProblemDetail összeállítása
        ProblemDetail body = ProblemDetail.forStatusAndDetail(status, detail);
        body.setTitle("Hibás kérés");
        body.setProperty("errorCode", "VALIDATION_ERROR");
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * Olvashatatlan kérés (hibás JSON, rossz típus, ismeretlen mező) → 400, saját hibakóddal.
     *
     * @param ex      a kivétel
     * @param headers válaszfejlécek
     * @param status  HTTP státusz (400)
     * @param request az aktuális kérés
     * @return a hibaválasz
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        // A belső kivételszöveget szándékosan nem adjuk ki (információszivárgás), csak általános üzenetet.
        ProblemDetail body = ProblemDetail.forStatusAndDetail(status, "A kérés törzse nem értelmezhető JSON.");
        body.setTitle("Hibás kérés");
        body.setProperty("errorCode", "MALFORMED_REQUEST");
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * Közös segédmetódus az üzleti hibák {@link ProblemDetail}-jéhez.
     *
     * @param status    HTTP státusz
     * @param title     rövid, emberi cím
     * @param errorCode géppel olvasható hibakód
     * @param ex        az üzleti kivétel (üzenete lesz a {@code detail})
     * @return a hibaválasz
     */
    private static ProblemDetail problem(HttpStatus status, String title, String errorCode, WebshopException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        pd.setTitle(title);
        pd.setProperty("errorCode", errorCode);
        return pd;
    }
}
