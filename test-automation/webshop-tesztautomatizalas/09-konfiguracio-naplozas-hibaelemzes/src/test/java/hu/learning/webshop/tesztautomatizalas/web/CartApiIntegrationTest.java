package hu.learning.webshop.tesztautomatizalas.web;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hu.learning.webshop.tesztautomatizalas.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * <b>Integrációs teszt</b> – a teljes Spring kontextus elindul ({@code @SpringBootTest}),
 * de HTTP szerver nélkül (MockMvc). A controller → service → domain → katalógus lánc
 * együttműködését ellenőrzi: ez a VIII. feladat komponensintegrációs szintje.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CartApiIntegrationTest {

    /** HTTP-szerű hívások a DispatcherServlet felé. */
    @Autowired
    private MockMvc mockMvc;

    /** A közös kosár – közvetlenül ürítjük, hogy a tesztek függetlenek legyenek. */
    @Autowired
    private CartService cartService;

    /** Tesztizoláció: minden teszt üres kosárral indul (megbízható előfeltétel). */
    @BeforeEach
    void emptyCart() {
        cartService.clear();
    }

    /** Teljes boldog út: hozzáadás → módosítás → lekérdezés → törlés. */
    @Test
    void addUpdateGetRemoveFlow() throws Exception {
        // 1. Hozzáadás: 201 Created, a válasz a kosár új állapota
        mockMvc.perform(post("/cart/items").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":\"P001\",\"quantity\":2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.total").value(15980.00));

        // 2. Mennyiség felülírása 5-re
        mockMvc.perform(patch("/cart/items/P001").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].quantity").value(5))
                .andExpect(jsonPath("$.total").value(39950.00));

        // 3. Lekérdezés: a GET ugyanazt az állapotot mutatja
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemCount").value(5))
                .andExpect(jsonPath("$.currency").value("HUF"));

        // 4. Törlés: 204, utána üres kosár
        mockMvc.perform(delete("/cart/items/P001")).andExpect(status().isNoContent());
        mockMvc.perform(get("/cart")).andExpect(jsonPath("$.items", hasSize(0)));
    }

    /** Ismeretlen termék → 404 ProblemDetail. */
    @Test
    void unknownProductIs404Problem() throws Exception {
        mockMvc.perform(post("/cart/items").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":\"P999\",\"quantity\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.errorCode").value("UNKNOWN_PRODUCT"))
                .andExpect(jsonPath("$.detail").value("Ismeretlen termék: P999"));
    }

    /** Tartományon kívüli mennyiség → 400, és a kosár nem változik. */
    @Test
    void invalidQuantityIs400() throws Exception {
        mockMvc.perform(post("/cart/items").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":\"P001\",\"quantity\":100}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_QUANTITY"));
        mockMvc.perform(get("/cart")).andExpect(jsonPath("$.items", hasSize(0)));
    }

    /** Hiányzó kötelező mező → 400 VALIDATION_ERROR a mező nevével. */
    @Test
    void missingFieldIsValidationError() throws Exception {
        mockMvc.perform(post("/cart/items").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.detail").value("productId: a productId kötelező"));
    }

    /** Nem kosárban lévő tétel módosítása → 404 ITEM_NOT_IN_CART. */
    @Test
    void patchMissingItemIs404() throws Exception {
        mockMvc.perform(patch("/cart/items/P002").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":3}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ITEM_NOT_IN_CART"));
    }

    /** Termékkeresés ?q= paraméterrel, és ismeretlen termék lekérdezése. */
    @Test
    void productSearchAndLookup() throws Exception {
        mockMvc.perform(get("/products").param("q", "egér"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("P001"));
        mockMvc.perform(get("/products")).andExpect(jsonPath("$", hasSize(6)));
        mockMvc.perform(get("/products/P404")).andExpect(status().isNotFound());
    }
}
