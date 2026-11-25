package hu.learning.webshop.tesztautomatizalas.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A {@code POST /validate-username} válasza.
 *
 * <p>Érvényes névnél pontosan a tananyag X. feladatában naplózott választ adja:
 * {@code {"valid": true}} – a {@code null} értékű {@code reason} mező kimarad.</p>
 *
 * @param valid  érvényes-e a név
 * @param reason az elutasítás oka (csak érvénytelen névnél)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UsernameValidationResponse(boolean valid, String reason) {
}
