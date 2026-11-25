package hu.learning.webshop.tesztautomatizalas.taf.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * A TAF saját komponensének ({@link JsonShapeMatcher}) tesztje – szerver nélkül, gyorsan.
 *
 * <p>Ha egy tesztkönyvtár hibás, „hamis zöld” vagy „hamis piros” eredményeket ad; ezért a
 * TAF kódját is tesztelni kell (VII. fejezet: TAF komponensek tesztelése).</p>
 */
class JsonShapeMatcherTest {

    /** JSON olvasó. */
    private final ObjectMapper mapper = new ObjectMapper();

    /** Szövegből JSON fa (rövidítés a tesztekhez). */
    private JsonNode json(String s) throws Exception {
        return mapper.readTree(s.replace('\'', '"'));
    }

    /** Extra mező és eltérő érték megengedett, ha a típus egyezik. */
    @Test
    void extraFieldsAndDifferentValuesAreAccepted() throws Exception {
        JsonNode expected = json("{'id':'P001','price':1}");
        JsonNode actual = json("{'id':'P777','price':7990.00,'name':'új mező'}");
        assertThat(JsonShapeMatcher.mismatches(expected, actual, Map.of())).isEmpty();
    }

    /** Hiányzó mező és típuseltérés kiderül – ez lenne a fogyasztó eltörése. */
    @Test
    void missingFieldAndWrongTypeAreReported() throws Exception {
        JsonNode expected = json("{'id':'P001','price':1}");
        JsonNode actual = json("{'price':'7990'}");
        assertThat(JsonShapeMatcher.mismatches(expected, actual, Map.of()))
                .containsExactly("/id: hiányzik", "/price: várt típus number, kapott string");
    }

    /** Tömb: minden elemet a példa első eleméhez mérünk; üres tömb nem elég. */
    @Test
    void arrayElementsAreCheckedAgainstFirstExample() throws Exception {
        JsonNode expected = json("[{'id':'x'}]");
        assertThat(JsonShapeMatcher.mismatches(expected, json("[{'id':'a'},{'nev':'b'}]"), Map.of()))
                .containsExactly("/1/id: hiányzik");
        assertThat(JsonShapeMatcher.mismatches(expected, json("[]"), Map.of()))
                .containsExactly("/: legalább 1 elemet vártunk, a tömb üres");
    }

    /** Pontos érték szabály: számoknál skálafüggetlen, eltérésnél jelez. */
    @Test
    void exactRules() throws Exception {
        JsonNode actual = json("{'total':2.00,'code':'A'}");
        assertThat(JsonShapeMatcher.mismatches(json("{}"), actual,
                Map.of("/total", json("2")))).isEmpty();
        assertThat(JsonShapeMatcher.mismatches(json("{}"), actual,
                Map.of("/code", json("'B'")))).containsExactly("/code: várt érték \"B\", kapott \"A\"");
    }
}
