package hu.learning.webshop.tesztautomatizalas.taf.core;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Egyszerű, Pact-szerű „típus szerinti” JSON-egyeztető – a szerződésteszt magja.
 *
 * <p>Szabályok (a fogyasztó csak azt rögzíti, amit ténylegesen használ):</p>
 * <ul>
 *   <li><b>Objektum</b>: a példában szereplő minden mezőnek léteznie kell a valódi válaszban,
 *       azonos JSON-típussal (rekurzívan). A valódi válasz <em>további</em> mezőket tartalmazhat
 *       (Postel-elv: „légy megengedő abban, amit elfogadsz”).</li>
 *   <li><b>Tömb</b>: ha a példa nem üres, a valódi tömb sem lehet üres, és <em>minden</em> eleme
 *       a példa első elemének szerkezetét kell kövesse.</li>
 *   <li><b>Egyszerű érték</b> (szöveg, szám, logikai): csak a típus számít, az érték nem –
 *       kivéve az {@code exact} szabályokat (JSON Pointer → elvárt érték).</li>
 * </ul>
 *
 * <p>Általános (üzleti tudás nélküli) eszköz, ezért a core rétegben van. Saját tesztje: {@code JsonShapeMatcherTest} –
 * a VII. fejezet szerint a TAF komponenseit is ellenőrizni kell.</p>
 */
public final class JsonShapeMatcher {

    /** Segédosztály, nem példányosítható. */
    private JsonShapeMatcher() {
    }

    /**
     * Összeveti a valódi választ a példával és a pontos egyezési szabályokkal.
     *
     * @param expected a szerződésben szereplő példa
     * @param actual   a szolgáltató valódi válasza
     * @param exact    JSON Pointer → elvárt érték (lehet üres)
     * @return az eltérések olvasható listája; üres lista = megfelel
     */
    public static List<String> mismatches(JsonNode expected, JsonNode actual, Map<String, JsonNode> exact) {
        List<String> errors = new ArrayList<>();
        // 1. Szerkezet és típusok rekurzív összevetése a gyökértől
        compare("", expected, actual, errors);
        // 2. Pontos értékek ellenőrzése JSON Pointerrel (pl. "/items/0/quantity")
        exact.forEach((pointer, value) -> {
            JsonNode found = actual.at(pointer);
            if (found.isMissingNode()) {
                errors.add(pointer + ": hiányzik (pontos érték várva: " + value + ")");
            } else if (!sameValue(value, found)) {
                errors.add(pointer + ": várt érték " + value + ", kapott " + found);
            }
        });
        return errors;
    }

    /**
     * Rekurzív szerkezeti összevetés.
     *
     * @param path     az aktuális hely JSON Pointer formában (hibaüzenethez)
     * @param expected példa csomópont
     * @param actual   valódi csomópont
     * @param errors   ide gyűjtjük az eltéréseket
     */
    private static void compare(String path, JsonNode expected, JsonNode actual, List<String> errors) {
        String where = path.isEmpty() ? "/" : path;
        // 1. Hiányzó csomópont
        if (actual == null || actual.isMissingNode()) {
            errors.add(where + ": hiányzik");
            return;
        }
        // 2. Típuseltérés (pl. szám helyett szöveg)
        if (!sameType(expected, actual)) {
            errors.add(where + ": várt típus " + typeName(expected) + ", kapott " + typeName(actual));
            return;
        }
        // 3. Objektum: a példa minden mezőjére rekurzió
        if (expected.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = expected.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                compare(path + "/" + field.getKey(), field.getValue(), actual.get(field.getKey()), errors);
            }
        }
        // 4. Tömb: minden valódi elem a példa első elemének szerkezetét kövesse
        if (expected.isArray() && !expected.isEmpty()) {
            if (actual.isEmpty()) {
                errors.add(where + ": legalább 1 elemet vártunk, a tömb üres");
            }
            for (int i = 0; i < actual.size(); i++) {
                compare(path + "/" + i, expected.get(0), actual.get(i), errors);
            }
        }
    }

    /**
     * Két csomópont JSON-típusa azonos-e. A számoknál egész és tört között nem teszünk különbséget
     * ({@code 7990} és {@code 7990.00} is szám).
     */
    private static boolean sameType(JsonNode a, JsonNode b) {
        if (a.isNumber() && b.isNumber()) {
            return true;
        }
        return a.getNodeType() == b.getNodeType();
    }

    /** Érték-egyezés; számoknál skálafüggetlenül (2 == 2.00). */
    private static boolean sameValue(JsonNode expected, JsonNode actual) {
        if (expected.isNumber() && actual.isNumber()) {
            return expected.decimalValue().compareTo(actual.decimalValue()) == 0;
        }
        return expected.equals(actual);
    }

    /** Olvasható típusnév a hibaüzenethez. */
    private static String typeName(JsonNode node) {
        return node.getNodeType().name().toLowerCase();
    }
}
