package hu.learning.webshop.tesztautomatizalas.web;

import hu.learning.webshop.tesztautomatizalas.validation.UsernameValidator;
import hu.learning.webshop.tesztautomatizalas.validation.ValidationResult;
import hu.learning.webshop.tesztautomatizalas.web.dto.UsernameRequest;
import hu.learning.webshop.tesztautomatizalas.web.dto.UsernameValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * A regisztrációs űrlap felhasználónév-ellenőrző végpontja (X. feladat:
 * {@code POST /validate-username → {"valid": true}}).
 */
@RestController
public class UsernameController {

    /**
     * Auditnapló – a X. feladat SUT-oldali adatforrása:
     * {@code event=USERNAME_VALIDATED, input=admin12, result=success}.
     */
    private static final Logger AUDIT = LoggerFactory.getLogger("AUDIT");

    /** Az 01. lépésben TDD-vel készült validátor. */
    private final UsernameValidator validator;

    /**
     * @param validator a validátor bean
     */
    public UsernameController(UsernameValidator validator) {
        this.validator = validator;
    }

    /**
     * Ellenőrzi a felhasználónevet. Mindig 200-at ad: az érvénytelen név üzleti válasz, nem hiba.
     *
     * @param request a kérés törzse
     * @return {@code {"valid": true}} vagy {@code {"valid": false, "reason": "..."}}
     */
    @PostMapping("/validate-username")
    public UsernameValidationResponse validate(@RequestBody UsernameRequest request) {
        ValidationResult result = validator.validate(request.username());
        // Auditesemény a tananyag táblázatának formátumában. (Saját döntés: a bemenetet csak
        // biztonságos karakterek esetén írjuk ki, különben maszkoljuk – naplóinjekció ellen.)
        String input = request.username() != null && request.username().matches("[A-Za-z0-9]{0,64}")
                ? request.username() : "<maszkolva>";
        AUDIT.info("event=USERNAME_VALIDATED, input={}, result={}", input, result.valid() ? "success" : "rejected");
        return new UsernameValidationResponse(result.valid(), result.reason());
    }
}
