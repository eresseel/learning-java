package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Központi kivételkezelő – @RestControllerAdvice minden controllerre érvényes.
 *
 * RFC 7807 ProblemDetail választ ad vissza (Spring 6+ natív támogatás):
 *  - TaskNotFoundException    → 404 Not Found
 *  - Validation hiba          → 400 Bad Request + mezőhibák map-je
 *  - Hiányzó / hibás JSON body → 400 Bad Request
 *  - Általános Exception      → 500 Internal Server Error
 *
 * ProblemDetail előnye: szabványos JSON struktúra, bővíthető egyedi property-kkel.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Task nem található – 404.
     * ProblemDetail.forStatusAndDetail: type automatikusan about:blank lesz.
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ProblemDetail handleTaskNotFound(TaskNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * @Valid validáció hiba – 400.
     * Mezőnév → hibaüzenet map a "violations" property-ben.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Validation failed");

        // Minden mező hibáját összegyűjtjük: { "title": "must not be blank", ... }
        Map<String, String> violations = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "invalid",
                        (a, b) -> a + "; " + b   // több hiba ugyanazon mezőn → összefűzés
                ));

        problem.setProperty("violations", violations);
        return problem;
    }

    /**
     * Hiányzó vagy nem értelmezhető kérés törzs (pl. üres body, hibás JSON) – 400.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Malformed or missing request body");
    }

    /**
     * Általános nem kezelt hiba – 500.
     * Production-ban ne adjunk belső részleteket vissza!
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(Exception ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }
}
