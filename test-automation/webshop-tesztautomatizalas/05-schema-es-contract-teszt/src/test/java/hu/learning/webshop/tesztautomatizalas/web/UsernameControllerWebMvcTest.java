package hu.learning.webshop.tesztautomatizalas.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hu.learning.webshop.tesztautomatizalas.validation.UsernameValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * <b>Szelet (slice) teszt</b> – csak a webréteg indul el ({@code @WebMvcTest}), adatréteg és
 * szolgáltatások nélkül. Valódi HTTP szerver sincs: a {@link MockMvc} a DispatcherServlet-et
 * közvetlenül hívja. Gyors, a komponensintegrációs szintet képviseli (controller + JSON + validátor).
 */
@WebMvcTest(UsernameController.class)
@Import(UsernameValidator.class) // a @WebMvcTest nem tölti be a @Configuration bean-jeinket, ezért importáljuk
class UsernameControllerWebMvcTest {

    /** Spring által injektált MockMvc. */
    @Autowired
    private MockMvc mockMvc;

    /** A X. feladat SUT-naplójában szereplő hívás: admin12 → {"valid": true}. */
    @Test
    void admin12IsValid() throws Exception {
        mockMvc.perform(post("/validate-username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin12\"}"))
                // Assert – státusz és PONTOSAN a naplózott JSON (strict összehasonlítás: nincs reason mező)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valid\":true}", true));
    }

    /** Érvénytelen név: továbbra is 200, de valid=false és az ok szerepel. */
    @Test
    void tooShortNameIsInvalidWithReason() throws Exception {
        mockMvc.perform(post("/validate-username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"adm12\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.reason", containsString("legalább 6 karakter")));
    }

    /** Hibás JSON → 400 ProblemDetail, saját hibakóddal. */
    @Test
    void malformedJsonIsBadRequest() throws Exception {
        mockMvc.perform(post("/validate-username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.errorCode").value("MALFORMED_REQUEST"));
    }
}
