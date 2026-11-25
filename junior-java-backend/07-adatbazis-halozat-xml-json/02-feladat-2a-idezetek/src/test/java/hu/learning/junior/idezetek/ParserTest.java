package hu.learning.junior.idezetek;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * A két válasz-feldolgozó tesztje mentett (saját készítésű) mintaválaszokon,
 * így a teszt NEM függ az internettől és a szolgáltatások elérhetőségétől.
 */
class ParserTest {

    /**
     * Beolvas egy tesztfájlt a classpath-ról (src/test/resources).
     *
     * @param nev a fájl neve
     * @return a tartalma szövegként
     */
    private static String eroforras(String nev) throws IOException {
        try (InputStream be = ParserTest.class.getResourceAsStream("/" + nev)) {
            assertNotNull(be, "Hiányzó tesztfájl: " + nev);
            return new String(be.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void quotableValaszFeldolgozasa() throws IOException {
        // Act: 3 elemből csak 2-t kérünk
        List<Idezet> lista = QuotableParser.feldolgoz(eroforras("quotable-minta.json"), 2);
        // Assert
        assertEquals(2, lista.size());
        assertEquals(new Idezet("Charles Dickens", "We need never be ashamed of our tears.", "quotable"),
                lista.getFirst());
    }

    @Test
    void quotableUresValasz() {
        assertTrue(QuotableParser.feldolgoz("{\"message\":\"hiba\"}", 3).isEmpty());
    }

    @Test
    void wikiquoteValaszFeldolgozasa() throws IOException {
        // Act
        List<Idezet> lista = WikiquoteParser.feldolgoz(eroforras("wikiquote-minta.json"), "George Bernard Shaw", 3);
        // Assert: a ** forrássorok, az üres * sor és a maximum feletti sor kimarad,
        // a wiki-jelölés eltűnik
        assertEquals(List.of(
                "My method is to take the utmost trouble to find the right thing to say, and then to say it with the utmost levity.",
                "Patriotism is, fundamentally, a conviction that a particular country is the best in the world because you were born in it.",
                "A life spent making mistakes is not only more honorable, but more useful than a life spent doing nothing."),
                lista.stream().map(Idezet::szoveg).toList());
        assertTrue(lista.stream().allMatch(i -> i.szerzo().equals("George Bernard Shaw")));
    }

    @Test
    void wikiJelolesTisztitasa() {
        assertEquals("a b c", WikiquoteParser.tisztit(" '''a''' [[x|b]] [[c]]<ref name=\"r\"/>"));
    }
}
