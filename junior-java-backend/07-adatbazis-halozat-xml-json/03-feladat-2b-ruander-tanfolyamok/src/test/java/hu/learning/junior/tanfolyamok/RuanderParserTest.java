package hu.learning.junior.tanfolyamok;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * A {@link RuanderParser} tesztje egy mentett mintaoldalon
 * (src/test/resources/ruander-minta.html) – a teszt nem függ az élő oldaltól.
 */
class RuanderParserTest {

    /**
     * @return a mintaoldal tartalma
     */
    private static String mintaOldal() throws IOException {
        try (InputStream be = RuanderParserTest.class.getResourceAsStream("/ruander-minta.html")) {
            assertNotNull(be, "Hiányzik a mintaoldal");
            return new String(be.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void mintaOldalFeldolgozasa() throws IOException {
        // Act
        List<Tanfolyam> lista = RuanderParser.feldolgoz(mintaOldal());
        // Assert: a duplikátum és az ár nélküli kártya kimarad, a sorrend megmarad,
        // akciós árnál az aktuális (nem az áthúzott) ár kerül be, az &amp; dekódolódik
        assertEquals(List.of(
                new Tanfolyam("Junior Java Backend fejlesztő tanfolyam", 690_000),
                new Tanfolyam("Junior szoftvertesztelő + automata tesztelő tanfolyam", 790_000),
                new Tanfolyam("SQL & MySQL alapok tanfolyam", 129_000),
                new Tanfolyam("Python programozó tanfolyam", 499_000)), lista);
    }

    @Test
    void bruttoArAZarojelbol() {
        assertEquals(119_000, RuanderParser.arKereses("40 óra / 93.701 Ft+áfa (119.000 Ft) / x"));
    }

    @Test
    void arNelkul() {
        assertNull(RuanderParser.arKereses("<span>Érdekel</span>"));
    }

    @Test
    void ezresTagolas() {
        assertEquals(1_280_000, RuanderParser.arSzam("1.280.000"));
    }

    @Test
    void uresOldal() {
        assertTrue(RuanderParser.feldolgoz("<html></html>").isEmpty());
    }
}
