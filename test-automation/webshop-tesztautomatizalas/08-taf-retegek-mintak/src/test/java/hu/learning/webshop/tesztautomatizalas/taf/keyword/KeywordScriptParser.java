package hu.learning.webshop.tesztautomatizalas.taf.keyword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Kulcsszó-táblázat (pontosvesszővel tagolt szövegfájl) beolvasása lépések listájává.
 *
 * <p>Formátum: soronként {@code KULCSSZO;arg1;arg2}; az üres és a {@code #}-tel kezdődő sorok
 * megjegyzések. A táblázatot teszt- vagy üzleti elemző is szerkesztheti Java tudás nélkül –
 * ez a kulcsszóvezérelt tesztelés (KDT) fő előnye a tananyag szerint.</p>
 */
public final class KeywordScriptParser {

    /** Segédosztály. */
    private KeywordScriptParser() {
    }

    /**
     * Beolvas egy forgatókönyvet a classpath-ról.
     *
     * @param resource classpath útvonal (pl. {@code /keywords/kosar-alap.csv})
     * @return a lépések listája
     * @throws IOException ha a fájl nem olvasható
     * @throws IllegalStateException ha a fájl nem található (TAS-hiba)
     */
    public static List<KeywordStep> parse(String resource) throws IOException {
        InputStream in = KeywordScriptParser.class.getResourceAsStream(resource);
        if (in == null) {
            throw new IllegalStateException("A forgatókönyv nem található: " + resource);
        }
        String fileName = resource.substring(resource.lastIndexOf('/') + 1);
        List<KeywordStep> steps = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            int lineNo = 0;
            // Soronkénti feldolgozás; a sorszámot a hibaüzenetekhez megőrizzük
            while ((line = reader.readLine()) != null) {
                lineNo++;
                String trimmed = line.strip();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue; // megjegyzés vagy üres sor
                }
                // Felbontás ';' mentén; az első elem a kulcsszó, a többi az argumentum
                List<String> cells = Arrays.stream(trimmed.split(";")).map(String::strip).toList();
                steps.add(new KeywordStep(fileName + ":" + lineNo,
                        cells.getFirst().toUpperCase(), cells.subList(1, cells.size())));
            }
        }
        return steps;
    }
}
