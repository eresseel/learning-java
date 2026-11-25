package hu.learning.webshop.tesztautomatizalas.web;

import hu.learning.webshop.tesztautomatizalas.validation.UsernameValidator;
import hu.learning.webshop.tesztautomatizalas.validation.ValidationResult;
import hu.learning.webshop.tesztautomatizalas.web.dto.UsernameRequest;
import hu.learning.webshop.tesztautomatizalas.web.dto.UsernameValidationResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * A regisztrációs űrlap felhasználónév-ellenőrző végpontja (X. feladat:
 * {@code POST /validate-username → {"valid": true}}).
 */
@RestController
public class UsernameController {

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
        return new UsernameValidationResponse(result.valid(), result.reason());
    }
}
